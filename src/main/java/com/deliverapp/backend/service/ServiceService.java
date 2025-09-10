package com.deliverapp.backend.service;

import com.deliverapp.backend.model.Service;
import com.deliverapp.backend.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceService {
    @Autowired
    private ServiceRepository serviceRepository;

    public List<Service> getAllServices() {
        return serviceRepository.findAll();
    }

    public Optional<Service> getServiceById(Long id) {
        return serviceRepository.findById(id);
    }

    public Service createService(Service service) {
        return serviceRepository.save(service);
    }

    public Service updateService(Long id, Service updatedService) {
        return serviceRepository.findById(id)
                .map(service -> {
                    service.setName(updatedService.getName());
                    service.setDescription(updatedService.getDescription());
                    service.setPrice(updatedService.getPrice());
                    service.setUpdatedAt(updatedService.getUpdatedAt());
                    return serviceRepository.save(service);
                })
                .orElse(null);
    }

    public void deleteService(Long id) {
        serviceRepository.deleteById(id);
    }
}
