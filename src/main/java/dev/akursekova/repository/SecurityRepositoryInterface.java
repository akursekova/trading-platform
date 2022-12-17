package dev.akursekova.repository;

import dev.akursekova.entities.Security;
import dev.akursekova.exception.SecurityCreationException;
import dev.akursekova.exception.SecurityNotExistException;

public interface SecurityRepositoryInterface {
    void addSecurity(Security security) throws SecurityCreationException;

    Security getSecurity(Long id) throws SecurityNotExistException;

    boolean exists(Security security);
}
