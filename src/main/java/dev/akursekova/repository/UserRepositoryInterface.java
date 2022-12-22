package dev.akursekova.repository;

import dev.akursekova.entities.User;
import dev.akursekova.exception.UserCreationException;
import dev.akursekova.exception.UserNotExistException;

import java.util.Collection;

public interface UserRepositoryInterface {
    void addUser(User user) throws UserCreationException;

    User getUser(Long id) throws UserNotExistException;

    Collection<User> getAllUsers();
}
