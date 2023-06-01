package ua.alisasira.thymeleaf.facade;

import ua.alisasira.thymeleaf.BaseTestCase;
import ua.alisasira.thymeleaf.bean.RegistrationBean;
import ua.alisasira.thymeleaf.bean.UserBean;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ua.alisasira.thymeleaf.facade.RegistrationException;
import ua.alisasira.thymeleaf.facade.UserFacade;

import java.util.List;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class UserFacadeTest extends BaseTestCase {

    @Autowired
    private UserFacade userFacade;

    @Test
    public void testGetUserList() {
        List<UserBean> users = userFacade.getUserList();
        assertThat(users, hasSize(greaterThanOrEqualTo(1)));
    }

    @Test
    public void testCreateUserSuccessfully() throws RegistrationException {
        RegistrationBean registration = new RegistrationBean();
        registration.setEmail("other.test@gmail.com");
        registration.setPassword("123");
        registration.setPasswordConfirmation("123");

        Long userId = userFacade.createUser(registration);

        assertThat(userId, is(notNullValue()));
    }

    @Test
    public void testCreateUserValidationPasswordEmpty() {
        RegistrationBean registration = new RegistrationBean();
        registration.setEmail("failed.test@gmail.com");

        try {
            userFacade.createUser(registration);
        } catch (RegistrationException e) {
            assertThat(e.getMessage(), equalTo("The line is null. Please enter Your password"));
        }
    }

    @Test
    public void testCreateUserPasswordConfirmation() {
        RegistrationBean registration = new RegistrationBean();
        registration.setEmail("failed.test@gmail.com");
        registration.setPassword("123");
        registration.setPasswordConfirmation("456");

        try {
            userFacade.createUser(registration);
        } catch (RegistrationException e) {
            assertThat(e.getMessage(), equalTo("Password and Confirmation password does not match"));
        }
    }

    @Test
    public void testCreateUserUniqueEmail() {
        {
            RegistrationBean registration = new RegistrationBean();
            registration.setEmail("unique.test@gmail.com");
            registration.setPassword("123");
            registration.setPasswordConfirmation("123");

            try {
                Long userId = userFacade.createUser(registration);
                assertThat(userId, is(notNullValue()));
            } catch (RegistrationException e) {
                Assertions.fail("User creation is not successfully");
            }
        }

        {
            RegistrationBean registration = new RegistrationBean();
            registration.setEmail("unique.test@gmail.com");
            registration.setPassword("456");
            registration.setPasswordConfirmation("456");

            try {
                userFacade.createUser(registration);
            } catch (RegistrationException e) {
                assertThat(e.getMessage(), startsWith("An account with email"));
            }
        }
    }
}