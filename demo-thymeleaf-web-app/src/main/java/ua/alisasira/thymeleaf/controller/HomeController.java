package ua.alisasira.thymeleaf.controller;

import ua.alisasira.thymeleaf.bean.AlertMessage;
import ua.alisasira.thymeleaf.bean.RegistrationBean;
import ua.alisasira.thymeleaf.entity.RoleType;
import ua.alisasira.thymeleaf.facade.BookingFacade;
import ua.alisasira.thymeleaf.facade.RegistrationException;
import ua.alisasira.thymeleaf.facade.UserFacade;
import ua.alisasira.thymeleaf.security.SecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

import static ua.alisasira.thymeleaf.facade.BookingFacade.OUTPUT_FORMAT;

@Controller
public class HomeController {

    @Autowired
    private UserFacade userFacade;

    @Autowired
    private BookingFacade bookingFacade;

    @GetMapping("/")
    public String index(Model model) {
        String STATIC_HEADER = "Next available time: %s";
        Date nextAvailableTime = bookingFacade.getNextAvailableTimeFrom(new Date());
        model.addAttribute("nextAvailableTime", String.format(
                STATIC_HEADER,
                nextAvailableTime != null ? OUTPUT_FORMAT.format(nextAvailableTime) : "Not available today"
        ));
        return "home";
    }

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("registrationForm", new RegistrationBean());
        return "registration";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute RegistrationBean registrationBean, Model model) {
        try {
            userFacade.createUser(registrationBean);
        } catch (RegistrationException e) {
            model.addAttribute("alertMessage", AlertMessage.build(e));
            model.addAttribute("registrationForm", new RegistrationBean());
            return "registration";
        }
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        if (SecurityManager.isAuthenticated()) {
            return redirectToPageByRole();
        }
        return "login";
    }

    @GetMapping("/login_failed")
    public String loginFailed(Model model) {
        AlertMessage message = new AlertMessage();
        message.setHeader("Something went wrong: ");
        message.setMessage("You are not registered. Wrong email or password.");
        model.addAttribute("alertMessage", message);
        return "login_failed";
    }

    @GetMapping("/home")
    public String home() {
        return redirectToPageByRole();
    }

    private String redirectToPageByRole() {
        if (SecurityManager.hasRole(RoleType.ROLE_USER.name())) {
            return "redirect:/booking";
        }
        if (SecurityManager.hasRole(RoleType.ROLE_ADMIN.name())) {
            return "redirect:/admin";
        }
        return "login";
    }
}