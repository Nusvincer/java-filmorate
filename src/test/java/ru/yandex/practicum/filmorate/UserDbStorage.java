package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import(UserDbStorage.class)
class UserDbStorageTest {

    @Autowired
    private UserDbStorage userDbStorage;

    @Test
    void shouldAddAndGetUser() {
        User user = new User("test@example.com", "testLogin", "Test Name", LocalDate.of(2000, 1, 1));

        User savedUser = userDbStorage.addUser(user);
        Optional<User> retrievedUser = userDbStorage.getUser(savedUser.getId());

        assertThat(retrievedUser).isPresent();
        assertThat(retrievedUser.get().getEmail()).isEqualTo("test@example.com");
        assertThat(retrievedUser.get().getLogin()).isEqualTo("testLogin");
        assertThat(retrievedUser.get().getName()).isEqualTo("Test Name");
    }

    @Test
    void shouldUpdateUser() {
        User user = new User("test@example.com", "testLogin", "Test Name", LocalDate.of(2000, 1, 1));
        User savedUser = userDbStorage.addUser(user);

        savedUser.setName("Updated Name");
        savedUser.setEmail("updated@example.com");
        userDbStorage.updateUser(savedUser);
        Optional<User> updatedUser = userDbStorage.getUser(savedUser.getId());

        assertThat(updatedUser).isPresent();
        assertThat(updatedUser.get().getName()).isEqualTo("Updated Name");
        assertThat(updatedUser.get().getEmail()).isEqualTo("updated@example.com");
    }

    @Test
    void shouldGetAllUsers() {
        User user1 = new User("user1@example.com", "user1Login", "User One", LocalDate.of(1990, 1, 1));
        User user2 = new User("user2@example.com", "user2Login", "User Two", LocalDate.of(1992, 2, 2));
        userDbStorage.addUser(user1);
        userDbStorage.addUser(user2);

        var users = userDbStorage.getAllUsers();

        assertThat(users).hasSize(2);
        assertThat(users).extracting("email").contains("user1@example.com", "user2@example.com");
        assertThat(users).extracting("login").contains("user1Login", "user2Login");
    }
}