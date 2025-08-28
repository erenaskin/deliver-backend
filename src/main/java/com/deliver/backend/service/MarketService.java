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
public class MarketService {

    private final ProductRepository productRepository;
    private final VendorRepository vendorRepository;
    private final Random random = new Random();

    @Transactional(readOnly = true)
    public List<FoodProductResponse> getPopularProducts() {
        log.info("Fetching popular market products");

        List<Product> featuredMarketProducts = productRepository.findByServiceTypeAndIsFeaturedTrue(ServiceType.MARKET.name());

        return featuredMarketProducts.stream()
                .limit(10)
                .map(this::mapToFoodProductResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<FoodStoreResponse> getStores() {
        log.info("Fetching market stores");

        List<Vendor> activeMarketVendors = vendorRepository.findByServiceTypeAndStatus(ServiceType.MARKET.name(), Vendor.VendorStatus.ACTIVE);

        return activeMarketVendors.stream()
                .limit(20)
                .map(this::mapToFoodStoreResponse)
                .toList();
    }

    private FoodProductResponse mapToFoodProductResponse(Product product) {
        return FoodProductResponse.builder()
                .name(product.getName())
                .category(product.getCategory() != null ? product.getCategory() : "Market")
                .price(product.getPrice())
                .emoji(getProductEmoji(product.getCategory()))
                .build();
    }

    private FoodStoreResponse mapToFoodStoreResponse(Vendor vendor) {
        return FoodStoreResponse.builder()
                .name(vendor.getBusinessName())
                .description(vendor.getDescription() != null ? vendor.getDescription() : "Günlük ihtiyaçlar ve market ürünleri")
                .rating(vendor.getAverageRating())
                .deliveryTime(generateDeliveryTime())
                .deliveryFee(vendor.getDeliveryFee() != null ? vendor.getDeliveryFee() : BigDecimal.valueOf(3.99))
                .image(getStoreEmoji(vendor.getCategory()))
                .hasDiscount(random.nextBoolean())
                .cuisineType(vendor.getCategory())
                .build();
    }

    private String getProductEmoji(String category) {
        if (category == null) return "🛒";

        String lowerCategory = category.toLowerCase();

        if (lowerCategory.contains("meyve") || lowerCategory.contains("sebze")) {
            return "🥕";
        } else if (lowerCategory.contains("süt") || lowerCategory.contains("süt ürünleri")) {
            return "🥛";
        } else if (lowerCategory.contains("et") || lowerCategory.contains("meat")) {
            return "🥩";
        } else if (lowerCategory.contains("ekmek") || lowerCategory.contains("bread")) {
            return "🍞";
        } else if (lowerCategory.contains("içecek")) {
            return "🥤";
        } else if (lowerCategory.contains("temizlik")) {
            return "🧽";
        } else {
            return "🛒";
        }
    }

    private String getStoreEmoji(String category) {
        if (category == null) return "🏪";

        String lowerCategory = category.toLowerCase();

        if (lowerCategory.contains("market") || lowerCategory.contains("süpermarket")) {
            return "🏪";
        } else if (lowerCategory.contains("fırın")) {
            return "🍞";
        } else if (lowerCategory.contains("manav")) {
            return "🥕";
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