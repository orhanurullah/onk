package com.onk.dto;

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
public class CategoryDto {

    private Long id;

    @Size(min = 2)
    @NotBlank
    private String name;

    @Size(min = 4)
    private String description;

    private boolean isActive;

    private Long parentCategory;
}
