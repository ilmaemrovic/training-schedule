package raf.microservice.components.userservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import raf.microservice.components.userservice.dto.*;
import raf.microservice.components.userservice.model.Manager;

public interface ManagerService {
    VerifyTokenDto add(ManagerCreateDto managerCreateDto);
    Manager findUsername(String username);
    AuthenticationResponseDto authenticate(AuthLoginDto authLoginDto);

    ManagerDto getMe(String authorization);

    SessionTokenDto refreshToken(String authorization);

    ManagerDto edit(String authorization, ManagerEditDto managerEditDto);

    void verify(String id);

    void changePassword(ChangePasswordDto changePasswordDto, String authorization);

    Page<ManagerAllDto> getAllManagers(Pageable pageable);
}
