package dev.akursekova.repository;

import dev.akursekova.entities.Trade;
import dev.akursekova.entities.User;
import dev.akursekova.exception.UserCreationException;
import dev.akursekova.exception.UserNotExistException;

public interface TradeRepositoryInterface {
    void addTrade(Trade trade);

    Trade getTrade(Long id);
}
