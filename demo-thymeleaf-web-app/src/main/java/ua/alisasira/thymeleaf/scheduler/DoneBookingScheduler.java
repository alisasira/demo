package ua.alisasira.thymeleaf.scheduler;

import ua.alisasira.thymeleaf.entity.BookingStatus;
import ua.alisasira.thymeleaf.facade.BookingFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@Profile("!test")
public class DoneBookingScheduler {

    public static final long DELAY = 60;

    @Autowired
    private BookingFacade bookingFacade;

    @Scheduled(fixedDelay = DELAY, timeUnit = TimeUnit.SECONDS)
    public void markBookingAsDone() {
        Date now = new Date();
        List<Long> ids = bookingFacade.getOutdatedBookingIds(now);
        if (!ids.isEmpty()) {
            ids.forEach(id -> bookingFacade.changeStatus(id, BookingStatus.DONE));
        }
    }
}