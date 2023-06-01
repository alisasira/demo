package ua.alisasira.thymeleaf.bean;

import ua.alisasira.thymeleaf.facade.BookingException;
import ua.alisasira.thymeleaf.facade.RegistrationException;

public class AlertMessage {

    private String header;

    private String message;

    public static AlertMessage build(BookingException exception) {
        AlertMessage am = new AlertMessage();
        am.setHeader(exception.getTitle());
        am.setMessage(exception.getMessage());
        return am;
    }

    public static AlertMessage build(RegistrationException exception) {
        AlertMessage am = new AlertMessage();
        am.setHeader(exception.getTitle());
        am.setMessage(exception.getMessage());
        return am;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}