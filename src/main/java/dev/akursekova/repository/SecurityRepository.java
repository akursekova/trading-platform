package dev.akursekova.repository;

import dev.akursekova.entities.Security;
import dev.akursekova.exception.SecurityCreationException;
import dev.akursekova.exception.SecurityNotExistException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class SecurityRepository implements SecurityRepositoryInterface {

    private static final Logger LOG = LogManager.getLogger(SecurityRepository.class);

    protected Map<Long, Security> securities;
    private static AtomicLong securityId = new AtomicLong(0L);

    public SecurityRepository(Map<Long, Security> securities) {
        this.securities = securities;
    }


    @Override
    public void addSecurity(Security security) throws SecurityCreationException {
        if (security.getName().isBlank()) {
            LOG.error("Security with empty name cannot be created");
            throw new SecurityCreationException("empty security name");
        }

        if (exists(security)) {
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
    public Collection<Security> getAllSecurities(){
        return securities.values();
    }

    //    @Override //todo investigate
    private boolean exists(Security security) {
        return !securities
                .values()
                .stream()
                .filter(s -> s.getName().equals(security.getName()))
                .findFirst()
                .equals(Optional.empty());
    }

}
