package com.deliver.backend.service;

import com.deliver.backend.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceService {

    private final ServiceRepository serviceRepository;

    @Transactional(readOnly = true)
    public List<com.deliver.backend.entity.Service> getAllServices() {
        return serviceRepository.findAll();
    }

    @Transactional(readOnly = true)
    public com.deliver.backend.entity.Service getServiceById(Long id) {
        return serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found"));
    }

}
