package com.onk.model;

import com.onk.core.utils.DbConstants;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = DbConstants.orderTableName)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order extends BaseModel {

    private static final long serialVersion = 1L;

    @ManyToOne
    @JoinColumn(name = DbConstants.orderUserColumnName)
    private User user;

    @OneToMany
    private List<Product> products;

}


