package dev.akursekova.repository;

import dev.akursekova.entities.Trade;
import dev.akursekova.entities.User;
import dev.akursekova.exception.UserCreationException;
import dev.akursekova.exception.UserNotExistException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class TradeRepository implements TradeRepositoryInterface {

    private static final Logger LOG = LogManager.getLogger(TradeRepository.class);

    private static AtomicLong tradeId = new AtomicLong(0L);
    private Map<Long, Trade> trades = new HashMap<>();


    @Override
    public void addTrade(Trade trade) {
        trade.setId(tradeId.incrementAndGet());
        trades.put(trade.getId(), trade);
    }

    @Override
    public Trade getTrade(Long id) {
        return null;
    }
}
