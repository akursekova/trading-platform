package dev.akursekova.repository;

import dev.akursekova.entities.Trade;
import dev.akursekova.exception.TradeCreationException;
import dev.akursekova.exception.TradeNotExistException;

import java.util.Collection;

public interface TradeRepositoryInterface {
    Trade addTrade(Trade trade) throws TradeCreationException;

    Trade getTrade(Long id) throws TradeNotExistException;

    Collection<Trade> getAllTrades();
}
