package dev.akursekova.repository;

import dev.akursekova.entities.Security;
import dev.akursekova.exception.SecurityCreationException;
import dev.akursekova.exception.SecurityNotExistException;
import dev.akursekova.exception.UserNotExistException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class SecurityRepository implements SecurityRepositoryInterface {

    private static final Logger LOG = LogManager.getLogger(SecurityRepository.class);
    private static AtomicLong securityId = new AtomicLong(0L);
    private Map<Long, Security> securities = new HashMap<>();


    @Override
    public void addSecurity(Security security) throws SecurityCreationException {
        if (security.getName().isBlank()) {
            LOG.error("Security with empty name cannot be created");
            throw new SecurityCreationException("empty security name");
        }

        if (exists(security)){
            LOG.error("Security with name = " + security.getName() + " already exists");
            throw new SecurityCreationException("Security with name = '" + security.getName() + "' already exists");
        }

        security.setId(securityId.incrementAndGet());
        securities.put(security.getId(), security);
        LOG.debug("New security created: " + security.toString());
    }

    @Override
    public Security getSecurity(Long id) throws SecurityNotExistException {
        if (!securities.containsKey(id)) {
            LOG.error("Security with given id = " + id + " doesn't exist");
            throw new SecurityNotExistException("security with id = " + id + " doesn't exist");
        }
        return securities.get(id);
    }

    @Override
    public boolean exists(Security security){
        return !securities.values().stream().filter(s -> s.getName().equals(security.getName())).findFirst().equals(Optional.empty());
    }

}
