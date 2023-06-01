package ua.alisasira.rest.controller;

import ua.alisasira.rest.dto.AccountDto;
import ua.alisasira.rest.facade.TransactionFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/account")
public class AccountController {

    @Autowired
    private TransactionFacade transactionFacade;

    @RequestMapping(value = "/{number}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody AccountDto createTransaction(@PathVariable String number) {
        return transactionFacade.getAccountByNumber(number);
    }
}