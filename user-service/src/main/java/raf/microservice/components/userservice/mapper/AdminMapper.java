package raf.microservice.components.userservice.mapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;
import raf.microservice.components.userservice.dto.AdminDto;
import raf.microservice.components.userservice.model.Admin;

@Component
public class AdminMapper {
    public AdminDto adminToAdminDto(Admin admin) {
        ModelMapper modelMapper = new ModelMapper();
        TypeMap<Admin, AdminDto> propertyMapper = modelMapper.createTypeMap(Admin.class, AdminDto.class);
        propertyMapper.addMapping(Admin::getUsername, AdminDto::setUsername);
        propertyMapper.addMapping(Admin::getEmail, AdminDto::setEmail);
        propertyMapper.addMapping(Admin::getDateBirth, AdminDto::setDateBirth);
        propertyMapper.addMapping(Admin::getName, AdminDto::setName);
        propertyMapper.addMapping(Admin::getLastName, AdminDto::setLastName);
        return modelMapper.map(admin, AdminDto.class);
    }
}
