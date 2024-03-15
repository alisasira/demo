package ua.alisasira.validation.controller;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import ua.alisasira.validation.entity.BaseEntity;
import ua.alisasira.validation.entity.User;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.jsoup.Connection.Method.*;
import static org.jsoup.Connection.Method.DELETE;

public class HttpApiProvider {
    protected static final String OS_X_USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_5) " +
            "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.104 Safari/537.36";
    private final String serverUrl;

    public HttpApiProvider(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public static final String USER_URL = "/api/user";
    public static final String USER_SEARCH_URL = "/api/user/search";

    public ResponseWrapper create(User user) throws IOException {
        return post(USER_URL, user);
    }

    public ResponseWrapper update(User user) throws IOException {
        return put(USER_URL, user);
    }

    public int delete(Long id) throws IOException {
        return delete(USER_URL + "/" + id);

    }

    public ResponseWrapper search(String from, String to) throws IOException {
        Map<String, String> data = new HashMap<>();
        data.put("from", from);
        data.put("to", to);

        return get(USER_SEARCH_URL, new HashMap<>(), data);
    }

    public ResponseWrapper get(String url, Map<String, String> headers, Map<String, String> data) throws IOException {
        if (!headers.containsKey("Content-Type")) {
            headers.put("Content-Type", "application/json");
        }

        Connection.Response response = Jsoup.connect(serverUrl + url)
                .userAgent(OS_X_USER_AGENT)
                .timeout(30000)
                .ignoreContentType(true)
                .headers(headers)
                .data(data)
                .method(GET)
                .execute();

        return new ResponseWrapper(response);
    }

    public ResponseWrapper post(String url, BaseEntity entity) throws IOException {
        return execute(POST, url, new HashMap<>(), entity.toJson());
    }

    public ResponseWrapper put(String url, BaseEntity entity) throws IOException {
        return execute(PUT, url, new HashMap<>(), entity.toJson());
    }

    public int delete(String url) throws IOException {
        Connection.Response result = Jsoup.connect(serverUrl + url)
                .ignoreContentType(true)
                .ignoreHttpErrors(true)
                .method(DELETE).execute();

        return result.statusCode();
    }

    public ResponseWrapper execute(Connection.Method method, String url, Map<String, String> headers, String body) throws IOException {
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
                .method(method)
                .execute();

        return new ResponseWrapper(response);
    }

    static class ResponseWrapper {
        Connection.Response response;

        public ResponseWrapper(Connection.Response response) {
            this.response = response;
        }

        int statusCode() {
            return response.statusCode();
        }

        <E extends BaseEntity> E deserialize(Class<E> resultClass) {
            return BaseEntity.fromJson(response.body(), resultClass);
        }

        List<User> deserializeToList() {
            return BaseEntity.fromJsonArray(response.body());
        }
    }
}
