package raf.microservice.components.userservice.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raf.microservice.components.userservice.dto.*;
import raf.microservice.components.userservice.exceptions.NotFoundException;
import raf.microservice.components.userservice.listener.MessageHelper;
import raf.microservice.components.userservice.mapper.ManagerMapper;
import raf.microservice.components.userservice.model.Client;
import raf.microservice.components.userservice.model.Manager;
import raf.microservice.components.userservice.model.Token;
import raf.microservice.components.userservice.model.VerifyToken;
import raf.microservice.components.userservice.repository.ManagerRepository;
import raf.microservice.components.userservice.repository.RoleRepository;
import raf.microservice.components.userservice.repository.TokenRepository;
import raf.microservice.components.userservice.repository.VerifyTokenRepository;
import raf.microservice.components.userservice.service.JwtService;
import raf.microservice.components.userservice.service.ManagerService;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ManagerServiceImpl implements ManagerService {

    private final ManagerRepository managerRepository;
    private final ManagerMapper managerMapper;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService customUserDetailsService;
    private final VerifyTokenRepository verifyTokenRepository;
    private final RoleRepository roleRepository;
    private final String sendEmailDestination;
    private final JmsTemplate jmsTemplate;
    private final MessageHelper messageHelper;
    public ManagerServiceImpl(ManagerRepository managerRepository, ManagerMapper managerMapper, JwtService jwtService,
                              TokenRepository tokenRepository, AuthenticationManager authenticationManager,
                              PasswordEncoder passwordEncoder, CustomUserDetailsService customUserDetailsService,
                              VerifyTokenRepository verifyTokenRepository, RoleRepository roleRepository,
                              @Value("${destination.sendEmails}") String sendEmailDestination, JmsTemplate jmsTemplate,
                              MessageHelper messageHelper){
        this.managerMapper = managerMapper;
        this.managerRepository = managerRepository;
        this.jwtService = jwtService;
        this.tokenRepository = tokenRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.customUserDetailsService = customUserDetailsService;
        this.verifyTokenRepository = verifyTokenRepository;
        this.roleRepository = roleRepository;
        this.sendEmailDestination = sendEmailDestination;
        this.jmsTemplate = jmsTemplate;
        this.messageHelper = messageHelper;
    }

    @Override
    public VerifyTokenDto add(ManagerCreateDto managerCreateDto) {
        Manager manager = managerMapper.managerCreateDtoToManager(managerCreateDto);
        managerRepository.save(manager);

        VerifyToken verifyToken = new VerifyToken();
        verifyToken.setDateValidTo(LocalDateTime.now().plusDays(1));
        verifyToken.setRevoked(false);
        verifyToken.generateVerifyToken();
        verifyToken.setUsername(managerCreateDto.getUsername());
        verifyTokenRepository.save(verifyToken);
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("%name%", managerCreateDto.getName());
        paramsMap.put("%lastname%", managerCreateDto.getLastName());
        paramsMap.put("%link%", "http://localhost:9090/api/manager/verify/" + verifyToken.getVerifyToken());
        TransferDto transferDto = new TransferDto(managerCreateDto.getEmail(), "REGISTER_USER", paramsMap, managerCreateDto.getUsername());
        jmsTemplate.convertAndSend(sendEmailDestination, messageHelper.createTextMessage(transferDto));

        return new VerifyTokenDto(verifyToken.verifyToken);
    }

    private void saveUserToken(Manager user, String jwt){
        Token token = new Token();
        token.setToken(jwt);
        token.setUsersDetails(user);
        token.setExpired(false);
        token.setRevoked(false);
        tokenRepository.save(token);
    }

    @Override
    public Manager findUsername(String username) {
        return managerRepository.findManagerByUsername(username).orElse(null);
    }

    @Override
    public AuthenticationResponseDto authenticate(AuthLoginDto authLoginDto) {
        customUserDetailsService.setUserType("MANAGER");
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authLoginDto.getUsername(),
                        authLoginDto.getPassword()
                )
        );

        Manager manager = managerRepository.findManagerByUsername(authLoginDto.getUsername()).orElseThrow();

        String jwtToken = jwtService.generateToken(manager);
        String refreshToken = jwtService.generateRefreshToken(manager);
        revokeAllUserTokens(manager);

        AuthenticationResponseDto authenticationResponseDto = new AuthenticationResponseDto();
        authenticationResponseDto.setAccessToken(jwtToken);
        authenticationResponseDto.setRefreshToken(refreshToken);
        saveUserToken(manager, refreshToken);

        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("%name%", manager.getName());
        paramsMap.put("%lastname%", manager.getLastName());
        TransferDto transferDto = new TransferDto(manager.getEmail(), "LOGIN_USER", paramsMap, manager.getUsername());
        jmsTemplate.convertAndSend(sendEmailDestination, messageHelper.createTextMessage(transferDto));

        return authenticationResponseDto;
    }

    @Override
    public ManagerDto getMe(String authorization) { // TODO: transfer to JWT class
        String token = authorization.substring(7);
        Optional<Manager> manager = managerRepository.findManagerByUsername(jwtService.extractUsername(token));
        if(manager.isEmpty()) throw new NotFoundException("Manager not found");
        return managerMapper.managerToManagerDto(manager.get());
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
            Manager user = managerRepository.findManagerByUsername(username)
                    .orElseThrow();

            boolean check = true;
            boolean exist = false;

            List<Token> allTokens = tokenRepository.findAllValidTokenByUsersDetails(user);
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

            if (jwtService.isTokenValid(refreshToken, user) && check && exist) {
                System.out.println(allTokens);
                String accessToken = jwtService.generateToken(user);
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
    public ManagerDto edit(String authorization, ManagerEditDto managerEditDto) {
        String token = authorization.substring(7);
        Optional<Manager> manager = managerRepository.findManagerByUsername(jwtService.extractUsername(token));
        if(manager.isEmpty()) throw new NotFoundException("Manager not found");

        Manager managerNew = manager.get();
        managerNew.setEmail(managerEditDto.getEmail());
        managerNew.setName(managerEditDto.getName());
        managerNew.setLastName(managerEditDto.getLastName());
        managerNew.setDateBirth(managerEditDto.getDateBirth());
        managerNew.setDateEmployment(managerEditDto.getDateEmployment());
        managerNew.setSportsHall(managerEditDto.getSportsHall());
        managerRepository.save(managerNew);
        return managerMapper.managerToManagerDto(managerNew);
    }

    @Override
    public void verify(String id) {
        Optional<VerifyToken> verifyToken = verifyTokenRepository.findVerifyTokenByVerifyToken(id);
        if(verifyToken.isEmpty()) throw new NotFoundException("Verify token not found");
        VerifyToken verifyTokenF = verifyToken.get();

        if(verifyTokenF.isRevoked() || verifyTokenF.getDateValidTo().isBefore(LocalDateTime.now())) throw new NotFoundException("Not valid token");

        verifyTokenF.setRevoked(true);
        verifyTokenRepository.save(verifyTokenF);
        Optional<Manager> manager = managerRepository.findManagerByUsername(verifyTokenF.getUsername());
        if(manager.isEmpty()) throw new NotFoundException("User not found");

        manager.get().setRole(roleRepository.findRoleByName("ROLE_MANAGER").get());
        managerRepository.save(manager.get());
    }

    @Override
    public void changePassword(ChangePasswordDto changePasswordDto, String authorization) {
        String token = authorization.substring(7);
        Optional<Manager> user = managerRepository.findManagerByUsername(jwtService.extractUsername(token));
        if(user.isEmpty()) throw new NotFoundException("Client not found");

        Manager managerNew = user.get();

        if(!passwordEncoder.matches(changePasswordDto.getOldPassword(), managerNew.getPassword()))
            throw new NotFoundException("Password not found!");

        managerNew.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
        managerRepository.save(managerNew);

        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("%name%", managerNew.getName());
        paramsMap.put("%lastname%", managerNew.getLastName());
        TransferDto transferDto = new TransferDto(managerNew.getEmail(), "CHANGED_PASSWORD", paramsMap, managerNew.getUsername());
        jmsTemplate.convertAndSend(sendEmailDestination, messageHelper.createTextMessage(transferDto));
    }

    @Override
    public Page<ManagerAllDto> getAllManagers(Pageable pageable) {
        Page<Manager> managers = managerRepository.findAll(pageable);

        if(managers.isEmpty()) throw new NotFoundException("There are no managers");

        List<ManagerAllDto> managerDtoList = managers
                .stream()
                .map(managerMapper::managerToManagerAllDto).collect(Collectors.toList());

        return new PageImpl<>(managerDtoList, pageable, managers.getTotalElements());
    }

    private void revokeAllUserTokens(Manager user) { // TODO: transfer to JWT class
        var validUserTokens = tokenRepository.findAllValidTokenByUsersDetails(user);
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

}