package ua.alisasira.thymeleaf.bean;

public class UserBean extends BaseBean {

    private Long id;

    private String email;

    private Integer amountOfBookings;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAmountOfBookings() {
        return amountOfBookings;
    }

    public void setAmountOfBookings(Integer amountOfBookings) {
        this.amountOfBookings = amountOfBookings;
    }
}