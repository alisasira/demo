package ua.alisasira.thymeleaf;

import ua.alisasira.thymeleaf.security.SecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import ua.alisasira.thymeleaf.CarRentApp;

@ContextConfiguration(classes = CarRentApp.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles(value = {"test"})
public abstract class BaseTestCase {

    protected static final String DEFAULT_USER_NAME = "test.user@gmail.com";

    protected static final String DEFAULT_USER_PASSWORD = "test";

    protected static final Long DEFAULT_USER_ID = 2L;

    @Autowired
    private AuthenticationManager authenticationManager;

    protected void loginAsDefaultUser() {
        SecurityManager.authenticate(authenticationManager,
                new UsernamePasswordAuthenticationToken(DEFAULT_USER_NAME, DEFAULT_USER_PASSWORD));
    }
}