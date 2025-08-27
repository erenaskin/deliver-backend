package com.deliver.backend.service;

import com.deliver.backend.dto.response.ProductResponse;
import com.deliver.backend.dto.response.VendorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TechService {

    private final ProductService productService;
    private final VendorService vendorService;

    @Transactional(readOnly = true)
    public List<ProductResponse> getPopularProducts() {
        log.info("Fetching popular products");
        // Using featured products as popular products
        return productService.getFeaturedProducts(10);
    }

    @Transactional(readOnly = true)
    public List<VendorResponse> getStores() {
        log.info("Fetching stores");
        // Using featured vendors as stores
        return vendorService.getFeaturedVendors();
    }
}