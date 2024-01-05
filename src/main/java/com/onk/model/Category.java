package com.onk.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.onk.core.utils.DbConstants;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Entity
@Table(name = DbConstants.categoryTableName)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category extends BaseModel {

    private static final long serialVersion = 1L;

    @Column(name = DbConstants.categoryNameColumnName,
            unique = true, nullable = false)
    @NotBlank
    @Size(min = 2)
    private String name;

    @Column(name = DbConstants.categoryDescriptionColumnName)
    private String description;

    @ManyToOne
    @JoinColumn(name = DbConstants.categoryParentCategoryColumnName)
    private Category parentCategory;

    @JsonIgnore
    @OneToMany(
            fetch = FetchType.EAGER,
            mappedBy = DbConstants.mappedCategoryProduct
    )
    private List<Product> products;

    @Column(name = DbConstants.categoryIsActiveColumnName)
    private Boolean isActive;

}
