package ua.alisasira.rest.facade;

import ua.alisasira.rest.entity.Account;
import ua.alisasira.rest.entity.CategoryType;
import ua.alisasira.rest.entity.Transaction;
import ua.alisasira.rest.facade.model.ExportRowModel;
import ua.alisasira.rest.service.TransactionService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
public class ExportFacadeImpl implements ExportFacade {

    @Autowired
    private TransactionService transactionService;

    @Override
    public StringWriter exportAccountTransaction(String accountNumber) throws IOException {
        Account account = transactionService.getAccountByNumber(accountNumber);
        if (account == null) {
            throw new IllegalStateException("Export account not found");
        }

        List<ExportRowModel> rows = new ArrayList<>();
        rows.addAll(account.getFromTransactions().stream().map(it -> toModel(it, CategoryType.OUTCOME)).toList());
        rows.addAll(account.getToTransactions().stream().map(it -> toModel(it, CategoryType.INCOME)).toList());

        rows.sort(Comparator.comparing(ExportRowModel::getDate));

        StringWriter sw = new StringWriter();

        String[] HEADERS = {"Date", "From Account", "Sender", "To Account", "Recipient", "Category", "Type", "Amount"};
        CSVFormat csvFormat = CSVFormat.EXCEL.builder()
                .setHeader(HEADERS)
                .setDelimiter(';')
                .setRecordSeparator('\n')
                .build();

        try (final CSVPrinter printer = new CSVPrinter(sw, csvFormat)) {
            rows.forEach(r -> {
                try {
                    printer.printRecord(r.getDate(), r.getFromAccountNumber(), r.getFromUser(), r.getToAccountNumber(), r.getToUser(), r.getCategoryName(), r.getCategoryType(), r.getAmount());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
        return sw;
    }

    private ExportRowModel toModel(Transaction transaction, CategoryType type) {
        ExportRowModel row = new ExportRowModel();
        row.setDate(transaction.getCreated());
        row.setFromAccountNumber(transaction.getFromAccount().getNumber());
        row.setFromUser(transaction.getFromAccount().getUser().getFullName());
        row.setToAccountNumber(transaction.getToAccount().getNumber());
        row.setToUser(transaction.getToAccount().getUser().getFullName());
        row.setCategoryType(type);
        row.setCategoryName(transaction.getCategory().getName());
        row.setAmount(transaction.getAmount());
        return row;
    }
}