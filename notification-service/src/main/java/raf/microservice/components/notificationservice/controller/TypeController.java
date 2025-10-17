package raf.microservice.components.notificationservice.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import raf.microservice.components.notificationservice.dto.TypeDto;
import raf.microservice.components.notificationservice.service.TypeService;

@RestController
@RequestMapping("/type")
public class TypeController {

    private final TypeService typeService;

    public TypeController(TypeService typeService){
        this.typeService = typeService;
    }

    @GetMapping("/all")
    public ResponseEntity<Page<TypeDto>> getAllTypes(Pageable pageable){
        return new ResponseEntity<>(typeService.findAll(pageable), HttpStatus.OK);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<TypeDto> editType(@PathVariable("id") Long id, @RequestBody @Valid TypeDto typeDto){
        return new ResponseEntity<>(typeService.editType(id, typeDto), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteType(@PathVariable("id") Long id){
        typeService.deleteType(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/add")
    public ResponseEntity<TypeDto> addType(@RequestBody @Valid TypeDto typeDto){
        return new ResponseEntity<>(typeService.add(typeDto), HttpStatus.CREATED);
    }
}
