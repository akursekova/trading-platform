package dev.akursekova.repository;

import dev.akursekova.entities.Trade;
import dev.akursekova.exception.TradeCreationException;
import dev.akursekova.exception.TradeNotExistException;

public interface TradeRepositoryInterface {
    void addTrade(Trade trade) throws TradeCreationException;

    Trade getTrade(Long id) throws TradeNotExistException;
}
