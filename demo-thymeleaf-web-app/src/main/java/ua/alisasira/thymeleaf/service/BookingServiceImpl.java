package ua.alisasira.thymeleaf.service;

import ua.alisasira.thymeleaf.entity.Booking;
import ua.alisasira.thymeleaf.entity.BookingStatus;
import ua.alisasira.thymeleaf.entity.User;
import ua.alisasira.thymeleaf.repository.BookingRepository;
import ua.alisasira.thymeleaf.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Booking createBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    @Override
    public List<Booking> getBookingHistory(Long id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) {
            return bookingRepository.findFirst15ByUserOrderByFromDateDesc(user.get());
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public List<Booking> getBookingHistory(Date fromDate) {
        return bookingRepository.findAllByToDateAfter(fromDate);
    }

    @Override
    public Boolean changeBookingStatus(Long id, BookingStatus status) {
        Optional<Booking> booking = bookingRepository.findById(id);
        booking.ifPresent(it -> {
            it.setStatus(status);
            bookingRepository.save(it);
        });

        return booking.isPresent();
    }

    @Override
    public List<Booking> getPendingBookingBetweenDate(Date from, Date to) {
        return bookingRepository.findAllByFromDateLessThanAndToDateGreaterThanAndStatusIsIn(
                to,
                from,
                List.of(BookingStatus.PENDING, BookingStatus.APPROVED)
        );
    }

    @Override
    public List<Booking> getOutdatedBookingIds(Date date) {
        return bookingRepository.findAllByToDateLessThanAndStatus(date, BookingStatus.APPROVED);
    }
}