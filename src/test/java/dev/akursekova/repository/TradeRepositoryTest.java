package dev.akursekova.repository;

import dev.akursekova.entities.Order;
import dev.akursekova.entities.Security;
import dev.akursekova.entities.Trade;
import dev.akursekova.entities.User;
import dev.akursekova.exception.SecurityNotExistException;
import dev.akursekova.exception.TradeNotExistException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TradeRepositoryTest {
    private Map<Long, Trade> trades;
    private TradeRepository tradeRepository;

    @BeforeEach
    void setup() {
        trades = new HashMap<>();
        this.tradeRepository = new TradeRepository(trades);

        Trade trade1 = new Trade(1L, 2L, 50, 100);
        Trade trade2 = new Trade(3L, 4L, 50, 100);
        trades.put(2L, trade1);
        trades.put(3L, trade2);

    }

    @SneakyThrows
    @Test
    void test_addTrade_NewTrade() {
        Trade trade = new Trade(1L, 2L, 50, 100);
        trade.setId(1L);
        tradeRepository.addTrade(trade);
        assertEquals(3, tradeRepository.trades.size());
    }

    @SneakyThrows
    @Test
    void test_getTrade_WhenIdIsPresent() {
        Trade expectedTrade = trades.get(2L);
        assertEquals(expectedTrade, tradeRepository.getTrade(2L));
    }

    @Test
    void test_getTrade_WhenIdIsNotPresent_ShouldThrowTradeNotExistException() {
        assertThrows(TradeNotExistException.class,
                () -> tradeRepository.getTrade(1L));
    }

}