package com.deliver.backend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Food store response payload")
public class FoodStoreResponse {

    @Schema(description = "Store name", example = "Mario's Pizza")
    private String name;

    @Schema(description = "Store description", example = "Lezzetli pizzalar")
    private String description;

    @Schema(description = "Store rating", example = "4.5")
    private BigDecimal rating;

    @Schema(description = "Delivery time", example = "25-40 dk")
    private String deliveryTime;

    @Schema(description = "Delivery fee", example = "4.99")
    private BigDecimal deliveryFee;

    @Schema(description = "Store image/emoji", example = "🍕")
    private String image;

    @Schema(description = "Has discount", example = "true")
    private Boolean hasDiscount;

    @Schema(description = "Cuisine type", example = "İtalyan")
    private String cuisineType;
}