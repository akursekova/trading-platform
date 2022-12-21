package dev.akursekova.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CreatedUserDto {
    private Long id;
    private String username;
}

