package dev.akursekova.repository;

import dev.akursekova.entities.Order;
import dev.akursekova.entities.User;
import dev.akursekova.exception.UserCreationException;
import dev.akursekova.exception.UserNotExistException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryTest {

    private Map<Long, User> users;
    private UserRepository userRepository;


    @BeforeEach
    void setup() {
        //todo  вместо , использую рефлексию
        /*
        Field usersField = userRepository.getClass().getDeclaredField("users");
        usersField.setAccessible(true);
        Map<Long, User> usersValues = (Map<Long, User>) usersField.get(userRepository); //todo
        * */
        users = new HashMap<>();
        this.userRepository = new UserRepository(users);

        User user1 = new User(2L, "name1", "pswd1");
        User user2 = new User(3L, "name2", "pswd2");
        users.put(2L, user1);
        users.put(3L, user2);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "      "})
    void test_addUser_UserNameIsBlank(String argument) {
        User user = new User();
        user.setUsername(argument);
        assertThrows(UserCreationException.class, () -> userRepository.addUser(user));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "      "})
    void test_addUser_PasswordIsBlank(String argument) {
        User user = new User(1L, "test", argument);
        assertThrows(UserCreationException.class, () -> userRepository.addUser(user));
    }

    @Test
    void test_addUser_UserWithGivenUserNameAlreadyExists() {
        User user = new User(1L, "name1", "pswd3");
        assertThrows(UserCreationException.class, () -> userRepository.addUser(user));
    }

    @SneakyThrows
    @Test
    void test_addUser_NewUser() {
        User user = new User(1L, "name3", "pswd3");
        user.setId(3L);
        userRepository.addUser(user);

        Field usersField = userRepository.getClass().getDeclaredField("users");
        usersField.setAccessible(true);
        Map<Long, User> usersValues = (Map<Long, User>) usersField.get(userRepository); //todo
        assertEquals(3, usersValues.size());
    }

    @SneakyThrows
    @Test
    void test_getUser_WhenIdIsPresent() {
        User expectedUser = users.get(2L);
        assertEquals(expectedUser, userRepository.getUser(2L));
    }

    @Test
    void test_getUser_WhenIdIsNotPresent_ShouldThrowUserNotExistException() {
        assertThrows(UserNotExistException.class,
                () -> userRepository.getUser(1L));
    }

    @Test
    void test_getAllUsers_ShouldReturnWholeCollection() {
        Collection<User> expectedUsers = userRepository.users.values();
        assertEquals(expectedUsers, userRepository.getAllUsers());
    }

}