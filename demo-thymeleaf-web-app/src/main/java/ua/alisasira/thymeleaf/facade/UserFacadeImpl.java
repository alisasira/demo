package ua.alisasira.thymeleaf.facade;

import ua.alisasira.thymeleaf.bean.RegistrationBean;
import ua.alisasira.thymeleaf.bean.UserBean;
import ua.alisasira.thymeleaf.entity.RoleType;
import ua.alisasira.thymeleaf.entity.User;
import ua.alisasira.thymeleaf.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserFacadeImpl implements UserFacade {

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Override
    public Long createUser(RegistrationBean bean) throws RegistrationException {
        if (StringUtils.isEmpty(bean.getPassword())) {
            throw new RegistrationException("Invalid data", "The line is null. Please enter Your password");
        }
        if (!bean.getPassword().equals(bean.getPasswordConfirmation())) {
            throw new RegistrationException("Invalid data", "Password and Confirmation password does not match");
        }
        if (userService.getUserByLogin(bean.getEmail()).isPresent()) {
            throw new RegistrationException("Invalid data", String.format("An account with email %s already exists", bean.getEmail()));
        }

        User user = new User();
        user.setEmail(bean.getEmail());
        user.setPassword(encoder.encode(bean.getPassword()));
        user.setRoleType(RoleType.ROLE_USER);
        user = userService.createUser(user);
        return user.getId();
    }

    @Override
    public List<UserBean> getUserList() {
        List<User> users = userService.getUsers();
        return users.stream().map(it -> transformUser(it)).collect(Collectors.toList());
    }

    private UserBean transformUser(User user) {
        UserBean bean = new UserBean();
        bean.setId(user.getId());
        bean.setEmail(user.getEmail());
        bean.setAmountOfBookings(user.getBookings().size());
        return bean;
    }
}