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

    @Schema(description = "Product category", example = "Pizza")
    private String category;

    @Schema(description = "Available quantity", example = "50")
    private Integer quantity;

    @Schema(description = "Product availability status", example = "true")
    private Boolean isAvailable;
}
