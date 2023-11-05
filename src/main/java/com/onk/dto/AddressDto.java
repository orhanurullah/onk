package com.onk.dto;

import com.onk.core.utils.ValidationMessageConstant;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressDto {

    private Long id;

    @NotBlank(message = ValidationMessageConstant.notBlankMessage)
    @Size(min = 2, max = 50)
    private String country;

    @NotBlank(message = ValidationMessageConstant.notBlankMessage)
    @Size(min = 2, max = 50)
    private String city;

    @NotBlank(message = ValidationMessageConstant.notBlankMessage)
    @Size(min = 2, max = 50)
    private String county;

    @NotBlank(message = ValidationMessageConstant.notBlankMessage)
    @Size(min = 2, max = 50)
    private String neighbourhood;

    @NotBlank(message = ValidationMessageConstant.notBlankMessage)
    @Size(min = 2, max = 50)
    private String street;

    @NotBlank(message = ValidationMessageConstant.notBlankMessage)
    @Size(min = 2, max = 120)
    private String detail;

    @Size(min = 2, max = 10)
    private String zipCode;

}
