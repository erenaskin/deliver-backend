package com.deliver.backend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

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

    @NotBlank(message = "Product category is required")
    @Schema(description = "Product category", example = "Pizza")
    private String category;

    @NotNull(message = "Available quantity is required")
    @Min(value = 0, message = "Quantity cannot be negative")
    @Schema(description = "Available quantity", example = "50")
    private Integer quantity;

    @Schema(description = "Product availability status", example = "true")
    @Builder.Default
    private Boolean isAvailable = true;
}
