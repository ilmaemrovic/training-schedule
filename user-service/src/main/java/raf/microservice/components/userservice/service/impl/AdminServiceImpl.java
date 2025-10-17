package raf.microservice.components.userservice.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raf.microservice.components.userservice.dto.*;
import raf.microservice.components.userservice.exceptions.NotFoundException;
import raf.microservice.components.userservice.listener.MessageHelper;
import raf.microservice.components.userservice.mapper.AdminMapper;
import raf.microservice.components.userservice.mapper.ManagerMapper;
import raf.microservice.components.userservice.mapper.ClientMapper;
import raf.microservice.components.userservice.model.Admin;
import raf.microservice.components.userservice.model.Client;
import raf.microservice.components.userservice.model.Manager;
import raf.microservice.components.userservice.model.Token;
import raf.microservice.components.userservice.repository.*;
import raf.microservice.components.userservice.service.AdminService;
import raf.microservice.components.userservice.service.JwtService;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JmsTemplate jmsTemplate;
    private final CustomUserDetailsService customUserDetailsService;
    private final AdminMapper adminMapper;
    private final ClientRepository clientRepository;
    private final RoleRepository roleRepository;
    private final ClientMapper clientMapper;
    private final ManagerRepository managerRepository;
    private final ManagerMapper managerMapper;
    private final MessageHelper messageHelper;
    private final String sendEmailDestination;

    public AdminServiceImpl(JwtService jwtService, TokenRepository tokenRepository,
                            AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder,
                            CustomUserDetailsService customUserDetailsService, AdminRepository adminRepository,
                            AdminMapper adminMapper, ClientRepository clientRepository, RoleRepository roleRepository,
                            ClientMapper clientMapper, ManagerRepository managerRepository, ManagerMapper managerMapper,
                            JmsTemplate jmsTemplate, MessageHelper messageHelper, @Value("${destination.sendEmails}") String sendEmailDestination) {
        this.jwtService = jwtService;
        this.tokenRepository = tokenRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.customUserDetailsService = customUserDetailsService;
        this.adminRepository = adminRepository;
        this.adminMapper = adminMapper;
        this.clientRepository = clientRepository;
        this.roleRepository = roleRepository;
        this.clientMapper = clientMapper;
        this.managerRepository = managerRepository;
        this.managerMapper = managerMapper;
        this.jmsTemplate = jmsTemplate;
        this.messageHelper = messageHelper;
        this.sendEmailDestination = sendEmailDestination;
    }

    @Override
    public AuthenticationResponseDto authenticate(AuthLoginDto authLoginDto) throws NotFoundException {
        customUserDetailsService.setUserType("ADMIN");
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authLoginDto.getUsername(),
                        authLoginDto.getPassword()
                )
        );

        Admin admin = adminRepository.findAdminByUsername(authLoginDto.getUsername()).orElseThrow();

        String jwtToken = jwtService.generateToken(admin);
        String refreshToken = jwtService.generateRefreshToken(admin);
        revokeAllUserTokens(admin);

        AuthenticationResponseDto authenticationResponseDto = new AuthenticationResponseDto();
        authenticationResponseDto.setAccessToken(jwtToken);
        authenticationResponseDto.setRefreshToken(refreshToken);
        saveUserToken(admin, refreshToken);

        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("%name%", admin.getName());
        paramsMap.put("%lastname%", admin.getLastName());
        TransferDto transferDto = new TransferDto(admin.getEmail(), "LOGIN_USER", paramsMap, admin.getUsername());
        jmsTemplate.convertAndSend(sendEmailDestination, messageHelper.createTextMessage(transferDto));

        return authenticationResponseDto;
    }

    @Override
    public SessionTokenDto refreshToken(String authorization) {
        String refreshToken;
        String username;
        if(authorization == null ||!authorization.startsWith("Bearer "))
            return null;

        refreshToken = authorization.substring(7);
        username = jwtService.extractUsername(refreshToken);

        if (username != null) {
            Admin admin = adminRepository.findAdminByUsername(username)
                    .orElseThrow();

            boolean check = true;
            boolean exist = false;

            List<Token> allTokens = tokenRepository.findAllValidTokenByUsersDetails(admin);
            Token update = null;
            for(Token t : allTokens){
                if (t.token.equals(refreshToken)) {
                    exist = true;
                    update = t;
                    if(t.expired || t.revoked)
                        check = false;

                    break;
                }
            }

            if (jwtService.isTokenValid(refreshToken, admin) && check && exist) {
                System.out.println(allTokens);
                String accessToken = jwtService.generateToken(admin);
                SessionTokenDto sessionTokenDto = new SessionTokenDto();
                sessionTokenDto.setAccessToken(accessToken);
                return sessionTokenDto;
            }

            if(update != null){  // token does not exist
                update.setRevoked(true);
                update.setExpired(true);
                tokenRepository.save(update);
            }
        }

        return null;
    }

    @Override
    public AdminDto getMe(String authorization) {
        String token = authorization.substring(7);
        Optional<Admin> admin = adminRepository.findAdminByUsername(jwtService.extractUsername(token));
        if(admin.isEmpty()) throw new NotFoundException("Admin not found");
        return adminMapper.adminToAdminDto(admin.get());
    }

    @Override
    public AdminDto edit(String authorization, AdminEditDto adminEditDto) {
        String token = authorization.substring(7);
        Optional<Admin> admin = adminRepository.findAdminByUsername(jwtService.extractUsername(token));
        if(admin.isEmpty()) throw new NotFoundException("Admin not found");

        Admin adminNew = admin.get();
        adminNew.setEmail(adminEditDto.getEmail());
        adminNew.setName(adminEditDto.getName());
        adminNew.setLastName(adminEditDto.getLastName());
        adminNew.setDateBirth(adminEditDto.getDateBirth());
        adminRepository.save(adminNew);
        return adminMapper.adminToAdminDto(adminNew);
    }

    @Override
    public BanUserDto banUser(Long id) throws NotFoundException{
        Optional<Client> user = clientRepository.findById(id);

        if(user.isEmpty())
            throw new NotFoundException("Client not found");

        Client userP = user.get();
        userP.setRole(roleRepository.findRoleByName("ROLE_BANNED").get());

        clientRepository.save(userP);
        BanUserDto banUserDto = new BanUserDto();

        banUserDto.setUser(clientMapper.clientToClientDto(userP));
        banUserDto.setBanned(true);

        return banUserDto;
    }

    @Override
    public BanManagerDto banManager(Long id) {
        Optional<Manager> manager = managerRepository.findById(id);

        if(manager.isEmpty())
            throw new NotFoundException("Manager not found");

        Manager managerP = manager.get();
        managerP.setRole(roleRepository.findRoleByName("ROLE_BANNED").get());

        managerRepository.save(managerP);
        BanManagerDto banManagerDto = new BanManagerDto();

        banManagerDto.setManager(managerMapper.managerToManagerDto(managerP));
        banManagerDto.setBanned(true);

        return banManagerDto;
    }

    @Override
    public BanUserDto unbanUser(Long id) {
        Optional<Client> user = clientRepository.findById(id);

        if(user.isEmpty())
            throw new NotFoundException("Client not found");

        Client userP = user.get();
        userP.setRole(roleRepository.findRoleByName("ROLE_CLIENT").get());

        clientRepository.save(userP);
        BanUserDto banUserDto = new BanUserDto();

        banUserDto.setUser(clientMapper.clientToClientDto(userP));
        banUserDto.setBanned(false);

        return banUserDto;
    }

    @Override
    public BanManagerDto unbanManger(Long id){
        Optional<Manager> manager = managerRepository.findById(id);

        if(manager.isEmpty())
            throw new NotFoundException("Manager not found");

        Manager managerP = manager.get();
        managerP.setRole(roleRepository.findRoleByName("ROLE_MANAGER").get());

        managerRepository.save(managerP);
        BanManagerDto banManagerDto = new BanManagerDto();

        banManagerDto.setManager(managerMapper.managerToManagerDto(managerP));
        banManagerDto.setBanned(false);

        return banManagerDto;
    }

    private void revokeAllUserTokens(Admin admin) {
        var validUserTokens = tokenRepository.findAllValidTokenByUsersDetails(admin);
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    private void saveUserToken(Admin admin, String jwt){
        Token token = new Token();
        token.setToken(jwt);
        token.setUsersDetails(admin);
        token.setExpired(false);
        token.setRevoked(false);
        tokenRepository.save(token);
    }

}
