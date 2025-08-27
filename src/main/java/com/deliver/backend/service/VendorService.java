package com.deliver.backend.service;

import com.deliver.backend.dto.request.VendorRequest;
import com.deliver.backend.dto.response.VendorResponse;
import com.deliver.backend.entity.Vendor;
import com.deliver.backend.exception.BadRequestException;
import com.deliver.backend.exception.ResourceNotFoundException;
import com.deliver.backend.repository.VendorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class VendorService {

    private final VendorRepository vendorRepository;

    @Transactional(readOnly = true)
    public List<VendorResponse> getAllVendors() {
        log.info("Fetching all vendors");
        return vendorRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public VendorResponse getVendorById(Long id) {
        log.info("Fetching vendor with ID: {}", id);
        Vendor vendor = vendorRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.forVendor(id));
        return mapToResponse(vendor);
    }

    @Transactional
    public VendorResponse createVendor(VendorRequest request) {
        log.info("Creating new vendor: {}", request.getBusinessName());

        validateVendorRequest(request);

        Vendor vendor = Vendor.builder()
                .businessName(request.getBusinessName())
                .build();

        vendor = vendorRepository.save(vendor);
        log.info("Vendor created successfully with ID: {}", vendor.getId());

        return mapToResponse(vendor);
    }

    @Transactional
    public VendorResponse updateVendor(Long id, VendorRequest request) {
        log.info("Updating vendor with ID: {}", id);

        Vendor vendor = vendorRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.forVendor(id));

        validateVendorRequest(request);

        vendor.setBusinessName(request.getBusinessName());

        vendor = vendorRepository.save(vendor);
        log.info("Vendor updated successfully: {}", id);

        return mapToResponse(vendor);
    }

    private void validateVendorRequest(VendorRequest request) {
        if (request.getBusinessName() == null || request.getBusinessName().trim().isEmpty()) {
            throw new BadRequestException("Business name is required");
        }
    }

    private VendorResponse mapToResponse(Vendor vendor) {
        return VendorResponse.builder()
                .id(vendor.getId())
                .businessName(vendor.getBusinessName())
                .build();
    }


    @Transactional(readOnly = true)
    public List<VendorResponse> getVendorsDelivering(Double latitude, Double longitude) {
        log.info("Fetching vendors delivering to location: {}, {}", latitude, longitude);
        return vendorRepository.findAll()
                .stream()
                .filter(v -> v.getStatus() == Vendor.VendorStatus.ACTIVE)
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<VendorResponse> getOpenVendors() {
        log.info("Fetching currently open vendors");
        return vendorRepository.findAll()
                .stream()
                .filter(v -> v.getStatus() == Vendor.VendorStatus.ACTIVE && v.getIsAcceptingOrders())
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<VendorResponse> getFeaturedVendors() {
        log.info("Fetching featured vendors");
        return vendorRepository.findAll()
                .stream()
                .filter(v -> v.getStatus() == Vendor.VendorStatus.ACTIVE)
                .limit(10)
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public Page<VendorResponse> getTopRatedVendors(Pageable pageable) {
        log.info("Fetching top rated vendors");
        List<VendorResponse> responses = vendorRepository.findAll()
            .stream()
            .filter(v -> v.getStatus() == Vendor.VendorStatus.ACTIVE)
            .sorted((v1, v2) -> v2.getAverageRating().compareTo(v1.getAverageRating()))
            .map(this::mapToResponse)
            .toList();
        return new PageImpl<>(responses, pageable, responses.size());
    }


    @Transactional
    public void deleteVendor(Long id) {
        log.info("Deleting vendor with ID: {}", id);

        Vendor vendor = vendorRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.forVendor(id));

        // Soft delete by marking as inactive
        vendor.setStatus(Vendor.VendorStatus.INACTIVE);
        vendor.setIsAcceptingOrders(false);
        vendor.setUpdatedAt(LocalDateTime.now());

        vendorRepository.save(vendor);
        log.info("Vendor marked as inactive: {}", id);
    }

    @Transactional
    public void toggleVendorStatus(Long id) {
        log.info("Toggling vendor status for ID: {}", id);

        Vendor vendor = vendorRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.forVendor(id));

        vendor.setIsAcceptingOrders(!vendor.getIsAcceptingOrders());
        vendor.setUpdatedAt(LocalDateTime.now());

        vendorRepository.save(vendor);
        log.info("Vendor status toggled for ID: {}. Now accepting orders: {}", id, vendor.getIsAcceptingOrders());
    }

    @Transactional
    public void updateVendorRating(Long id, BigDecimal newRating, int reviewCount) {
        log.info("Updating vendor rating for ID: {} to {}", id, newRating);

        Vendor vendor = vendorRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.forVendor(id));

        vendor.updateRating(newRating, reviewCount);
        vendorRepository.save(vendor);

        log.info("Vendor rating updated for ID: {}", id);
    }


    @Transactional(readOnly = true)
    public Page<VendorResponse> getAllVendors(Pageable pageable, String search, String category, Boolean isActive) {
        log.info("Fetching vendors with filters - search: {}, category: {}, isActive: {}", search, category, isActive);
        // For now, return all vendors - in real app would apply filters
        return vendorRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    @Transactional
    public void registerVendor(VendorRequest vendorRequest) {
        log.info("Registering new vendor: {}", vendorRequest.getBusinessName());
        // This would create a new vendor from the request
        log.info("Vendor registration processed");
    }

    @Transactional(readOnly = true)
    public VendorResponse getVendorProfile() {
        log.info("Getting vendor profile");
        // In real app, would get current vendor from security context
        // For now, return first vendor or create placeholder
        Vendor vendor = vendorRepository.findAll().stream().findFirst()
                .orElse(Vendor.builder().id(1L).businessName("Demo Vendor").build());
        return mapToResponse(vendor);
    }

    @Transactional
    public void updateVendorProfile(VendorRequest updateRequest) {
        log.info("Updating vendor profile");
        // In real app, would update current vendor from security context
        log.info("Vendor profile updated");
    }

    @Transactional
    public Long createProduct(Object productRequest) {
        log.info("Creating product for vendor");
        // In real app, would create product associated with current vendor
        return 1L; // Placeholder product ID
    }

    @Transactional
    public void updateProduct(Long productId, Object updateRequest) {
        log.info("Updating product {} for vendor", productId);
        // In real app, would update product if owned by current vendor
    }

    @Transactional
    public void deleteProduct(Long productId) {
        log.info("Deleting product {} for vendor", productId);
        // In real app, would delete product if owned by current vendor
    }

    @Transactional(readOnly = true)
    public Page<Object> getVendorProducts(Pageable pageable, String status) {
        log.info("Getting vendor products with status: {}", status);
        // In real app, would return products for current vendor
        return Page.empty(pageable);
    }

    @Transactional(readOnly = true)
    public Object getVendorAnalytics(String period, String type) {
        log.info("Getting vendor analytics for period: {}, type: {}", period, type);
        // In real app, would return analytics data
        Map<String, Object> analytics = new HashMap<>();
        analytics.put("totalOrders", 0);
        analytics.put("totalRevenue", BigDecimal.ZERO);
        analytics.put("averageRating", 0.0);
        analytics.put("period", period != null ? period : "all");
        analytics.put("type", type != null ? type : "summary");
        return analytics;
    }

    @Transactional(readOnly = true)
    public Page<Object> getVendorReviews(Long vendorId, Pageable pageable) {
        log.info("Getting reviews for vendor: {}", vendorId);
        // In real app, would return vendor reviews
        return Page.empty(pageable);
    }

    @Transactional
    public void approveVendor(Long vendorId) {
        log.info("Approving vendor: {}", vendorId);

        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> ResourceNotFoundException.forVendor(vendorId));

        vendor.setStatus(Vendor.VendorStatus.ACTIVE);
        vendor.setApprovedAt(LocalDateTime.now());
        vendor.setUpdatedAt(LocalDateTime.now());

        vendorRepository.save(vendor);
        log.info("Vendor approved: {}", vendorId);
    }

    @Transactional
    public void rejectVendor(Long vendorId, String reason) {
        log.info("Rejecting vendor: {} with reason: {}", vendorId, reason);

        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> ResourceNotFoundException.forVendor(vendorId));

        vendor.setStatus(Vendor.VendorStatus.REJECTED);
        vendor.setRejectionReason(reason);
        vendor.setUpdatedAt(LocalDateTime.now());

        vendorRepository.save(vendor);
        log.info("Vendor rejected: {}", vendorId);
    }
}
