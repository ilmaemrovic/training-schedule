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
import raf.microservice.components.userservice.mapper.ClientMapper;
import raf.microservice.components.userservice.model.Client;
import raf.microservice.components.userservice.model.Token;
import raf.microservice.components.userservice.model.VerifyToken;
import raf.microservice.components.userservice.repository.ClientRepository;
import raf.microservice.components.userservice.repository.RoleRepository;
import raf.microservice.components.userservice.repository.TokenRepository;
import raf.microservice.components.userservice.repository.VerifyTokenRepository;
import raf.microservice.components.userservice.service.JwtService;
import raf.microservice.components.userservice.service.ClientService;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;
    private final VerifyTokenRepository verifyTokenRepository;
    private final RoleRepository roleRepository;
    private final String sendEmailDestination;
    private final JmsTemplate jmsTemplate;
    private final MessageHelper messageHelper;
    private final PasswordEncoder passwordEncoder;

    public ClientServiceImpl(ClientRepository clientRepository, ClientMapper clientMapper, JwtService jwtService, TokenRepository tokenRepository
    , AuthenticationManager authenticationManager, CustomUserDetailsService customUserDetailsService,
                             VerifyTokenRepository verifyTokenRepository, RoleRepository roleRepository
            , @Value("${destination.sendEmails}") String sendEmailDestination, JmsTemplate jmsTemplate,
                             MessageHelper messageHelper, PasswordEncoder passwordEncoder){
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
        this.jwtService = jwtService;
        this.tokenRepository = tokenRepository;
        this.authenticationManager = authenticationManager;
        this.customUserDetailsService = customUserDetailsService;
        this.verifyTokenRepository = verifyTokenRepository;
        this.roleRepository = roleRepository;
        this.sendEmailDestination = sendEmailDestination;
        this.jmsTemplate = jmsTemplate;
        this.passwordEncoder = passwordEncoder;
        this.messageHelper = messageHelper;
    }

    @Override
    public VerifyTokenDto add(ClientCreateDto clientCreateDto) {
        Client user = clientMapper.clientCreateDtoToClient(clientCreateDto);
        clientRepository.save(user);

        VerifyToken verifyToken = new VerifyToken();
        verifyToken.setDateValidTo(LocalDateTime.now().plusDays(1));
        verifyToken.setRevoked(false);
        verifyToken.generateVerifyToken();
        verifyToken.setUsername(clientCreateDto.getUsername());
        verifyTokenRepository.save(verifyToken);

        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("%name%", clientCreateDto.getName());
        paramsMap.put("%lastname%", clientCreateDto.getLastName());
        paramsMap.put("%link%", "http://localhost:9090/api/client/verify/" + verifyToken.getVerifyToken());
        TransferDto transferDto = new TransferDto(clientCreateDto.getEmail(), "REGISTER_USER", paramsMap, clientCreateDto.getUsername());

        jmsTemplate.convertAndSend(sendEmailDestination, messageHelper.createTextMessage(transferDto));


        return new VerifyTokenDto(verifyToken.verifyToken);
    }

    @Override
    public AuthenticationResponseDto authenticate(AuthLoginDto authLoginDto) {
        customUserDetailsService.setUserType("CLIENT");
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authLoginDto.getUsername(),
                        authLoginDto.getPassword()
                )
        );

        Client user = clientRepository.findClientByUsername(authLoginDto.getUsername()).orElseThrow();

        String jwtToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);

        AuthenticationResponseDto authenticationResponseDto = new AuthenticationResponseDto();
        authenticationResponseDto.setAccessToken(jwtToken);
        authenticationResponseDto.setRefreshToken(refreshToken);
        saveUserToken(user, refreshToken);

        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("%name%", user.getName());
        paramsMap.put("%lastname%", user.getLastName());
        TransferDto transferDto = new TransferDto(user.getEmail(), "LOGIN_USER", paramsMap, user.getUsername());
        jmsTemplate.convertAndSend(sendEmailDestination, messageHelper.createTextMessage(transferDto));

        return authenticationResponseDto;
    }

    private void revokeAllUserTokens(Client user) { // TODO: transfer to JWT class
        var validUserTokens = tokenRepository.findAllValidTokenByUsersDetails(user);
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    private void saveUserToken(Client user, String jwt){ // TODO: transfer to JWT class
        Token token = new Token();
        token.setToken(jwt);
        token.setUsersDetails(user);
        token.setExpired(false);
        token.setRevoked(false);
        tokenRepository.save(token);
    }

    @Override
    public Client findUsername(String username) {
        return clientRepository.findClientByUsername(username).orElse(null);
    }

    @Override
    public SessionTokenDto refreshToken(String authorization) { // TODO: transfer to JWT class
        String refreshToken;
        String username;
        if(authorization == null ||!authorization.startsWith("Bearer "))
            return null;

        refreshToken = authorization.substring(7);
        username = jwtService.extractUsername(refreshToken);

        if (username != null) {
            Client user = clientRepository.findClientByUsername(username)
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
    public ClientDto getMe(String authorization) {
        String token = authorization.substring(7);
        Optional<Client> user = clientRepository.findClientByUsername(jwtService.extractUsername(token));
        if(user.isEmpty()) throw new NotFoundException("Client not found");
        return clientMapper.clientToClientDto(user.get());
    }

    @Override
    public ClientDto edit(String authorization, ClientEditDto clientEditDto) {
        String token = authorization.substring(7);
        Optional<Client> user = clientRepository.findClientByUsername(jwtService.extractUsername(token));
        if(user.isEmpty()) throw new NotFoundException("Client not found");

        Client userNew = user.get();
        userNew.setEmail(clientEditDto.getEmail());
        userNew.setName(clientEditDto.getName());
        userNew.setLastName(clientEditDto.getLastName());
        userNew.setDateBirth(clientEditDto.getDateBirth());
        clientRepository.save(userNew);
        return clientMapper.clientToClientDto(userNew);
    }

    @Override
    public void verify(String id) {
        Optional<VerifyToken> verifyToken = verifyTokenRepository.findVerifyTokenByVerifyToken(id);
        if(verifyToken.isEmpty()) throw new NotFoundException("Verify token not found");
        VerifyToken verifyTokenF = verifyToken.get();

        if(verifyTokenF.isRevoked() || verifyTokenF.getDateValidTo().isBefore(LocalDateTime.now())) throw new NotFoundException("Not valid token");

        verifyTokenF.setRevoked(true);
        verifyTokenRepository.save(verifyTokenF);
        Optional<Client> user = clientRepository.findClientByUsername(verifyTokenF.getUsername());
        if(user.isEmpty()) throw new NotFoundException("User not found");

        user.get().setRole(roleRepository.findRoleByName("ROLE_CLIENT").get());
        clientRepository.save(user.get());
    }

    @Override
    public void changePassword(ChangePasswordDto changePasswordDto, String authorization) {
        String token = authorization.substring(7);
        Optional<Client> user = clientRepository.findClientByUsername(jwtService.extractUsername(token));
        if(user.isEmpty()) throw new NotFoundException("Client not found");

        Client userNew = user.get();

        if(!passwordEncoder.matches(changePasswordDto.getOldPassword(), userNew.getPassword()))
            throw new NotFoundException("Password not found!");

        userNew.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
        clientRepository.save(userNew);

        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("%name%", userNew.getName());
        paramsMap.put("%lastname%", userNew.getLastName());
        TransferDto transferDto = new TransferDto(userNew.getEmail(), "CHANGED_PASSWORD", paramsMap, userNew.getUsername());
        jmsTemplate.convertAndSend(sendEmailDestination, messageHelper.createTextMessage(transferDto));


    }

    @Override
    public ClientDto editTrainingCount(ClientDto clientDto) {
        Optional<Client> user = clientRepository.findClientByUsername(clientDto.getUsername());
        if(user.isEmpty()) throw new NotFoundException("Client not found");

        Client userNew = user.get();
        userNew.setScheduledTrainingCount(clientDto.getScheduledTrainingCount());
        clientRepository.save(userNew);
        return clientMapper.clientToClientDto(userNew);
    }

    @Override
    public Page<ClientAllDto> getAllUsers(Pageable pageable) {
        Page<Client> clients = clientRepository.findAll(pageable);

        if(clients.isEmpty()) throw new NotFoundException("There are no users");

        List<ClientAllDto> clientsDtoList = clients
                .stream()
                .map(clientMapper::clientToClientAllDto).collect(Collectors.toList());

        return new PageImpl<>(clientsDtoList, pageable, clients.getTotalElements());
    }

    @Override
    public ClientDto getUserById(Long id) {
        Optional<Client> client = clientRepository.findById(id);
        if(client.isEmpty()) throw new NotFoundException("There are no users");
        return clientMapper.clientToClientDto(client.get());
    }


}
