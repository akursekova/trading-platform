package dev.akursekova.repository;

import dev.akursekova.entities.User;
import dev.akursekova.exception.UserCreationException;
import dev.akursekova.exception.UserNotExistException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class UserRepository implements UserRepositoryInterface {

    private static final Logger LOG = LogManager.getLogger(UserRepository.class);

    private static AtomicLong userId = new AtomicLong(0L);
    private Map<Long, User> users = new HashMap<>();

    @Override
    public void addUser(User user) throws UserCreationException {
        if (user.getUsername().isBlank()) {
            LOG.error("User with empty username cannot be created");
            throw new UserCreationException("empty username");
        }
        if (user.getPassword().isBlank()) {
            LOG.error("User with empty password cannot be created");
            throw new UserCreationException("empty password");
        }
        if (exists(user)) {
            LOG.error("User with username = " + user.getUsername() + " already exists");
            throw new UserCreationException("User with username = '" + user.getUsername() + "' already exists");
        }

        user.setId(userId.incrementAndGet());
        users.put(user.getId(), user);
        LOG.debug("New user created: " + user.toString());
    }

    @Override
    public User getUser(Long id) throws UserNotExistException {
        if (!users.containsKey(id)) {
            LOG.error("User with given id = " + id + " doesn't exist");
            throw new UserNotExistException("user with id = " + id + " doesn't exist");
        }
        return users.get(id);
    }

    @Override
    public boolean exists(User user) {
        return !users.values()
                .stream()
                .filter(u -> u.getUsername().equals(user.getUsername()))
                .findFirst()
                .equals(Optional.empty());
    }
}
