package ua.alisasira.rest.facade;

import ua.alisasira.rest.dto.AccountDto;
import ua.alisasira.rest.dto.TransactionDto;
import org.springframework.transaction.annotation.Transactional;

public interface TransactionFacade {

    @Transactional
    TransactionDto createTransaction(TransactionDto transaction);

    AccountDto getAccountByNumber(String number);
}