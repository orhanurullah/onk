package com.onk.model;

import com.onk.core.utils.DbConstants;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = DbConstants.productTableName)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product extends BaseModel {

    @NotBlank
    @Column(name = DbConstants.productNameColumnName , unique = true)
    @Size(min = 2, max = 255)
    private String name;

    @Column(name = DbConstants.productTitleColumnName)
    @Size(min = 5, max=100)
    private String title;

    @NotBlank
    @Column(name = DbConstants.productDescriptionColumnName)
    @Size(min = 2)
    private String description;

    @NotNull
    @Column(name = DbConstants.productQuantityColumnName)
    private Integer quantity;

    @NotNull
    @Column(name = DbConstants.productPriceColumnName)
    private BigDecimal price;

    @Column(name = DbConstants.productPurchasePriceColumnName)
    private BigDecimal purchasePrice;

    @Enumerated(EnumType.STRING)
    @Column(name = DbConstants.productCurrencyColumnName)
    private Currency currency;

    @ManyToOne
    @JoinColumn(name = DbConstants.productCategoryColumnName)
    private Category category;

    @Column(name = DbConstants.productIsPublishedColumnName)
    private Boolean isPublished;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = DbConstants.productImagesTableName,
            joinColumns = @JoinColumn(name = DbConstants.productImagesProductColumnName),
            inverseJoinColumns = @JoinColumn(name = DbConstants.productImagesImageColumnName))
    private Set<Image> images = new HashSet<>();


}
