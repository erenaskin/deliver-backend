package com.deliver.backend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Product creation/update request payload")
public class ProductRequest {

    @NotBlank(message = "Product name is required")
    @Size(min = 2, max = 100, message = "Product name must be between 2 and 100 characters")
    @Schema(description = "Product name", example = "Margherita Pizza")
    private String name;

    @NotBlank(message = "Product description is required")
    @Size(min = 10, max = 1000, message = "Product description must be between 10 and 1000 characters")
    @Schema(description = "Product description", example = "Classic Italian pizza with fresh tomato sauce, mozzarella, and basil")
    private String description;

    @NotNull(message = "Product price is required")
    @DecimalMin(value = "0.01", message = "Product price must be greater than 0")
    @Digits(integer = 8, fraction = 2, message = "Price must have at most 8 integer digits and 2 decimal places")
    @Schema(description = "Product price", example = "12.99")
    private BigDecimal price;

    @Schema(description = "Original price (before discount)", example = "15.99")
    private BigDecimal originalPrice;

    @NotBlank(message = "Product category is required")
    @Schema(description = "Product category", example = "Pizza")
    private String category;

    @Schema(description = "Product subcategory", example = "Classic")
    private String subcategory;

    @Schema(description = "Product image URLs")
    private List<String> imageUrls;

    @Schema(description = "Main product image URL", example = "https://example.com/pizza.jpg")
    private String mainImageUrl;

    @NotNull(message = "Available quantity is required")
    @Min(value = 0, message = "Quantity cannot be negative")
    @Schema(description = "Available quantity", example = "50")
    private Integer quantity;

    @Min(value = 1, message = "Minimum order quantity must be at least 1")
    @Schema(description = "Minimum order quantity", example = "1")
    @Builder.Default
    private Integer minOrderQuantity = 1;

    @Schema(description = "Maximum order quantity", example = "10")
    private Integer maxOrderQuantity;

    @Schema(description = "Product availability status", example = "true")
    @Builder.Default
    private Boolean isAvailable = true;

    @Schema(description = "Product featured status", example = "false")
    @Builder.Default
    private Boolean isFeatured = false;

    @Min(value = 1, message = "Preparation time must be at least 1 minute")
    @Max(value = 300, message = "Preparation time must not exceed 300 minutes")
    @Schema(description = "Estimated preparation time in minutes", example = "15")
    private Integer preparationTimeMinutes;

    @Schema(description = "Product tags", example = "[\"vegetarian\", \"popular\", \"spicy\"]")
    private List<String> tags;

    @Valid
    @Schema(description = "Nutritional information")
    private NutritionalInfoRequest nutritionalInfo;

    @Schema(description = "Product variants (sizes, options, etc.)")
    private List<ProductVariantRequest> variants;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Nutritional information request")
    public static class NutritionalInfoRequest {

        @Min(value = 0, message = "Calories cannot be negative")
        @Schema(description = "Calories per serving", example = "320")
        private Integer calories;

        @DecimalMin(value = "0.0", message = "Protein cannot be negative")
        @Schema(description = "Protein in grams", example = "15.5")
        private BigDecimal protein;

        @DecimalMin(value = "0.0", message = "Carbohydrates cannot be negative")
        @Schema(description = "Carbohydrates in grams", example = "35.0")
        private BigDecimal carbohydrates;

        @DecimalMin(value = "0.0", message = "Fat cannot be negative")
        @Schema(description = "Fat in grams", example = "12.5")
        private BigDecimal fat;

        @DecimalMin(value = "0.0", message = "Fiber cannot be negative")
        @Schema(description = "Fiber in grams", example = "3.2")
        private BigDecimal fiber;

        @DecimalMin(value = "0.0", message = "Sugar cannot be negative")
        @Schema(description = "Sugar in grams", example = "5.5")
        private BigDecimal sugar;

        @Min(value = 0, message = "Sodium cannot be negative")
        @Schema(description = "Sodium in milligrams", example = "850")
        private Integer sodium;

        @Schema(description = "Allergen information", example = "[\"gluten\", \"dairy\"]")
        private List<String> allergens;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Product variant request")
    public static class ProductVariantRequest {

        @NotBlank(message = "Variant name is required")
        @Schema(description = "Variant name", example = "Size")
        private String name;

        @NotBlank(message = "Variant value is required")
        @Schema(description = "Variant value", example = "Large")
        private String value;

        @Schema(description = "Additional price for this variant", example = "3.00")
        private BigDecimal additionalPrice;

        @Schema(description = "Variant availability", example = "true")
        @Builder.Default
        private Boolean isAvailable = true;
    }
}
