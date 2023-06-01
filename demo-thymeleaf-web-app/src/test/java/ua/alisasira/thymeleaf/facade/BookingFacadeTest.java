package ua.alisasira.thymeleaf.facade;

import ua.alisasira.thymeleaf.BaseTestCase;
import ua.alisasira.thymeleaf.bean.BookingBean;
import ua.alisasira.thymeleaf.bean.UserBookingsBean;
import ua.alisasira.thymeleaf.entity.BookingStatus;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ua.alisasira.thymeleaf.facade.BookingException;
import ua.alisasira.thymeleaf.facade.BookingFacade;

import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;

public class BookingFacadeTest extends BaseTestCase {

    @Autowired
    private BookingFacade bookingFacade;

    private DateTimeFormatter FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");

    @BeforeEach
    public void setup() {
        loginAsDefaultUser();
    }

    @Test
    public void testGetBookingHistory() {
        {
            List<UserBookingsBean> bookings = bookingFacade.getBookingHistory();
            assertThat(bookings, hasSize(greaterThanOrEqualTo(1)));
        }

        {
            List<UserBookingsBean> bookings = bookingFacade.getBookingHistory(DEFAULT_USER_ID);
            assertThat(bookings, hasSize(greaterThanOrEqualTo(1)));
        }
    }

    @Test
    public void testChangeStatus() {
        assertTrue(bookingFacade.changeStatus(1L, BookingStatus.APPROVED));
    }

    @Test
    public void testCreateBooking() throws BookingException {
        Date from = new DateTime().withHourOfDay(12).toDate();
        Date to = new DateTime().withHourOfDay(14).toDate();

        Long bookingId = bookingFacade.createBooking(makeBooking(from, to));
        assertThat(bookingId, is(notNullValue()));
    }

    @Test
    public void testCreateBookingValidationTime() {
        {
            Date from = new Date();
            Date to = new DateTime().minusMinutes(5).toDate();

            try {
                bookingFacade.createBooking(makeBooking(from, to));
            } catch (BookingException e) {
                assertThat(e.getMessage(), equalTo("'To Date' should be greater then 'From Date'"));
            }
        }

        {
            Date from = new DateTime().withHourOfDay(5).toDate();
            Date to = new DateTime().withHourOfDay(8).toDate();

            try {
                bookingFacade.createBooking(makeBooking(from, to));
            } catch (BookingException e) {
                assertThat(e.getMessage(), equalTo("Out of working hours"));
            }
        }
    }

    @Test
    public void testCreateBookingAvailableTime() {
        DateTime toDateTime = new DateTime().plusDays(1).withHourOfDay(16).withMinuteOfHour(0);

        {
            Date from = new DateTime().plusDays(1).withHourOfDay(15).withMinuteOfHour(0).toDate();
            Date to = toDateTime.toDate();

            try {
                bookingFacade.createBooking(makeBooking(from, to));
            } catch (BookingException e) {
                fail("Booking is not successfully");
            }
        }

        {
            Date from = new DateTime().plusDays(1).withHourOfDay(15).withMinuteOfHour(30).toDate();
            Date to = new DateTime().plusDays(1).withHourOfDay(16).withMinuteOfHour(30).toDate();

            try {
                bookingFacade.createBooking(makeBooking(from, to));
            } catch (BookingException e) {
                assertThat(e.getMessage(), equalTo("Track is available from " + FORMATTER.print(toDateTime)));
            }
        }
    }

    @Test
    public void testNextAvailableTime() throws BookingException {

        DateTime now = FORMATTER.parseDateTime("2023-05-25 08:25");

        {
            DateTime bookingFrom = FORMATTER.parseDateTime("2023-05-25 07:00");
            DateTime bookingTo = FORMATTER.parseDateTime("2023-05-25 09:00");
            bookingFacade.createBooking(makeBooking(bookingFrom.toDate(), bookingTo.toDate()));
        }

        {
            DateTime bookingFrom = FORMATTER.parseDateTime("2023-05-25 09:10");
            DateTime bookingTo = FORMATTER.parseDateTime("2023-05-25 11:20");
            bookingFacade.createBooking(makeBooking(bookingFrom.toDate(), bookingTo.toDate()));

            Date nextAvailableDateTime = bookingFacade.getNextAvailableTimeFrom(now.toDate());
            assertThat(new DateTime(nextAvailableDateTime).toDate(), equalTo(bookingTo.toDate()));
        }
    }

    private static BookingBean makeBooking(Date from, Date to) {
        BookingBean booking = new BookingBean();
        booking.setFromDate(BookingFacade.INPUT_FORMAT.format(from));
        booking.setToDate(BookingFacade.INPUT_FORMAT.format(to));
        return booking;
    }
}