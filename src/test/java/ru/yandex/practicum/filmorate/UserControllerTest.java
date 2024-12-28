package ru.yandex.practicum.filmorate;

import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserControllerTest {

    private UserController userController;

    @BeforeEach
    void setUp() {
        userController = new UserController();
    }

    @Test
    void addUser_ShouldAddValidUser() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("testuser");
        user.setName("Тестовый пользователь");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        User result = userController.addUser(user);

        assertEquals(1, result.getId());
        assertEquals("test@example.com", result.getEmail());
        assertEquals("testuser", result.getLogin());
        assertEquals("Тестовый пользователь", result.getName());
    }

    @Test
    void addUser_ShouldUseLoginAsNameIfNameIsEmpty() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("testuser");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        User result = userController.addUser(user);

        assertEquals("testuser", result.getName());
    }

    @Test
    void addUser_ShouldThrowExceptionForInvalidEmail() {
        User user = new User();
        user.setEmail("invalid-email");
        user.setLogin("testuser");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        ValidationException exception = assertThrows(ValidationException.class, () -> userController.addUser(user));
        assertEquals("Электронная почта обязательна и должна содержать символ '@'.", exception.getMessage());
    }

    @Test
    void addUser_ShouldThrowExceptionForEmptyLogin() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin(""); // Пустой логин
        user.setBirthday(LocalDate.of(1990, 1, 1));

        ValidationException exception = assertThrows(ValidationException.class, () -> userController.addUser(user));
        assertEquals("Логин не может быть пустым или содержать пробелы.", exception.getMessage());
    }

    @Test
    void updateUser_ShouldUpdateExistingUser() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("testuser");
        user.setName("Тестовый пользователь");
        user.setBirthday(LocalDate.of(1990, 1, 1));
        userController.addUser(user);

        User updatedUser = new User();
        updatedUser.setId(1);
        updatedUser.setEmail("updated@example.com");
        updatedUser.setLogin("updateduser");
        updatedUser.setName("Обновлённое имя");
        updatedUser.setBirthday(LocalDate.of(1995, 5, 20));

        User result = userController.updateUser(updatedUser);

        assertEquals(1, result.getId());
        assertEquals("updated@example.com", result.getEmail());
        assertEquals("updateduser", result.getLogin());
        assertEquals("Обновлённое имя", result.getName());
    }

    @Test
    void updateUser_ShouldThrowExceptionForNonExistentUser() {
        User user = new User();
        user.setId(999); // Несуществующий ID
        user.setEmail("test@example.com");
        user.setLogin("testuser");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        ValidationException exception = assertThrows(ValidationException.class, () -> userController.updateUser(user));
        assertEquals("Пользователь с id 999 не найден.", exception.getMessage());
    }
}
