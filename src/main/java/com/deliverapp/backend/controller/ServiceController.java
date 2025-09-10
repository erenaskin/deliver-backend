package com.deliverapp.backend.controller;

import com.deliverapp.backend.dto.request.ServiceRequest;
import com.deliverapp.backend.dto.response.ServiceResponse;
import com.deliverapp.backend.model.Service;
import com.deliverapp.backend.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/services")
public class ServiceController {
    @Autowired
    private ServiceService serviceService;

    @GetMapping
    public List<ServiceResponse> getAllServices() {
        return serviceService.getAllServices().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceResponse> getServiceById(@PathVariable Long id) {
        return serviceService.getServiceById(id)
                .map(service -> ResponseEntity.ok(toResponse(service)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ServiceResponse createService(@RequestBody ServiceRequest request) {
        Service service = new Service();
        service.setName(request.getName());
        service.setDescription(request.getDescription());
        service.setPrice(request.getPrice());
        service.setCreatedAt(LocalDateTime.now());
        service.setUpdatedAt(LocalDateTime.now());
        return toResponse(serviceService.createService(service));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceResponse> updateService(@PathVariable Long id, @RequestBody ServiceRequest request) {
        Service updated = new Service();
        updated.setName(request.getName());
        updated.setDescription(request.getDescription());
        updated.setPrice(request.getPrice());
        updated.setUpdatedAt(LocalDateTime.now());
        Service result = serviceService.updateService(id, updated);
        if (result == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(toResponse(result));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable Long id) {
        serviceService.deleteService(id);
        return ResponseEntity.noContent().build();
    }

    private ServiceResponse toResponse(Service service) {
        ServiceResponse response = new ServiceResponse();
        response.setId(service.getId());
        response.setName(service.getName());
        response.setDescription(service.getDescription());
        response.setPrice(service.getPrice());
        return response;
    }
}
