package raf.microservice.components.userservice.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import raf.microservice.components.userservice.exceptions.BannedUserException;
import raf.microservice.components.userservice.exceptions.NotFoundException;
import raf.microservice.components.userservice.model.Admin;
import raf.microservice.components.userservice.model.Client;
import raf.microservice.components.userservice.model.Manager;
import raf.microservice.components.userservice.model.Role;
import raf.microservice.components.userservice.repository.AdminRepository;
import raf.microservice.components.userservice.repository.ClientRepository;
import raf.microservice.components.userservice.repository.ManagerRepository;
import raf.microservice.components.userservice.service.JwtService;

import java.sql.SQLOutput;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final ClientRepository clientRepository;
    private final ManagerRepository managerRepository;
    private final AdminRepository adminRepository;
    private final JwtService jwtService;
    public String userType = "CLIENT";

    public CustomUserDetailsService(ClientRepository clientRepository, ManagerRepository managerRepository,
                                    AdminRepository adminRepository, JwtService jwtService) {
        this.clientRepository = clientRepository;
        this.managerRepository = managerRepository;
        this.adminRepository = adminRepository;
        this.jwtService = jwtService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails userDetails;
        switch (userType) {
            case "MANAGER" -> {
                Optional<Manager> manager = managerRepository.findManagerByUsername(username);
                userDetails = manager.orElseThrow(() -> new NotFoundException("Manager not found!"));
                if(userDetails.getAuthorities().toArray()[0].toString().equals("ROLE_BANNED"))
                    throw new BannedUserException("Manager is banned");
                if(userDetails.getAuthorities().toArray()[0].toString().equals("ROLE_PENDING"))
                    throw new BannedUserException("Manager is on pending");

            }
            case "CLIENT" -> {
                Optional<Client> user = clientRepository.findClientByUsername(username);
                userDetails = user.orElseThrow(() -> new NotFoundException("Client not found!"));
                if(userDetails.getAuthorities().toArray()[0].toString().equals("ROLE_BANNED"))
                    throw new BannedUserException("Client is banned");
                if(userDetails.getAuthorities().toArray()[0].toString().equals("ROLE_PENDING"))
                    throw new BannedUserException("Client is on pending");
            }
            case "ADMIN" -> {
                Optional<Admin> admin = adminRepository.findAdminByUsername(username);
                userDetails = admin.orElseThrow(() -> new NotFoundException("Admin not found!"));
            }
            default -> throw new UsernameNotFoundException("No user");
        }

        return userDetails;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
