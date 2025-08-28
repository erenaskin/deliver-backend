package com.deliver.backend.service;

import com.deliver.backend.dto.response.FoodProductResponse;
import com.deliver.backend.dto.response.FoodStoreResponse;
import com.deliver.backend.entity.Product;
import com.deliver.backend.entity.ServiceType;
import com.deliver.backend.entity.Vendor;
import com.deliver.backend.repository.ProductRepository;
import com.deliver.backend.repository.VendorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class WaterService {

    private final ProductRepository productRepository;
    private final VendorRepository vendorRepository;
    private final Random random = new Random();

    @Transactional(readOnly = true)
    public List<FoodProductResponse> getPopularProducts() {
        log.info("Fetching popular water products");

        List<Product> featuredWaterProducts = productRepository.findByServiceTypeAndIsFeaturedTrue(ServiceType.WATER.name());

        return featuredWaterProducts.stream()
                .limit(10)
                .map(this::mapToFoodProductResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<FoodStoreResponse> getStores() {
        log.info("Fetching water stores");

        List<Vendor> activeWaterVendors = vendorRepository.findByServiceTypeAndStatus(ServiceType.WATER.name(), Vendor.VendorStatus.ACTIVE);

        return activeWaterVendors.stream()
                .limit(20)
                .map(this::mapToFoodStoreResponse)
                .toList();
    }

    private FoodProductResponse mapToFoodProductResponse(Product product) {
        return FoodProductResponse.builder()
                .name(product.getName())
                .category(product.getCategory() != null ? product.getCategory() : "İçecek")
                .price(product.getPrice())
                .emoji(getProductEmoji(product.getCategory()))
                .build();
    }

    private FoodStoreResponse mapToFoodStoreResponse(Vendor vendor) {
        return FoodStoreResponse.builder()
                .name(vendor.getBusinessName())
                .description(vendor.getDescription() != null ? vendor.getDescription() : "Doğal kaynak suları")
            .rating(vendor.getAverageRating())
            .deliveryTime(generateDeliveryTime())
            .deliveryFee(vendor.getDeliveryFee() != null ? vendor.getDeliveryFee() : BigDecimal.valueOf(2.99))
            .image(getStoreEmoji(vendor.getCategory()))
            .hasDiscount(random.nextBoolean())
            .cuisineType(vendor.getCategory())
            .build();
    }

    private String getProductEmoji(String category) {
        if (category == null) return "💧";

        String lowerCategory = category.toLowerCase();

        if (lowerCategory.contains("su") || lowerCategory.contains("water")) {
            return "💧";
        } else if (lowerCategory.contains("maden suyu") || lowerCategory.contains("mineral")) {
            return "🥤";
        } else if (lowerCategory.contains("gazlı") || lowerCategory.contains("sparkling")) {
            return "🥤";
        } else {
            return "💧";
        }
    }

    private String getStoreEmoji(String category) {
        if (category == null) return "🏪";

        String lowerCategory = category.toLowerCase();

        if (lowerCategory.contains("su") || lowerCategory.contains("water")) {
            return "💧";
        } else {
            return "🏪";
        }
    }

    private String generateDeliveryTime() {
        int min = 15 + random.nextInt(20);
        int max = min + 5 + random.nextInt(15);
        return min + "-" + max + " dk";
    }
}