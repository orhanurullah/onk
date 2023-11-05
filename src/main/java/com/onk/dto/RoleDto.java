package com.onk.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class RoleDto {

    private Long id;

    @NotBlank
    @Size(min=3, max = 100)
    private String name;

    @Size(min = 5, max = 255)
    private String description;

    @JsonIgnore
    private List<String> users; // only email
}
