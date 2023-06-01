package ua.alisasira.rest.controller;

import ua.alisasira.rest.FinancialApp;
import ua.alisasira.rest.HttpApiProvider;
import ua.alisasira.rest.dto.AccountDto;
import ua.alisasira.rest.dto.TransactionDto;
import org.jsoup.Connection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.HashMap;

import static ua.alisasira.rest.HttpApiProvider.TRANSACTION_URL;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = FinancialApp.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles(value = {"test"})
public class TransactionControllerTest {

    private static final String SERVER_URL = "http://localhost:8081";

    private static final String FIRST_ACCOUNT_NUMBER = "11111111";
    private static final String SECOND_ACCOUNT_NUMBER = "22222222";

    private HttpApiProvider provider;

    @BeforeEach
    public void setup() {
        provider = new HttpApiProvider(SERVER_URL);
    }

    @Test
    public void testCreateTransaction() throws IOException {

        Long AMOUNT = 250L;

        AccountDto fromAccountBefore = provider.getAccount(FIRST_ACCOUNT_NUMBER);
        AccountDto toAccountBefore = provider.getAccount(SECOND_ACCOUNT_NUMBER);

        TransactionDto input = new TransactionDto();
        input.setFromAccountNumber(FIRST_ACCOUNT_NUMBER);
        input.setToAccountNumber(SECOND_ACCOUNT_NUMBER);
        input.setAmount(AMOUNT);
        input.setCategoryName("salary");

        provider.createTransaction(input);

        {
            AccountDto fromAccountAfter = provider.getAccount(FIRST_ACCOUNT_NUMBER);
            assertThat(fromAccountAfter.getBalance(), equalTo(fromAccountBefore.getBalance() - AMOUNT));
        }

        {
            AccountDto toAccountAfter = provider.getAccount(SECOND_ACCOUNT_NUMBER);
            assertThat(toAccountAfter.getBalance(), equalTo(toAccountBefore.getBalance() + AMOUNT));
        }
    }

    @Test
    public void testInputAccountValidation() throws Exception {
        TransactionDto input = new TransactionDto();
        input.setFromAccountNumber("10101010");
        input.setToAccountNumber(SECOND_ACCOUNT_NUMBER);
        input.setAmount(250L);
        input.setCategoryName("salary");

        Connection.Response response = provider.post(TRANSACTION_URL, new HashMap<>(), input.toJson());
        assertThat(response.statusCode(), equalTo(HttpStatus.BAD_REQUEST.value()));
    }
}