package ua.alisasira.validation.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.alisasira.validation.BaseTestCase;
import ua.alisasira.validation.builder.UserBuilder;
import ua.alisasira.validation.entity.User;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;
import static ua.alisasira.validation.builder.UserBuilder.someUser;

public class ControllerIntegrationTest extends BaseTestCase {
    private Long BOB_ID;

    private static final int OK_RESPONSE_CODE = 200;
    private static final int BAD_REQUEST_RESPONSE_CODE = 400;
    private static final int NOT_FOUND_RESPONSE_CODE = 404;
    private static final int INTERNAL_SERVER_ERROR_RESPONSE_CODE = 500;

    @BeforeEach
    public void setup() throws IOException {
        BOB_ID = provider.create(defaultBuilder().build())
                .deserialize(User.class)
                .getId();
    }

    @AfterEach
    public void tearDown() throws IOException {
        provider.delete(BOB_ID);
    }

    @Test
    public void testUserCreate() throws IOException {
        User created = provider.create(someUser()
                .withFirstName("Bob")
                .withLastName("Tail")
                .withPhoneNumber("0987654321")
                .withEmail("bob@gmail.com")
                .build()).deserialize(User.class);

        assertThat(created, is(notNullValue()));
        assertThat(created.getId(), greaterThan(0L));
    }

    @Test
    public void testUserCreateValidation() throws IOException {
        // id should be NULL on create
        assertThat(

                provider.create(
                        defaultBuilder()
                                .withId(10L)
                                .build()
                ).statusCode(),

                equalTo(INTERNAL_SERVER_ERROR_RESPONSE_CODE)
        );

        // incorrect email address
        assertThat(
                provider.create(
                        defaultBuilder()
                                .withEmail("abcd")
                                .build()
                ).statusCode(),
                equalTo(BAD_REQUEST_RESPONSE_CODE)
        );

        // blank first name
        assertThat(
                provider.create(
                        defaultBuilder()
                                .withFirstName("")
                                .build()
                ).statusCode(),
                equalTo(INTERNAL_SERVER_ERROR_RESPONSE_CODE)
        );

        // age less than 18 years (set in application-test.properties)
        assertThat(
                provider.create(
                        defaultBuilder()
                                .withBirthDate("2010-02-02")
                                .build()
                ).statusCode(),
                equalTo(BAD_REQUEST_RESPONSE_CODE)
        );
    }

    @Test
    public void testUserUpdate() throws IOException {
        User updated = provider.update(someUser()
                .withId(BOB_ID)
                .withFirstName("Den")
                .withLastName("Brown")
                .build()).deserialize(User.class);

        assertThat(updated.getFirstName(), equalTo("Den"));
        assertThat(updated.getLastName(), equalTo("Brown"));
        assertThat(updated.getPhoneNumber(), equalTo("555555555"));
        assertThat(updated.getBirthDate(), equalTo(LocalDate.parse("1999-02-02")));
        assertThat(updated.getEmail(), equalTo("bob@gmail.com"));
        assertThat(updated.getAddress(), equalTo("Dnipro"));
    }

    @Test
    public void testUserUpdateValidator() throws IOException {
        // id should be NOT NULL on update
        assertThat(

                provider.update(
                        defaultBuilder()
                                .withId(null)
                                .build()
                ).statusCode(),

                equalTo(INTERNAL_SERVER_ERROR_RESPONSE_CODE)
        );

        // id doesn't exist in storage
        assertThat(

                provider.update(
                        defaultBuilder()
                                .withId(1100L)
                                .build()
                ).statusCode(),

                equalTo(NOT_FOUND_RESPONSE_CODE)
        );

        // age less than 18 years (set in application-test.properties)
        assertThat(

                provider.update(
                        defaultBuilder()
                                .withBirthDate("2010-02-02")
                                .build()
                ).statusCode(),

                equalTo(BAD_REQUEST_RESPONSE_CODE)
        );
    }

    @Test
    public void testDelete() throws IOException {
        assertThat(provider.delete(BOB_ID), equalTo(OK_RESPONSE_CODE));
        assertThat(provider.delete(BOB_ID), equalTo(NOT_FOUND_RESPONSE_CODE));
    }

    @Test
    public void search() throws IOException {
        List<User> result = provider.search("1999-02-01", "1999-02-03").deserializeToList();

        assertThat(result, hasSize(1));
        assertThat(result.get(0).getId(), equalTo(BOB_ID));
    }

    private static UserBuilder defaultBuilder() {
        return someUser()
                .withFirstName("Bob")
                .withLastName("Fox")
                .withPhoneNumber("555555555")
                .withBirthDate("1999-02-02")
                .withEmail("bob@gmail.com")
                .withAddress("Dnipro");
    }
}
