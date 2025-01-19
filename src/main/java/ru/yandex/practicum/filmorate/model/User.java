package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class User {

    @JsonProperty("id")
    private int id;

    @JsonProperty("email")
    @NotBlank(message = "Email не может быть пустым")
    @Pattern(regexp = ".+@.+\\..+", message = "Email должен быть корректным")
    private String email;

    @JsonProperty("login")
    @NotBlank(message = "Логин не может быть пустым")
    private String login;

    @JsonProperty("name")
    private String name;

    @JsonProperty("birthday")
    private LocalDate birthday;

    private Set<Integer> friends = new HashSet<>();

    public User() {
    }

    public User(int id, String email, String login, String name, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name != null && !name.isBlank() ? name : login;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public void setFriends(Set<Integer> friends) {
        this.friends = friends;
    }

    public void addFriend(int friendId) {
        friends.add(friendId);
    }

    public void removeFriend(int friendId) {
        friends.remove(friendId);
    }

    public Set<Integer> getFriends() {
        return friends;
    }

    public void validate() {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email не может быть пустым");
        }
        if (!email.matches(".+@.+\\..+")) {
            throw new IllegalArgumentException("Email должен быть корректным");
        }
        if (login == null || login.isBlank()) {
            throw new IllegalArgumentException("Логин не может быть пустым");
        }
        if (birthday != null && birthday.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Дата рождения должна быть в прошлом");
        }
    }
}


