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
public class PetService {

    private final ProductRepository productRepository;
    private final VendorRepository vendorRepository;
    private final Random random = new Random();

    @Transactional(readOnly = true)
    public List<FoodProductResponse> getPopularProducts() {
        log.info("Fetching popular pet products");

        List<Product> featuredPetProducts = productRepository.findByServiceTypeAndIsFeaturedTrue(ServiceType.PET.name());

        return featuredPetProducts.stream()
                .limit(10)
                .map(this::mapToFoodProductResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<FoodStoreResponse> getStores() {
        log.info("Fetching pet stores");

        List<Vendor> activePetVendors = vendorRepository.findByServiceTypeAndStatus(ServiceType.PET.name(), Vendor.VendorStatus.ACTIVE);

        return activePetVendors.stream()
                .limit(20)
                .map(this::mapToFoodStoreResponse)
                .toList();
    }

    private FoodProductResponse mapToFoodProductResponse(Product product) {
        return FoodProductResponse.builder()
                .name(product.getName())
                .category(product.getCategory() != null ? product.getCategory() : "Pet Ürünleri")
                .price(product.getPrice())
                .emoji(getProductEmoji(product.getCategory()))
                .build();
    }

    private FoodStoreResponse mapToFoodStoreResponse(Vendor vendor) {
        return FoodStoreResponse.builder()
                .name(vendor.getBusinessName())
                .description(vendor.getDescription() != null ? vendor.getDescription() : "Pet ürünleri ve bakım")
                .rating(vendor.getAverageRating())
                .deliveryTime(generateDeliveryTime())
                .deliveryFee(vendor.getDeliveryFee() != null ? vendor.getDeliveryFee() : BigDecimal.valueOf(4.99))
                .image(getStoreEmoji(vendor.getCategory()))
                .hasDiscount(random.nextBoolean())
                .cuisineType(vendor.getCategory())
                .build();
    }

    private String getProductEmoji(String category) {
        if (category == null) return "🐾";

        String lowerCategory = category.toLowerCase();

        if (lowerCategory.contains("kedi") || lowerCategory.contains("cat")) {
            return "🐱";
        } else if (lowerCategory.contains("köpek") || lowerCategory.contains("dog")) {
            return "🐶";
        } else if (lowerCategory.contains("kuş") || lowerCategory.contains("bird")) {
            return "🐦";
        } else if (lowerCategory.contains("balık") || lowerCategory.contains("fish")) {
            return "🐠";
        } else if (lowerCategory.contains("mama")) {
            return "🍖";
        } else if (lowerCategory.contains("aksesuar")) {
            return "🦴";
        } else {
            return "🐾";
        }
    }

    private String getStoreEmoji(String category) {
        if (category == null) return "🏪";

        String lowerCategory = category.toLowerCase();

        if (lowerCategory.contains("kedi")) {
            return "🐱";
        } else if (lowerCategory.contains("köpek")) {
            return "🐶";
        } else if (lowerCategory.contains("pet")) {
            return "🐾";
        } else {
            return "🏪";
        }
    }

    private String generateDeliveryTime() {
        int min = 20 + random.nextInt(25);
        int max = min + 10 + random.nextInt(20);
        return min + "-" + max + " dk";
    }
}