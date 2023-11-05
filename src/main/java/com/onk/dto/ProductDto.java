package com.onk.dto;

import com.onk.core.utils.ValidationMessageConstant;
import com.onk.model.Currency;
import com.onk.model.Image;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    private Long id;

    @NotBlank(message = ValidationMessageConstant.notBlankMessage)
    @Size(min = 2, max = 255)
    private String name;

    @NotBlank(message = ValidationMessageConstant.notBlankMessage)
    @Size(min = 5, max=100)
    private String title;

    @NotBlank(message = ValidationMessageConstant.notBlankMessage)
    private String description;

    @NotBlank(message = ValidationMessageConstant.notBlankMessage)
    private Integer quantity;

    @NotBlank(message = ValidationMessageConstant.notBlankMessage)
    private BigDecimal price;

    private BigDecimal purchasePrice;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    private String categoryName;

    private Set<Image> productImages;

}
