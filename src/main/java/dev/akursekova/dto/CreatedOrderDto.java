package dev.akursekova.dto;

import dev.akursekova.entities.Order;
import dev.akursekova.entities.Trade;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CreatedOrderDto {
    private Order order;
    private Trade trade;
}
