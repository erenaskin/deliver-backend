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
@Schema(description = "Tech product response payload")
public class TechProductResponse {

    @Schema(description = "Product name", example = "iPhone 15 Pro")
    private String name;

    @Schema(description = "Product category", example = "Telefon")
    private String category;

    @Schema(description = "Product price", example = "45000.00")
    private BigDecimal price;

    @Schema(description = "Product emoji/icon", example = "📱")
    private String emoji;
}