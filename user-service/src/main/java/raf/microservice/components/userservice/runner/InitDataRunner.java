package raf.microservice.components.userservice.runner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import raf.microservice.components.userservice.model.Admin;
import raf.microservice.components.userservice.model.Role;
import raf.microservice.components.userservice.repository.AdminRepository;
import raf.microservice.components.userservice.repository.ClientRepository;
import raf.microservice.components.userservice.repository.ManagerRepository;
import raf.microservice.components.userservice.repository.RoleRepository;

import java.time.LocalDate;
import java.time.Month;

@Profile({"default"})
@Component
public class InitDataRunner implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private ClientRepository clientRepository;
    private final AdminRepository adminRepository;
    private ManagerRepository managerRepository;
    private final PasswordEncoder passwordEncoder;

    public InitDataRunner(RoleRepository roleRepository, ClientRepository clientRepository,
                          AdminRepository adminRepository, ManagerRepository managerRepository,
                          PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.clientRepository = clientRepository;
        this.adminRepository = adminRepository;
        this.managerRepository = managerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
//        Role roleUser = new Role("ROLE_CLIENT", "Client role");
//        Role roleAdmin = new Role("ROLE_ADMIN", "Admin role");
//        Role roleManager = new Role("ROLE_MANAGER", "Manager role");
//        Role roleBanned = new Role("ROLE_BANNED", "Banned role");
//        Role rolePending = new Role("ROLE_PENDING", "Pending role");
//        roleRepository.save(roleUser);
//        roleRepository.save(roleAdmin);
//        roleRepository.save(roleManager);
//        roleRepository.save(roleBanned);
//        roleRepository.save(rolePending);
//
//        Admin admin = new Admin();
//        admin.setEmail("karisik.mehmedalija@gmail.com");
//        admin.setUsername("karisikm");
//        admin.setPassword(passwordEncoder.encode("biguser123"));
//        admin.setRole(roleAdmin);
//        admin.setDateBirth(LocalDate.of(2002, Month.APRIL, 23));
//        admin.setName("Mehmedalija");
//        admin.setLastName("Karisik");
//
//        adminRepository.save(admin);
    }
}
