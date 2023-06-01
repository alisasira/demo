package ua.alisasira.thymeleaf.facade;

public class BookingException extends Exception {

    private String title;

    private String message;

    public BookingException(String title, String message) {
        this.title = title;
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}