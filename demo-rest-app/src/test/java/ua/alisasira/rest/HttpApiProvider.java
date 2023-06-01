package ua.alisasira.rest;

import ua.alisasira.rest.dto.AccountDto;
import ua.alisasira.rest.dto.BaseDto;
import ua.alisasira.rest.dto.TransactionDto;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.jsoup.Connection.Method.GET;
import static org.jsoup.Connection.Method.POST;

public class HttpApiProvider {

    protected static final String OS_X_USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_5) " +
            "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.104 Safari/537.36";
    private String serverUrl;

    public HttpApiProvider(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public static final String TRANSACTION_URL = "/api/transaction";
    public static final String ACCOUNT_URL = "/api/account";

    public static final String EXPORT_URL = "/api/export/%s.csv";

    public AccountDto getAccount(String number) throws IOException {
        return get(ACCOUNT_URL + "/" + number, AccountDto.class);
    }

    public TransactionDto createTransaction(TransactionDto transaction) throws IOException {
        return post(TRANSACTION_URL, transaction.toJson(), TransactionDto.class);
    }

    public <E extends BaseDto> E get(String url, Class<E> resultClass) throws IOException {
        return BaseDto.fromJson(get(url), resultClass);
    }

    public String get(String url) throws IOException {
        return get(url, new HashMap<>());
    }

    public String get(String url, Map<String, String> headers) throws IOException {
        if (!headers.containsKey("Content-Type")) {
            headers.put("Content-Type", "application/json");
        }

        Connection.Response response = Jsoup.connect(serverUrl + url)
                .userAgent(OS_X_USER_AGENT)
                .timeout(30000)
                .ignoreContentType(true)
                .headers(headers)
                .method(GET)
                .execute();

        return response.body();
    }

    public <E extends BaseDto> E post(String url, String body, Class<E> resultClass) throws IOException {
        return BaseDto.fromJson(post(url, new HashMap<>(), body).body(), resultClass);
    }

    public Connection.Response post(String url, Map<String, String> headers, String body) throws IOException {
        if (!headers.containsKey("Content-Type")) {
            headers.put("Content-Type", "application/json");
        }

        Connection.Response response = Jsoup.connect(serverUrl + url)
                .userAgent(OS_X_USER_AGENT)
                .timeout(30000)
                .ignoreContentType(true)
                .ignoreHttpErrors(true)
                .headers(headers)
                .requestBody(body)
                .maxBodySize(1024 * 1024)
                .method(POST)
                .execute();

        return response;
    }
}