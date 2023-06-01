package ua.alisasira.thymeleaf.controller;

import ua.alisasira.thymeleaf.bean.AlertMessage;
import ua.alisasira.thymeleaf.bean.BookingBean;
import ua.alisasira.thymeleaf.entity.BookingStatus;
import ua.alisasira.thymeleaf.facade.BookingException;
import ua.alisasira.thymeleaf.facade.BookingFacade;
import ua.alisasira.thymeleaf.security.SecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/booking")
public class BookingController {

    @Autowired
    private BookingFacade bookingFacade;

    @GetMapping()
    public String home(Model model) {
        model.addAttribute("bookingForm", new BookingBean());
        return "pages/booking/home";
    }

    @PostMapping("/create")
    public String book(@ModelAttribute BookingBean bookingBean, Model model) {

        try {
            bookingFacade.createBooking(bookingBean);
        } catch (BookingException e) {
            model.addAttribute("alertMessage", AlertMessage.build(e));
            model.addAttribute("bookingForm", new BookingBean());
            return "pages/booking/home";
        }
        return "redirect:/booking/history";
    }

    @GetMapping("/history")
    public String bookingHistory(Model model) {
        model.addAttribute("bookingHistoryList",
                bookingFacade.getBookingHistory(SecurityManager.getCurrentUserId()));
        return "pages/booking/history";
    }

    @GetMapping("/cancel/{bookingId}")
    public String editBooking(@PathVariable Long bookingId) {
        bookingFacade.changeStatus(bookingId, BookingStatus.CANCELED);
        return "redirect:/booking/history";
    }
}