package raf.microservice.components.userservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import raf.microservice.components.userservice.dto.*;
import raf.microservice.components.userservice.model.Client;

public interface ClientService {
    VerifyTokenDto add(ClientCreateDto clientCreateDto);
    AuthenticationResponseDto authenticate(AuthLoginDto authLoginDto);
    Client findUsername(String username);
    SessionTokenDto refreshToken(String authorization);
    ClientDto getMe(String authorization);
    ClientDto edit(String authorization, ClientEditDto clientEditDto);
    void verify(String id);
    void changePassword(ChangePasswordDto changePasswordDto, String authorization);
    ClientDto editTrainingCount(ClientDto clientDto);
    Page<ClientAllDto> getAllUsers(Pageable pageable);
    ClientDto getUserById(Long id);
}
