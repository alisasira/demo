package ua.alisasira.thymeleaf.facade;

import ua.alisasira.thymeleaf.bean.BookingBean;
import ua.alisasira.thymeleaf.bean.UserBookingsBean;
import ua.alisasira.thymeleaf.entity.BookingStatus;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public interface BookingFacade {

    SimpleDateFormat INPUT_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

    SimpleDateFormat OUTPUT_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    @Transactional
    Long createBooking(BookingBean bean) throws BookingException;

    @Transactional(readOnly = true)
    List<UserBookingsBean> getBookingHistory(Long id);

    @Transactional(readOnly = true)
    List<UserBookingsBean> getBookingHistory();

    @Transactional
    Boolean changeStatus(Long id, BookingStatus status);

    @Transactional(readOnly = true)
    List<Long> getOutdatedBookingIds(Date date);

    @Transactional(readOnly = true)
    Date getNextAvailableTimeFrom(Date date);
}