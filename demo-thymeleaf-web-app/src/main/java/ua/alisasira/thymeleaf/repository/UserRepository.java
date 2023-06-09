package ua.alisasira.thymeleaf.repository;

import ua.alisasira.thymeleaf.entity.RoleType;
import ua.alisasira.thymeleaf.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    List<User> findAllByRoleType(RoleType roleType);
}