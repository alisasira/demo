package ua.alisasira.thymeleaf.facade;

import ua.alisasira.thymeleaf.bean.RegistrationBean;
import ua.alisasira.thymeleaf.bean.UserBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserFacade {

    @Transactional
    Long createUser(RegistrationBean user) throws RegistrationException;

    @Transactional(readOnly = true)
    List<UserBean> getUserList();
}