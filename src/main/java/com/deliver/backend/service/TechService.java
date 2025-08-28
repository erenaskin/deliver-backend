package com.deliver.backend.service;

import com.deliver.backend.dto.response.TechProductResponse;
import com.deliver.backend.dto.response.TechStoreResponse;
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
public class TechService {

    private final ProductRepository productRepository;
    private final VendorRepository vendorRepository;
    private final Random random = new Random();

    @Transactional(readOnly = true)
    public List<TechProductResponse> getPopularProducts() {
        log.info("Fetching popular tech products");

        // Get popular tech products using popularity score
        List<Product> popularTechProducts = productRepository.findTop10ByServiceTypeOrderByPopularityScoreDesc(ServiceType.TECH.name());

        return popularTechProducts.stream()
                .limit(10)
                .map(this::mapToTechProductResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<TechStoreResponse> getStores() {
        log.info("Fetching tech stores");

        // Get active tech vendors using service_type
        List<Vendor> activeTechVendors = vendorRepository.findByServiceTypeAndStatus(ServiceType.TECH.name(), Vendor.VendorStatus.ACTIVE);

        return activeTechVendors.stream()
                .limit(20)
                .map(this::mapToTechStoreResponse)
                .toList();
    }

    private TechProductResponse mapToTechProductResponse(Product product) {
        return TechProductResponse.builder()
                .name(product.getName())
                .category(product.getCategory() != null ? product.getCategory() : "Teknoloji")
                .price(product.getPrice())
                .emoji(getProductEmoji(product.getCategory()))
                .build();
    }

    private TechStoreResponse mapToTechStoreResponse(Vendor vendor) {
        return TechStoreResponse.builder()
                .name(vendor.getBusinessName())
                .description(vendor.getDescription() != null ? vendor.getDescription() : "Teknoloji ürünleri mağazası")
                .rating(vendor.getAverageRating())
                .deliveryTime(generateDeliveryTime())
                .deliveryFee(vendor.getDeliveryFee() != null ? vendor.getDeliveryFee() : BigDecimal.valueOf(5.99))
                .image("🏪")
                .hasInstallment(random.nextBoolean())
                .hasWarranty(random.nextBoolean())
                .build();
    }

    private String getProductEmoji(String category) {
        if (category == null) return "📱";

        String lowerCategory = category.toLowerCase();

        if (lowerCategory.contains("telefon") || lowerCategory.contains("mobile")) {
            return "📱";
        } else if (lowerCategory.contains("bilgisayar") || lowerCategory.contains("laptop")) {
            return "💻";
        } else if (lowerCategory.contains("tablet")) {
            return "📱";
        } else if (lowerCategory.contains("oyun")) {
            return "🎮";
        } else if (lowerCategory.contains("ses") || lowerCategory.contains("kulaklık")) {
            return "🎧";
        } else if (lowerCategory.contains("şarj") || lowerCategory.contains("power")) {
            return "🔋";
        } else if (lowerCategory.contains("aksesuar")) {
            return "🔌";
        } else {
            return "📱";
        }
    }

    private String generateDeliveryTime() {
        int min = 15 + random.nextInt(30); // 15-45 dakika arası
        int max = min + 15 + random.nextInt(30); // min'den 15-45 dakika sonrası
        return min + "-" + max + " dk";
    }
}