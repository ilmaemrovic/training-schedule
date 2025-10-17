package raf.microservice.components.userservice.mapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import raf.microservice.components.userservice.dto.ClientAllDto;
import raf.microservice.components.userservice.dto.ClientCreateDto;
import raf.microservice.components.userservice.dto.ClientDto;
import raf.microservice.components.userservice.model.Client;
import raf.microservice.components.userservice.repository.RoleRepository;

@Component
public class ClientMapper {
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public ClientMapper(RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public Client clientCreateDtoToClient(ClientCreateDto clientCreateDto){
        ModelMapper modelMapper = new ModelMapper();
        TypeMap<ClientCreateDto, Client> propertyMapper = modelMapper.createTypeMap(ClientCreateDto.class, Client.class);
        propertyMapper.addMapping(ClientCreateDto::getUsername, Client::setUsername);
        propertyMapper.addMapping(ClientCreateDto::getPassword, Client::setPassword);
        propertyMapper.addMapping(ClientCreateDto::getEmail, Client::setEmail);
        propertyMapper.addMapping(ClientCreateDto::getDateBirth, Client::setDateBirth);
        propertyMapper.addMapping(ClientCreateDto::getName, Client::setName);
        propertyMapper.addMapping(ClientCreateDto::getLastName, Client::setLastName);

        Client user = modelMapper.map(clientCreateDto, Client.class);
        user.setRole(roleRepository.findRoleByName("ROLE_PENDING").get());
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return user;
    }

    public ClientDto clientToClientDto(Client client){
        ModelMapper modelMapper = new ModelMapper();
        TypeMap<Client, ClientDto> propertyMapper = modelMapper.createTypeMap(Client.class, ClientDto.class);
        propertyMapper.addMapping(Client::getId, ClientDto::setId);
        propertyMapper.addMapping(Client::getUsername, ClientDto::setUsername);
        propertyMapper.addMapping(Client::getEmail, ClientDto::setEmail);
        propertyMapper.addMapping(Client::getDateBirth, ClientDto::setDateBirth);
        propertyMapper.addMapping(Client::getName, ClientDto::setName);
        propertyMapper.addMapping(Client::getLastName, ClientDto::setLastName);
        propertyMapper.addMapping(Client::getMembershipCardId, ClientDto::setMembershipCardId);
        propertyMapper.addMapping(Client::getScheduledTrainingCount, ClientDto::setScheduledTrainingCount);

        return modelMapper.map(client, ClientDto.class);
    }

    public ClientAllDto clientToClientAllDto(Client client) {
        ClientAllDto clientAllDto = new ClientAllDto();
        clientAllDto.setId(client.getId());
        clientAllDto.setEmail(client.getEmail());
        clientAllDto.setDateBirth(client.getDateBirth());
        clientAllDto.setName(client.getName());
        clientAllDto.setLastName(client.getLastName());
        clientAllDto.setUsername(client.getUsername());
        clientAllDto.setRole(client.getRole());
        clientAllDto.setMembershipCardId(client.getMembershipCardId());
        clientAllDto.setScheduledTrainingCount(client.getScheduledTrainingCount());

        return  clientAllDto;
    }
}
