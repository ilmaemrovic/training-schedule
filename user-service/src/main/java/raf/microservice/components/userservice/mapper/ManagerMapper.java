package raf.microservice.components.userservice.mapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import raf.microservice.components.userservice.dto.ManagerAllDto;
import raf.microservice.components.userservice.dto.ManagerCreateDto;
import raf.microservice.components.userservice.dto.ManagerDto;
import raf.microservice.components.userservice.model.Manager;
import raf.microservice.components.userservice.repository.RoleRepository;

@Component
public class ManagerMapper {

    @Autowired
    private PasswordEncoder passwordEncoder;
    private RoleRepository roleRepository;

    public ManagerMapper(RoleRepository roleRepository){
        this.roleRepository = roleRepository;
    }

    public Manager managerCreateDtoToManager(ManagerCreateDto managerCreateDto){
        ModelMapper modelMapper = new ModelMapper();
        TypeMap<ManagerCreateDto, Manager> propertyMapper = modelMapper.createTypeMap(ManagerCreateDto.class, Manager.class);
        propertyMapper.addMapping(ManagerCreateDto::getUsername, Manager::setUsername);
        propertyMapper.addMapping(ManagerCreateDto::getPassword, Manager::setPassword);
        propertyMapper.addMapping(ManagerCreateDto::getEmail, Manager::setEmail);
        propertyMapper.addMapping(ManagerCreateDto::getDateBirth, Manager::setDateBirth);
        propertyMapper.addMapping(ManagerCreateDto::getName, Manager::setName);
        propertyMapper.addMapping(ManagerCreateDto::getLastName, Manager::setLastName);
        propertyMapper.addMapping(ManagerCreateDto::getSportsHall, Manager::setSportsHall);
        propertyMapper.addMapping(ManagerCreateDto::getDateEmployment, Manager::setDateEmployment);

        Manager manager = modelMapper.map(managerCreateDto, Manager.class);
        manager.setRole(roleRepository.findRoleByName("ROLE_PENDING").get());
        manager.setPassword(passwordEncoder.encode(manager.getPassword()));
        return manager;
    }

    public ManagerDto managerToManagerDto(Manager manager){
        ModelMapper modelMapper = new ModelMapper();
        TypeMap<Manager, ManagerDto> propertyMapper = modelMapper.createTypeMap(Manager.class, ManagerDto.class);
        propertyMapper.addMapping(Manager::getId, ManagerDto::setId);
        propertyMapper.addMapping(Manager::getUsername, ManagerDto::setUsername);
        propertyMapper.addMapping(Manager::getEmail, ManagerDto::setEmail);
        propertyMapper.addMapping(Manager::getDateBirth, ManagerDto::setDateBirth);
        propertyMapper.addMapping(Manager::getName, ManagerDto::setName);
        propertyMapper.addMapping(Manager::getLastName, ManagerDto::setLastName);
        propertyMapper.addMapping(Manager::getSportsHall, ManagerDto::setSportsHall);
        propertyMapper.addMapping(Manager::getDateEmployment, ManagerDto::setDateEmployment);

        return modelMapper.map(manager, ManagerDto.class);
    }

    public ManagerAllDto managerToManagerAllDto(Manager manager) {
        ManagerAllDto managerAllDto = new ManagerAllDto();
        managerAllDto.setId(manager.getId());
        managerAllDto.setDateEmployment(manager.getDateEmployment());
        managerAllDto.setName(manager.getName());
        managerAllDto.setEmail(manager.getEmail());
        managerAllDto.setLastName(manager.getLastName());
        managerAllDto.setDateBirth(manager.getDateBirth());
        managerAllDto.setRole(manager.getRole());
        managerAllDto.setSportsHall(manager.getSportsHall());
        managerAllDto.setUsername(manager.getUsername());

        return  managerAllDto;
    }
}
