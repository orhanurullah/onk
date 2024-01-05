package com.onk.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.onk.core.utils.DbConstants;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Entity
@Table(name = DbConstants.imageTableName)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Image extends BaseModel{


    @NotBlank
    @Column(name = DbConstants.imagePathName,
            unique = true)
    private String path;

    @JsonIgnore
    @ManyToMany(mappedBy = DbConstants.imageTableName)
    private List<Product> products;


}
