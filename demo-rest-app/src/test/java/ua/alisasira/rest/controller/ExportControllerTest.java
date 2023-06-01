package ua.alisasira.rest.controller;

import ua.alisasira.rest.FinancialApp;
import ua.alisasira.rest.HttpApiProvider;
import ua.alisasira.rest.utils.ContentUtils;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.*;

import static ua.alisasira.rest.HttpApiProvider.EXPORT_URL;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = FinancialApp.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles(value = {"test"})
public class ExportControllerTest {

    private static final String SERVER_URL = "http://localhost:8081";
    private HttpApiProvider provider;

    private static final String FOURTH_ACCOUNT_NUMBER = "44444444";

    @BeforeEach
    public void setup() {
        provider = new HttpApiProvider(SERVER_URL);
    }

    @Test
    public void testExportAccount() throws Exception {
        String body = provider.get(String.format(EXPORT_URL, FOURTH_ACCOUNT_NUMBER));

        StringWriter expectedContent = new StringWriter();
        IOUtils.copy(new FileReader(ContentUtils.get("assets/test.csv")), expectedContent);

        assertThat(body.trim(), equalTo(expectedContent.toString().trim()));
    }
}