package com.onk.model;

import com.onk.core.utils.DbConstants;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = DbConstants.addressTableName)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address extends BaseModel {

    @NotBlank
    @Column(name = DbConstants.addressCountryColumnName,
            length = DbConstants.textGrandeSize)
    private String country;

    @NotBlank
    @Column(name = DbConstants.addressCityColumnName, length = DbConstants.textGrandeSize)
    private String city;

    @NotBlank
    @Column(name = DbConstants.addressCountyColumnName, length = DbConstants.textGrandeSize)
    private String county;

    @NotBlank
    @Column(name = DbConstants.addressNeighbourhoodColumnName, length = DbConstants.textGrandeSize)
    private String neighbourhood;

    @NotBlank
    @Column(name = DbConstants.addressStreetColumnName, length = DbConstants.textGrandeSize)
    private String street;

    @NotBlank
    @Column(name = DbConstants.addressDetailColumnName, length = DbConstants.textGrandeSize)
    private String detail;

    @Column(name = DbConstants.addressZipCodeColumnName, length = DbConstants.textGrandeSize)
    private String zipCode;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = DbConstants.addressUserColumnName)
    private User user;

}

