package com.onk.dto;

import com.onk.core.utils.ValidationMessageConstant;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageDto {

    private Long id;

    @NotBlank(message = ValidationMessageConstant.notBlankMessage)
    private String path;

}
