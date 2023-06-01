package ua.alisasira.thymeleaf.service;

import ua.alisasira.thymeleaf.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User createUser(User user);

    Optional<User> getUserById(Long id);

    List<User> getUsers();

    Optional<User> getUserByLogin(String email);
}