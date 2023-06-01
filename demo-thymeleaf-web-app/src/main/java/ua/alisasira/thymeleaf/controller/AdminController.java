package ua.alisasira.thymeleaf.controller;

import ua.alisasira.thymeleaf.entity.BookingStatus;
import ua.alisasira.thymeleaf.facade.BookingFacade;
import ua.alisasira.thymeleaf.facade.UserFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private BookingFacade bookingFacade;

    @Autowired
    private UserFacade userFacade;

    @GetMapping()
    public String index() {
        return "redirect:/admin/history";
    }

    @GetMapping("/history")
    public String bookingHistory(Model model) {
        model.addAttribute("bookingHistoryList", bookingFacade.getBookingHistory());
        return "pages/admin/history";
    }

    @GetMapping("/users")
    public String userList(Model model) {
        model.addAttribute("userList", userFacade.getUserList());
        return "pages/admin/users_list";
    }

    @GetMapping("/history/{userId}")
    public String userBookingHistory(@PathVariable Long userId, Model model) {
        model.addAttribute("bookingHistoryList", bookingFacade.getBookingHistory(userId));
        return "pages/admin/history";
    }

    @GetMapping("/approve/{bookingId}")
    public String approveBooking(@PathVariable Long bookingId) {
        bookingFacade.changeStatus(bookingId, BookingStatus.APPROVED);
        return "redirect:/admin/history";
    }

    @GetMapping("/decline/{bookingId}")
    public String declineBooking(@PathVariable Long bookingId) {
        bookingFacade.changeStatus(bookingId, BookingStatus.DECLINED);
        return "redirect:/admin/history";
    }
}