package ru.yandex.practicum.filmorate;

import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    private UserController userController;

    @BeforeEach
    void setUp() {
        UserService userService = new UserService();
        userController = new UserController(userService);
    }

    @Test
    void addUserShouldAddValidUser() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("testuser");
        user.setName("Test User");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        User result = userController.createUser(user);

        assertEquals(1, result.getId());
        assertEquals("test@example.com", result.getEmail());
        assertEquals("testuser", result.getLogin());
        assertEquals("Test User", result.getName());
    }

    @Test
    void addUserShouldUseLoginAsNameIfNameIsEmpty() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("testuser");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        User result = userController.createUser(user);

        assertEquals("testuser", result.getName());
    }

    @Test
    void addUserShouldThrowExceptionForInvalidEmail() {
        User user = new User();
        user.setEmail("invalid-email");
        user.setLogin("testuser");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userController.createUser(user));
        assertEquals("Электронная почта не может быть пустой и должна содержать символ '@'.", exception.getMessage());
    }

    @Test
    void addUserShouldThrowExceptionForEmptyLogin() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userController.createUser(user));
        assertEquals("Логин не может быть пустым и содержать пробелы.", exception.getMessage());
    }

    @Test
    void updateUserShouldUpdateExistingUser() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("testuser");
        user.setName("Test User");
        user.setBirthday(LocalDate.of(1990, 1, 1));
        userController.createUser(user);

        User updatedUser = new User();
        updatedUser.setId(1);
        updatedUser.setEmail("updated@example.com");
        updatedUser.setLogin("updateduser");
        updatedUser.setName("Updated Name");
        updatedUser.setBirthday(LocalDate.of(1995, 5, 20));

        User result = userController.updateUser(updatedUser);

        assertEquals(1, result.getId());
        assertEquals("updated@example.com", result.getEmail());
        assertEquals("updateduser", result.getLogin());
        assertEquals("Updated Name", result.getName());
    }
}
