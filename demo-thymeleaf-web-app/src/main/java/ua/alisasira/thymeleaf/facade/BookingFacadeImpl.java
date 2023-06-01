package ua.alisasira.thymeleaf.facade;

import ua.alisasira.thymeleaf.bean.BookingBean;
import ua.alisasira.thymeleaf.bean.UserBookingsBean;
import ua.alisasira.thymeleaf.entity.BaseEntity;
import ua.alisasira.thymeleaf.entity.Booking;
import ua.alisasira.thymeleaf.entity.BookingStatus;
import ua.alisasira.thymeleaf.entity.User;
import ua.alisasira.thymeleaf.security.SecurityManager;
import ua.alisasira.thymeleaf.service.BookingService;
import ua.alisasira.thymeleaf.service.UserService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class BookingFacadeImpl implements BookingFacade {

    private static final int WORKING_FROM_HOUR = 6;
    private static final int WORKING_TO_HOUR = 22;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserService userService;

    @Override
    public Long createBooking(BookingBean bean) throws BookingException {
        Date from;
        Date to;

        try {
            from = INPUT_FORMAT.parse(bean.getFromDate());
            to = INPUT_FORMAT.parse(bean.getToDate());
        } catch (ParseException e) {
            throw new BookingException("Date Parsing Failed", e.getMessage());
        }

        if (to.before(from)) {
            throw new BookingException("Invalid period of time", "'To Date' should be greater then 'From Date'");
        }

        DateTime fromDateTime = new DateTime(from);

        Date fromWorkingDateTime = fromDateTime.withHourOfDay(WORKING_FROM_HOUR).withMinuteOfHour(0).withSecondOfMinute(0).toDate();
        Date toWorkingDateTime = fromDateTime.withHourOfDay(WORKING_TO_HOUR).withMinuteOfHour(0).withSecondOfMinute(0).toDate();

        if (from.before(fromWorkingDateTime) || to.after(toWorkingDateTime)) {
            throw new BookingException("Invalid period of time", "Out of working hours");
        }

        List<Booking> existing = bookingService.getPendingBookingBetweenDate(from, to);
        if (!existing.isEmpty()) {
            throw makeBookingException(existing, from, to);
        }

        //user couldn't be null because /booking URL secured
        User user = userService.getUserById(SecurityManager.getCurrentUserId()).orElse(null);

        Booking booking = new Booking();
        booking.setFromDate(from);
        booking.setToDate(to);
        booking.setUser(user);
        booking = bookingService.createBooking(booking);
        return booking.getId();
    }

    private BookingException makeBookingException(List<Booking> existing, Date from, Date to) {
        StringBuilder builder = new StringBuilder();

        existing.sort(Comparator.comparing(Booking::getToDate).reversed());
        Date availableFrom = existing.get(0).getToDate();

        existing.sort(Comparator.comparing(Booking::getFromDate));
        Date availableTo = existing.get(0).getFromDate();

        if (availableTo.after(from)) {
            builder.append("Track is available till ").append(OUTPUT_FORMAT.format(availableTo));
        }

        if (availableFrom.before(to)) {
            if (builder.isEmpty()) {
                builder.append("Track is available from ").append(OUTPUT_FORMAT.format(availableFrom));
            } else {
                builder.append(" and from ").append(OUTPUT_FORMAT.format(availableFrom));
            }
        }

        if (builder.isEmpty()) {
            builder.append("Track is not available");
        }

        return new BookingException("Requested period is not available", builder.toString());
    }

    @Override
    public List<UserBookingsBean> getBookingHistory(Long id) {
        List<Booking> bookings = bookingService.getBookingHistory(id);
        return bookings.stream().map(it -> transformHistory(it)).collect(Collectors.toList());
    }

    @Override
    public List<UserBookingsBean> getBookingHistory() {
        Date now = new Date();
        List<Booking> bookings = bookingService.getBookingHistory(now);
        return bookings.stream().map(it -> transformHistory(it)).collect(Collectors.toList());
    }

    @Override
    public Boolean changeStatus(Long id, BookingStatus status) {
        return bookingService.changeBookingStatus(id, status);
    }

    @Override
    public List<Long> getOutdatedBookingIds(Date date) {
        return bookingService.getOutdatedBookingIds(date).stream().map(BaseEntity::getId).collect(Collectors.toList());
    }

    @Override
    public Date getNextAvailableTimeFrom(Date from) {
        int MINIMAL_EXPECTED_BOOKING_DURATION_IN_MINUTES = 30;

        DateTime fromDateTime = new DateTime(from);

        DateTime fromWorkingDateTime = fromDateTime.withHourOfDay(WORKING_FROM_HOUR).withMinuteOfHour(0).withSecondOfMinute(0);
        DateTime toWorkingDateTime = fromDateTime.withHourOfDay(WORKING_TO_HOUR).withMinuteOfHour(0).withSecondOfMinute(0);

        if(fromDateTime.isAfter(toWorkingDateTime)) {
            fromDateTime = fromWorkingDateTime.plusDays(1);
            toWorkingDateTime = toWorkingDateTime.plusDays(1);
        } else if(fromDateTime.isBefore(fromWorkingDateTime)) {
            fromDateTime = fromWorkingDateTime;
        }

        from = fromDateTime.toDate();
        Date to = toWorkingDateTime.toDate();

        List<Booking> existing = bookingService.getPendingBookingBetweenDate(from, to);

        if(existing.isEmpty()) {
            return from;
        }

        existing.sort(Comparator.comparing(Booking::getFromDate));
        if(greaterThenMinutes(from, existing.get(0).getFromDate(), MINIMAL_EXPECTED_BOOKING_DURATION_IN_MINUTES)){
            return from;
        }

        Date nextAvailable = null;
        for (Booking booking: existing) {
            if(nextAvailable != null && greaterThenMinutes(nextAvailable, booking.getFromDate(),
                    MINIMAL_EXPECTED_BOOKING_DURATION_IN_MINUTES)) {
                return nextAvailable;
            } else {
                nextAvailable = booking.getToDate();
            }
        }

        return greaterThenMinutes(nextAvailable, to, MINIMAL_EXPECTED_BOOKING_DURATION_IN_MINUTES) ? nextAvailable : null;
    }

    private boolean greaterThenMinutes(Date source, Date target, int minutes) {
        DateTime sourceDt = new DateTime(source).plusMinutes(minutes);
        DateTime targetDt = new DateTime(target);

        return sourceDt.isBefore(targetDt);
    }

    private UserBookingsBean transformHistory(Booking booking) {
        UserBookingsBean bean = new UserBookingsBean();
        bean.setId(booking.getId());
        bean.setEmail(booking.getUser().getEmail());
        bean.setFromDate(OUTPUT_FORMAT.format(booking.getFromDate()));
        bean.setToDate(OUTPUT_FORMAT.format(booking.getToDate()));
        bean.setStatus(booking.getStatus().name());
        return bean;
    }
}