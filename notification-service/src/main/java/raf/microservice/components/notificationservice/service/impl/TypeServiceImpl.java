package raf.microservice.components.notificationservice.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raf.microservice.components.notificationservice.dto.TypeDto;
import raf.microservice.components.notificationservice.exceptions.NotFoundException;
import raf.microservice.components.notificationservice.mapper.TypeMapper;
import raf.microservice.components.notificationservice.mapper.model.Type;
import raf.microservice.components.notificationservice.repository.TypeRepository;
import raf.microservice.components.notificationservice.service.TypeService;

import java.util.Optional;

@Service
@Transactional
public class TypeServiceImpl implements TypeService {

    private final TypeRepository typeRepository;
    private final TypeMapper typeMapper;

    public TypeServiceImpl(TypeRepository typeRepository, TypeMapper typeMapper){
        this.typeRepository = typeRepository;
        this.typeMapper = typeMapper;
    }


    @Override
    public Page<TypeDto> findAll(Pageable pageable) {
        return typeRepository.findAll(pageable)
                .map(typeMapper::typeToTypeDto);
    }

    @Override
    public TypeDto editType(Long id, TypeDto typeDto) {
        Optional<Type> type = typeRepository.findById(id);
        if(type.isEmpty()) throw new NotFoundException("Type not found"); //exception

        Type myType = type.get();
        myType.setFormat(typeDto.getFormat());
        myType.setName(typeDto.getName());
        typeRepository.save(myType);

        return typeDto;
    }

    @Override
    public void deleteType(Long id) {
        typeRepository.deleteById(id);
    }

    @Override
    public TypeDto add(TypeDto typeDto) {
        Type type = new Type();
        type.setName(typeDto.getName());
        type.setFormat(typeDto.getFormat());
        typeRepository.save(type);
        return typeDto;
    }
}
