package dev.akursekova.repository;

import dev.akursekova.entities.User;
import dev.akursekova.exception.UserCreationException;
import dev.akursekova.exception.UserNotExistException;

public interface UserRepositoryInterface {
    void addUser(User user) throws UserCreationException;

    User getUser(Long id) throws UserNotExistException;

//    boolean exists(User user); //todo investigate
}
