package ua.alisasira.validation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ua.alisasira.validation.entity.User;
import ua.alisasira.validation.service.UserService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@Controller
@Validated
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService service;

    @Validated(User.OnCreate.class)
    @PostMapping
    public @ResponseBody User create(@Valid @RequestBody User user) {
        return service.create(user);
    }

    @Validated(User.OnUpdate.class)
    @PutMapping
    public @ResponseBody User update(@Valid @RequestBody User user) {
        return service.update(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> delete(@PathVariable Long id) {
        boolean result = service.delete(id);

        if (!result) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @GetMapping("/search")
    public @ResponseBody List<User> search(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        return service.search(from, to);
    }
}
