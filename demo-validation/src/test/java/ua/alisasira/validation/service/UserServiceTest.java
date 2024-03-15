package ua.alisasira.validation.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import ua.alisasira.validation.BaseTestCase;
import ua.alisasira.validation.entity.User;
import ua.alisasira.validation.exception.ResourceNotFoundException;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ua.alisasira.validation.builder.UserBuilder.someUser;

public class UserServiceTest extends BaseTestCase {

    @Autowired
    private UserService service;

    private Long ALEX_ID;

    @BeforeEach
    public void setup() {
        service.reset();

        service.create(
                someUser()
                        .withFirstName("Nick")
                        .withLastName("Smith")
                        .withBirthDate(LocalDate.parse("2023-09-05"))
                        .build());

        ALEX_ID = service.create(
                someUser()
                        .withFirstName("Alex")
                        .withLastName("Grey")
                        .withBirthDate(LocalDate.parse("2023-09-10"))
                        .build()).getId();

        service.create(
                someUser()
                        .withFirstName("Sam")
                        .withLastName("Brown")
                        .withBirthDate(LocalDate.parse("2023-09-11"))
                        .build());
    }

    @Test
    public void testCreate() {
        User user = service.create(someUser()
                .withPhoneNumber("12345")
                .build());

        assertThat(user, is(notNullValue()));
        assertThat(user.getId(), greaterThan(0L));
    }

    @Test
    public void testUpdate() {

        User updated = service.update(someUser()
                .withId(ALEX_ID)
                .withFirstName("Dave")
                .build()
        );

        assertThat(updated.getFirstName(), equalTo("Dave"));
        assertThat(updated.getLastName(), equalTo("Grey"));
    }

    @Test
    public void testUpdateNonExistingUser() {
        Executable function = () -> service.update(someUser()
                .withId(0L)
                .withFirstName("Polly")
                .build());

        assertThrows(ResourceNotFoundException.class, function);
    }

    @Test
    public void testDelete() {
        boolean deleted = service.delete(ALEX_ID);
        assertThat(deleted, equalTo(true));

        deleted = service.delete(ALEX_ID);
        assertThat(deleted, equalTo(false));
    }

    @Test
    public void testSearchBorderCase() {
        List<User> users = service.search(LocalDate.parse("2023-09-08"), LocalDate.parse("2023-09-11"));

        assertThat(users, hasSize(1));
        assertThat(users.get(0).getFirstName(), equalTo("Alex"));
        assertThat(users.get(0).getLastName(), equalTo("Grey"));
    }

    @Test
    public void testSearchSorting() {
        List<User> users = service.search(LocalDate.parse("2023-09-08"), LocalDate.parse("2023-09-12"));

        assertThat(users, hasSize(2));
        {
            User user = users.get(0);
            assertThat(user.getFirstName(), equalTo("Sam"));
            assertThat(user.getLastName(), equalTo("Brown"));
        }
        {
            User user = users.get(1);
            assertThat(user.getFirstName(), equalTo("Alex"));
            assertThat(user.getLastName(), equalTo("Grey"));
        }
    }
}
