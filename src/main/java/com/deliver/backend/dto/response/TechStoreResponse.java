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
@Schema(description = "Tech store response payload")
public class TechStoreResponse {

    @Schema(description = "Store name", example = "TechStore Pro")
    private String name;

    @Schema(description = "Store description", example = "En yeni teknoloji ürünleri")
    private String description;

    @Schema(description = "Store rating", example = "4.5")
    private BigDecimal rating;

    @Schema(description = "Delivery time", example = "30-45 dk")
    private String deliveryTime;

    @Schema(description = "Delivery fee", example = "5.99")
    private BigDecimal deliveryFee;

    @Schema(description = "Store image/emoji", example = "🏪")
    private String image;

    @Schema(description = "Has installment option", example = "true")
    private Boolean hasInstallment;

    @Schema(description = "Has warranty", example = "true")
    private Boolean hasWarranty;
}