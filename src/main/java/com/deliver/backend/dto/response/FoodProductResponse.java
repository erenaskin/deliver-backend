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
@Schema(description = "Food product response payload")
public class FoodProductResponse {

    @Schema(description = "Product name", example = "Margherita Pizza")
    private String name;

    @Schema(description = "Product category", example = "Pizza")
    private String category;

    @Schema(description = "Product price", example = "45.00")
    private BigDecimal price;

    @Schema(description = "Product emoji/icon", example = "🍕")
    private String emoji;
}