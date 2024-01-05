package com.onk.model;

import com.onk.core.utils.DbConstants;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = DbConstants.cartTableName)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cart extends BaseModel {

    private static final long serialVersion = 1L;

    @OneToOne
    @JoinColumn(name = DbConstants.cartUserColumnName, unique = true)
    private User user;

    @ManyToMany
    @JoinTable(
            joinColumns = @JoinColumn(name = DbConstants.cartsProductsCartColumnName),
            inverseJoinColumns = @JoinColumn(name = DbConstants.cartsProductsProductColumnName)
    )
    private List<Product> products;

}

