package ua.alisasira.rest.service;

import ua.alisasira.rest.entity.Account;
import ua.alisasira.rest.entity.Category;
import ua.alisasira.rest.entity.Transaction;

public interface TransactionService {

    Transaction createTransaction(Transaction transaction);

    Account getAccountByNumber(String accountNumber);

    Category getCategoryByName(String name);
}