package ua.alisasira.validation.builder;

import ua.alisasira.validation.entity.User;

import java.time.LocalDate;

public class UserBuilder {

    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String address;
    private String phoneNumber;

    public static UserBuilder someUser() {
        return new UserBuilder();
    }

    public User build() {
        User user = new User();
        user.setId(id);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setBirthDate(birthDate);
        user.setAddress(address);
        user.setPhoneNumber(phoneNumber);
        return user;
    }

    public Long getId() {
        return id;
    }

    public UserBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public UserBuilder withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public UserBuilder withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public UserBuilder withBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public UserBuilder withBirthDate(String birthDate) {
        this.birthDate = LocalDate.parse(birthDate);
        return this;
    }

    public String getAddress() {
        return address;
    }

    public UserBuilder withAddress(String address) {
        this.address = address;
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public UserBuilder withPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }
}