package ua.alisasira.thymeleaf.repository;

import ua.alisasira.thymeleaf.entity.Booking;
import ua.alisasira.thymeleaf.entity.BookingStatus;
import ua.alisasira.thymeleaf.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findFirst15ByUserOrderByFromDateDesc(User user);

    List<Booking> findAllByToDateAfter(Date fromDate);

    List<Booking> findAllByFromDateLessThanAndToDateGreaterThanAndStatusIsIn(Date endDate, Date startDate, List<BookingStatus> status);

    List<Booking> findAllByToDateLessThanAndStatus(Date toDate, BookingStatus status);
}