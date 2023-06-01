package ua.alisasira.rest.facade;

import ua.alisasira.rest.dto.AccountDto;
import ua.alisasira.rest.dto.TransactionDto;
import ua.alisasira.rest.entity.Account;
import ua.alisasira.rest.entity.Category;
import ua.alisasira.rest.entity.Transaction;
import ua.alisasira.rest.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TransactionFacadeImpl implements TransactionFacade {

    @Autowired
    private TransactionService transactionService;

    @Override
    public TransactionDto createTransaction(TransactionDto dto) {
        Account from = transactionService.getAccountByNumber(dto.getFromAccountNumber());
        if (from == null) {
            throw new IllegalStateException("Sender's account was not found");
        }

        Account to = transactionService.getAccountByNumber(dto.getToAccountNumber());
        if (to == null) {
            throw new IllegalStateException("Recipient's account was not found");
        }

        Category category = transactionService.getCategoryByName(dto.getCategoryName());
        if (category == null) {
            throw new IllegalStateException("This category was not found");
        }

        if (from.getBalance() < dto.getAmount()) {
            throw new IllegalStateException("Not enough money on account");
        }

        Transaction transaction = new Transaction();
        transaction.setFromAccount(from);
        transaction.setToAccount(to);
        transaction.setCategory(category);
        transaction.setAmount(dto.getAmount());
        transaction.setCreated(new Date());

        return toDto(transactionService.createTransaction(transaction));
    }

    @Override
    public AccountDto getAccountByNumber(String number) {
        return toDto(transactionService.getAccountByNumber(number));
    }

    private TransactionDto toDto(Transaction transaction) {
        TransactionDto dto = new TransactionDto();
        dto.setId(transaction.getId());
        dto.setFromAccountNumber(transaction.getFromAccount().getNumber());
        dto.setToAccountNumber(transaction.getToAccount().getNumber());
        dto.setAmount(transaction.getAmount());
        dto.setDate(transaction.getCreated());
        dto.setCategoryName(transaction.getCategory().getName());
        return dto;
    }

    private AccountDto toDto(Account account) {
        AccountDto dto = new AccountDto();
        dto.setId(account.getId());
        dto.setNumber(account.getNumber());
        dto.setBalance(account.getBalance());
        dto.setUserId(account.getUser().getId());
        return dto;
    }
}