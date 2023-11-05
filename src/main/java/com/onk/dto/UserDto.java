package com.onk.dto;

import com.onk.core.utils.ValidationMessageConstant;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private Long id;

    @NotBlank(message = ValidationMessageConstant.notBlankMessage)
    @Size(min=3, max = 100)
    private String name;

    @NotBlank(message = ValidationMessageConstant.notBlankMessage)
    @Size(min=3, max = 100)
    private String lastName;

    @NotBlank(message = ValidationMessageConstant.notBlankMessage)
    @Size(min=3, max = 255)
    @Email
    private String email;

    private List<String> roles;


}
