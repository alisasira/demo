package ua.alisasira.rest.service;

import ua.alisasira.rest.entity.Account;
import ua.alisasira.rest.entity.Category;
import ua.alisasira.rest.entity.Transaction;
import ua.alisasira.rest.repository.AccountRepository;
import ua.alisasira.rest.repository.CategoryRepository;
import ua.alisasira.rest.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public Transaction createTransaction(Transaction input) {
        Transaction transaction = transactionRepository.save(input);

        Account fromAccount = transaction.getFromAccount();
        fromAccount.setBalance(fromAccount.getBalance() - transaction.getAmount());
        accountRepository.save(fromAccount);

        Account toAccount = transaction.getToAccount();
        toAccount.setBalance(toAccount.getBalance() + transaction.getAmount());
        accountRepository.save(toAccount);

        return transaction;
    }

    @Override
    public Account getAccountByNumber(String accountNumber) {
        return accountRepository.findAccountByNumber(accountNumber);
    }

    @Override
    public Category getCategoryByName(String name) {
        return categoryRepository.findCategoryByName(name);
    }
}