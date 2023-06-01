package ua.alisasira.rest.controller;

import ua.alisasira.rest.dto.TransactionDto;
import ua.alisasira.rest.facade.TransactionFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/transaction")
public class TransactionController {

    @Autowired
    private TransactionFacade transactionFacade;

    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody TransactionDto createTransaction(@RequestBody TransactionDto transaction) {
        return transactionFacade.createTransaction(transaction);
    }
}