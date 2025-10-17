package raf.microservice.components.userservice.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import raf.microservice.components.userservice.dto.*;
import raf.microservice.components.userservice.security.CheckExists;
import raf.microservice.components.userservice.service.ManagerService;

@RestController
@RequestMapping("/api/manager")
public class ManagerController {

    private final ManagerService managerService;

    public ManagerController(ManagerService managerService){this.managerService = managerService;}

    @ApiOperation(value = "Register new manager")
    @PostMapping("/register")
    @CheckExists
    public ResponseEntity<Void> saveManager(@RequestBody @Valid ManagerCreateDto managerCreateDto){
        managerService.add(managerCreateDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ApiOperation(value = "Verify manager")
    @GetMapping("/verify/{id}")
    public String findById(@PathVariable("id") String id) {
        managerService.verify(id);
        return "<!DOCTYPE html><html lang=\"en\"><head><meta charset=\"UTF-8\"><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"><title>Account Verification</title><style>body {font-family: Arial, sans-serif;background-color: #f4f4f4;color: #333;text-align: center;margin: 50px;}h1 {color: #007bff;}</style></head><body><div><h1>Account Verification Successful</h1><p>Your account has been successfully verified. Thank you!</p></div></body></html>";
    }


    @ApiOperation(value = "Login")
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDto> authenticate(@RequestBody @Valid AuthLoginDto authLoginDto) {
        return new ResponseEntity<>(managerService.authenticate(authLoginDto), HttpStatus.OK);
    }

    @ApiOperation(value = "Change password")
    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(@RequestHeader("Authorization") String authorization,
                                               @RequestBody @Valid ChangePasswordDto changePasswordDto){
        managerService.changePassword(changePasswordDto, authorization);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "Refresh token")
    @PostMapping("/refresh-token")
    public ResponseEntity<SessionTokenDto> refreshToken(@RequestHeader("Authorization") String authorization){
        return new ResponseEntity<>(managerService.refreshToken(authorization), HttpStatus.OK);
    }

    @ApiOperation(value = "Get my information")
    @GetMapping("/me")
    public ResponseEntity<ManagerDto> me(@RequestHeader("Authorization") String authorization){
        return new ResponseEntity<>(managerService.getMe(authorization), HttpStatus.OK);
    }

    @ApiOperation(value = "Edit manager")
    @PutMapping("/edit")
    public ResponseEntity<ManagerDto> edit(@RequestHeader("Authorization") String authorization,
                                        @RequestBody @Valid ManagerEditDto managerEditDto){
        return new ResponseEntity<>(managerService.edit(authorization, managerEditDto), HttpStatus.OK);
    }

    @ApiOperation(value = "Get all managers")
    @GetMapping("/all")
    public ResponseEntity<Page<ManagerAllDto>> allManagers(@RequestHeader("Authorization") String authorization, Pageable pageable){
        return new ResponseEntity<>(managerService.getAllManagers(pageable), HttpStatus.OK);
    }
}
