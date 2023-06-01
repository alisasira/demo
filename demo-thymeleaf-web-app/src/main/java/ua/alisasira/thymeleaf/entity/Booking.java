package ua.alisasira.thymeleaf.entity;


import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name = "bookings")
public class Booking extends BaseEntity {

    @Column(name = "from_date")
    private Date fromDate;

    @Column(name = "to_date")
    private Date toDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private BookingStatus status;
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void onPrePersist() {
        super.onPrePersist();
        this.status = BookingStatus.PENDING;
    }
}