package dev.akursekova.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorMessageDto {
    private String errorMessage;
}
