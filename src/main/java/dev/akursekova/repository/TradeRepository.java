package dev.akursekova.repository;

import dev.akursekova.entities.Trade;
import dev.akursekova.exception.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class TradeRepository implements TradeRepositoryInterface {

    private static final Logger LOG = LogManager.getLogger(TradeRepository.class);

    private static AtomicLong tradeId = new AtomicLong(0L);
    protected Map<Long, Trade> trades;

    public TradeRepository(Map<Long, Trade> trades) {
        this.trades = trades;
    }


    @Override
    public Trade addTrade(Trade trade) throws TradeCreationException {
        if (trade.getBuyOrderId() == trade.getSellOrderId()) {
            LOG.error("Trade cannot be created: sellOrderId is equal to buyOrderId");
            throw new TradeCreationException("sellOrderId cannot be equal to buyOrderId");
        }
        trade.setId(tradeId.incrementAndGet());
        trades.put(trade.getId(), trade);
        LOG.debug("New trade has been added to the trade repository. Trade = " + trade.toString());

        return trade;
    }

    @Override
    public Trade getTrade(Long id) throws TradeNotExistException {
        if (!trades.containsKey(id)) {
            LOG.error("Trade with given id = " + id + " doesn't exist");
            throw new TradeNotExistException("trade with id = " + id + " doesn't exist");
        }
        return trades.get(id);
    }

    @Override
    public Collection<Trade> getAllTrades() {
        return trades.values();
    }
}
