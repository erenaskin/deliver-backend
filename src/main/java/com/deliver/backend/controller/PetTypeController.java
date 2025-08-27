package com.deliver.backend.controller;

import com.deliver.backend.entity.PetType;
import com.deliver.backend.service.PetTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pet-types")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PetTypeController {
    
    private final PetTypeService petTypeService;
    
    @GetMapping("/service/{serviceId}")
    public ResponseEntity<List<PetType>> getPetTypesByService(@PathVariable Long serviceId) {
        List<PetType> petTypes = petTypeService.getPetTypesByService(serviceId);
        return ResponseEntity.ok(petTypes);
    }
    
    @GetMapping("/service/{serviceId}/ordered")
    public ResponseEntity<List<PetType>> getPetTypesByServiceOrdered(@PathVariable Long serviceId) {
        List<PetType> petTypes = petTypeService.getPetTypesByServiceOrdered(serviceId);
        return ResponseEntity.ok(petTypes);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<PetType> getPetTypeById(@PathVariable Long id) {
        PetType petType = petTypeService.getPetTypeById(id);
        return ResponseEntity.ok(petType);
    }
    
    @GetMapping("/service/{serviceId}/name/{name}")
    public ResponseEntity<PetType> getPetTypeByNameAndService(@PathVariable Long serviceId, @PathVariable String name) {
        PetType petType = petTypeService.getPetTypeByNameAndService(name, serviceId);
        return ResponseEntity.ok(petType);
    }
}
