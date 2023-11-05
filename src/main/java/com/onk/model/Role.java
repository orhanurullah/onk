package com.onk.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.onk.core.utils.DbConstants;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = DbConstants.roleTableName)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role extends BaseModel {


    @NotBlank
    @Column(name = DbConstants.roleTableColumnName, unique = true, length = DbConstants.textTallSize)
    private String name;

    @Column(name = DbConstants.roleTableColumnDescription, length = DbConstants.textGrandeSize)
    private String description;

    @JsonIgnore
    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

}
