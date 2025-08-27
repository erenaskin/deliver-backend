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
public class FoodService {

    private final ProductRepository productRepository;
    private final VendorRepository vendorRepository;
    private final Random random = new Random();

    @Transactional(readOnly = true)
    public List<FoodProductResponse> getPopularProducts() {
        log.info("Fetching popular food products");

        // Get featured food products using service_type
        List<Product> featuredFoodProducts = productRepository.findByServiceTypeAndIsFeaturedTrue(ServiceType.FOOD.name());

        return featuredFoodProducts.stream()
                .limit(10)
                .map(this::mapToFoodProductResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<FoodStoreResponse> getStores() {
        log.info("Fetching food stores");

        // Get active food vendors using service_type
        List<Vendor> activeFoodVendors = vendorRepository.findByServiceTypeAndStatus(ServiceType.FOOD.name(), Vendor.VendorStatus.ACTIVE);

        return activeFoodVendors.stream()
                .limit(20)
                .map(this::mapToFoodStoreResponse)
                .toList();
    }

    private FoodProductResponse mapToFoodProductResponse(Product product) {
        return FoodProductResponse.builder()
                .name(product.getName())
                .category(product.getCategory() != null ? product.getCategory() : "Yiyecek")
                .price(product.getPrice())
                .emoji(getProductEmoji(product.getCategory()))
                .build();
    }

    private FoodStoreResponse mapToFoodStoreResponse(Vendor vendor) {
        return FoodStoreResponse.builder()
                .name(vendor.getBusinessName())
                .description(vendor.getDescription() != null ? vendor.getDescription() : "Lezzetli yemekler")
                .rating(vendor.getAverageRating())
                .deliveryTime(generateDeliveryTime())
                .deliveryFee(vendor.getDeliveryFee() != null ? vendor.getDeliveryFee() : BigDecimal.valueOf(4.99))
                .image(getStoreEmoji(vendor.getCategory()))
                .hasDiscount(random.nextBoolean())
                .cuisineType(vendor.getCategory())
                .build();
    }

    private String getProductEmoji(String category) {
        if (category == null) return "🍽️";

        String lowerCategory = category.toLowerCase();

        if (lowerCategory.contains("pizza")) {
            return "🍕";
        } else if (lowerCategory.contains("hamburger") || lowerCategory.contains("burger")) {
            return "🍔";
        } else if (lowerCategory.contains("çorba") || lowerCategory.contains("soup")) {
            return "🍜";
        } else if (lowerCategory.contains("salata")) {
            return "🥗";
        } else if (lowerCategory.contains("tatlı") || lowerCategory.contains("dessert")) {
            return "🍰";
        } else if (lowerCategory.contains("kahve") || lowerCategory.contains("coffee")) {
            return "☕";
        } else if (lowerCategory.contains("döner")) {
            return "🥙";
        } else if (lowerCategory.contains("lahmacun")) {
            return "🥙";
        } else if (lowerCategory.contains("kebap")) {
            return "🍖";
        } else {
            return "🍽️";
        }
    }

    private String getStoreEmoji(String category) {
        if (category == null) return "🏪";

        String lowerCategory = category.toLowerCase();

        if (lowerCategory.contains("pizza") || lowerCategory.contains("italyan")) {
            return "🍕";
        } else if (lowerCategory.contains("hamburger") || lowerCategory.contains("fast food")) {
            return "🍔";
        } else if (lowerCategory.contains("çorba") || lowerCategory.contains("soup")) {
            return "🍜";
        } else if (lowerCategory.contains("kahve") || lowerCategory.contains("coffee")) {
            return "☕";
        } else if (lowerCategory.contains("tatlı") || lowerCategory.contains("pastane")) {
            return "🍰";
        } else if (lowerCategory.contains("döner") || lowerCategory.contains("kebap")) {
            return "🥙";
        } else {
            return "🏪";
        }
    }

    private String generateDeliveryTime() {
        int min = 20 + random.nextInt(25); // 20-45 dakika arası
        int max = min + 10 + random.nextInt(20); // min'den 10-30 dakika sonrası
        return min + "-" + max + " dk";
    }
}