package ua.alisasira.thymeleaf.service;

import ua.alisasira.thymeleaf.entity.Booking;
import ua.alisasira.thymeleaf.entity.BookingStatus;

import java.util.Date;
import java.util.List;

public interface BookingService {

    Booking createBooking(Booking booking);

    List<Booking> getBookingHistory(Long id);

    List<Booking> getBookingHistory(Date fromDate);

    Boolean changeBookingStatus(Long id, BookingStatus status);

    List<Booking> getPendingBookingBetweenDate(Date from, Date to);

    List<Booking> getOutdatedBookingIds(Date date);
}