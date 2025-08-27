package com.deliver.backend.service;

import com.deliver.backend.entity.PetType;
import com.deliver.backend.repository.PetTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PetTypeService {
    
    private final PetTypeRepository petTypeRepository;
    
    public List<PetType> getPetTypesByService(Long serviceId) {
        return petTypeRepository.findByServiceIdAndIsActiveTrue(serviceId);
    }
    
    public List<PetType> getPetTypesByServiceOrdered(Long serviceId) {
        return petTypeRepository.findActivePetTypesByServiceOrdered(serviceId);
    }
    
    public PetType getPetTypeById(Long id) {
        return petTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pet type not found with id: " + id));
    }
    
    public PetType getPetTypeByNameAndService(String name, Long serviceId) {
        return petTypeRepository.findByNameAndServiceIdAndIsActiveTrue(name, serviceId);
    }
}
