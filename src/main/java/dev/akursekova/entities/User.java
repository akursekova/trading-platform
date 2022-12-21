package dev.akursekova.entities;

import dev.akursekova.exception.UserCreationException;
import lombok.Builder;
import lombok.Data;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Data
public class User {
    private long id;
    private String username;
    private String password;

    public User(Long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public User() {
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String hashPassword(String password) throws UserCreationException {
        String generatedPassword = null;

        try {
            MessageDigest md = null;
            md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());

            byte[] bytes = md.digest();

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }

            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return generatedPassword;
    }
}
