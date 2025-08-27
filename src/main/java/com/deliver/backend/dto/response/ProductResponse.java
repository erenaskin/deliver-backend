package com.deliver.backend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Product response payload")
public class ProductResponse {

    @Schema(description = "Product ID", example = "1")
    private Long id;

    @Schema(description = "Product name", example = "Delicious Pizza")
    private String name;

    @Schema(description = "Product description", example = "Fresh Italian pizza with mozzarella and tomato sauce")
    private String description;

    @Schema(description = "Product price", example = "15.99")
    private BigDecimal price;

    @Schema(description = "Original price (before discount)", example = "18.99")
    private BigDecimal originalPrice;

    @Schema(description = "Discount percentage", example = "15.5")
    private BigDecimal discountPercentage;

    @Schema(description = "Product category", example = "Pizza")
    private String category;

    @Schema(description = "Product subcategory", example = "Italian")
    private String subcategory;

    @Schema(description = "Product images")
    private List<String> imageUrls;

    @Schema(description = "Main product image", example = "https://example.com/pizza.jpg")
    private String mainImageUrl;

    @Schema(description = "Available quantity", example = "50")
    private Integer quantity;

    @Schema(description = "Minimum order quantity", example = "1")
    private Integer minOrderQuantity;

    @Schema(description = "Maximum order quantity", example = "10")
    private Integer maxOrderQuantity;

    @Schema(description = "Product availability status", example = "true")
    private Boolean isAvailable;

    @Schema(description = "Product featured status", example = "false")
    private Boolean isFeatured;

    @Schema(description = "Average rating", example = "4.5")
    private Double averageRating;

    @Schema(description = "Total number of reviews", example = "123")
    private Integer reviewCount;

    @Schema(description = "Estimated preparation time in minutes", example = "30")
    private Integer preparationTimeMinutes;

    @Schema(description = "Product tags", example = "[\"spicy\", \"vegetarian\", \"popular\"]")
    private List<String> tags;

    @Schema(description = "Nutritional information")
    private NutritionalInfo nutritionalInfo;

    @Schema(description = "Vendor information")
    private VendorInfo vendor;

    @Schema(description = "Product creation time")
    private LocalDateTime createdAt;

    @Schema(description = "Product last update time")
    private LocalDateTime updatedAt;

    @Schema(description = "User favorite status", example = "false")
    private Boolean isFavorite;
    
    // Mobile app specific fields
    @Schema(description = "Product volume", example = "19L Damacana")
    private String volume;
    
    @Schema(description = "Delivery speed text", example = "Hızlı teslimat")
    private String deliverySpeedText;
    
    @Schema(description = "Whether delivery is available today", example = "true")
    private Boolean isAvailableToday;
    
    @Schema(description = "Whether product is popular", example = "false")
    private Boolean isPopular;
    
    @Schema(description = "Whether product is new arrival", example = "false")
    private Boolean isNewArrival;
    
    @Schema(description = "Whether product is on promotion", example = "false")
    private Boolean isPromotion;
    
    @Schema(description = "Whether product has limited stock", example = "false")
    private Boolean isLimitedStock;
    
    @Schema(description = "Product brand", example = "Erikli")
    private String brand;
    
    @Schema(description = "Delivery fee", example = "12.99")
    private BigDecimal deliveryFee;
    
    @Schema(description = "Delivery time in minutes", example = "30")
    private Integer deliveryTimeMinutes;
    
    @Schema(description = "Pet type for pet products")
    private PetTypeInfo petType;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Nutritional information")
    public static class NutritionalInfo {

        @Schema(description = "Calories per serving", example = "320")
        private Integer calories;

        @Schema(description = "Protein in grams", example = "15.5")
        private BigDecimal protein;

        @Schema(description = "Carbohydrates in grams", example = "35.0")
        private BigDecimal carbohydrates;

        @Schema(description = "Fat in grams", example = "12.5")
        private BigDecimal fat;

        @Schema(description = "Fiber in grams", example = "3.2")
        private BigDecimal fiber;

        @Schema(description = "Sugar in grams", example = "5.5")
        private BigDecimal sugar;

        @Schema(description = "Sodium in milligrams", example = "850")
        private Integer sodium;

        @Schema(description = "Allergen information", example = "[\"gluten\", \"dairy\"]")
        private List<String> allergens;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Vendor information")
    public static class VendorInfo {

        @Schema(description = "Vendor ID", example = "1")
        private Long id;

        @Schema(description = "Vendor name", example = "Mario's Pizza")
        private String name;

        @Schema(description = "Vendor logo URL", example = "https://example.com/logo.jpg")
        private String logoUrl;

        @Schema(description = "Vendor rating", example = "4.2")
        private Double rating;

        @Schema(description = "Vendor review count", example = "456")
        private Integer reviewCount;

        @Schema(description = "Estimated delivery time in minutes", example = "25")
        private Integer estimatedDeliveryTime;

        @Schema(description = "Delivery fee", example = "2.99")
        private BigDecimal deliveryFee;

        @Schema(description = "Minimum order amount", example = "15.00")
        private BigDecimal minimumOrderAmount;

        @Schema(description = "Vendor availability status", example = "true")
        private Boolean isOpen;

        @Schema(description = "Distance from user in kilometers", example = "2.5")
        private Double distanceKm;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Pet type information")
    public static class PetTypeInfo {

        @Schema(description = "Pet type ID", example = "1")
        private Long id;

        @Schema(description = "Pet type name", example = "Kedi")
        private String name;

        @Schema(description = "Pet type emoji", example = "🐱")
        private String emoji;

        @Schema(description = "Pet type icon", example = "cat")
        private String iconName;
    }
}
