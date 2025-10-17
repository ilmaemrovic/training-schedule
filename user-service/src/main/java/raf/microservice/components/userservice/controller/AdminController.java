package raf.microservice.components.userservice.controller;

import io.swagger.annotations.ApiOperation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import raf.microservice.components.userservice.dto.*;
import raf.microservice.components.userservice.service.AdminService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService){
        this.adminService = adminService;
    }

    @ApiOperation(value = "Login admin")
    @PostMapping("/login")  //  TODO: SHOULD SEND AN EMAIL THAT SOMEONE JUST LOGGED IN ACCOUNT
    public ResponseEntity<AuthenticationResponseDto> authenticate(@RequestBody @Valid AuthLoginDto authLoginDto){
        return new ResponseEntity<>(adminService.authenticate(authLoginDto), HttpStatus.OK);
    }

    @ApiOperation(value = "Refresh token")
    @PostMapping("/refresh-token")
    public ResponseEntity<SessionTokenDto> refreshToken(@RequestHeader("Authorization") String authorization){
        return new ResponseEntity<>(adminService.refreshToken(authorization), HttpStatus.OK);
    }

    @ApiOperation(value = "Get my information")
    @GetMapping("/me")
    public ResponseEntity<AdminDto> me(@RequestHeader("Authorization") String authorization){
        return new ResponseEntity<>(adminService.getMe(authorization), HttpStatus.OK);
    }

    @ApiOperation(value = "Edit admin")
    @PutMapping("/edit")
    public ResponseEntity<AdminDto> edit(@RequestHeader("Authorization") String authorization,
                                           @RequestBody @Valid AdminEditDto adminEditDto){
        return new ResponseEntity<>(adminService.edit(authorization, adminEditDto), HttpStatus.OK);
    }

    @ApiOperation(value = "Ban user")
    @PostMapping("/ban/user/{id}")
    public ResponseEntity<BanUserDto> banUser(@PathVariable("id") Long id){
        return new ResponseEntity<>(adminService.banUser(id), HttpStatus.OK);
    }

    @ApiOperation(value = "Ban manager")
    @PostMapping("/ban/manager/{id}")
    public ResponseEntity<BanManagerDto> banManager(@PathVariable("id") Long id){
        return new ResponseEntity<>(adminService.banManager(id), HttpStatus.OK);
    }

    @ApiOperation(value = "Unban user")
    @PostMapping("/unban/user/{id}")
    public ResponseEntity<BanUserDto> unbanUser(@PathVariable("id") Long id){
        return new ResponseEntity<>(adminService.unbanUser(id), HttpStatus.OK);
    }

    @ApiOperation(value = "Unban manager")
    @PostMapping("/unban/manager/{id}")
    public ResponseEntity<BanManagerDto> unbanManager(@PathVariable("id") Long id){
        return new ResponseEntity<>(adminService.unbanManger(id), HttpStatus.OK);
    }

}
