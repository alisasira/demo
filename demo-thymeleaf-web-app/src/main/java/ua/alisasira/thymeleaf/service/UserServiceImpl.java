package ua.alisasira.thymeleaf.service;

import ua.alisasira.thymeleaf.entity.RoleType;
import ua.alisasira.thymeleaf.entity.User;
import ua.alisasira.thymeleaf.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAllByRoleType(RoleType.ROLE_USER);
    }

    @Override
    public Optional<User> getUserByLogin(String email) {
        return userRepository.findByEmail(email);
    }
}