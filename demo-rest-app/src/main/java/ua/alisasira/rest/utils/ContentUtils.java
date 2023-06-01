package ua.alisasira.rest.utils;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

public class ContentUtils {

    public static File get(String path) {
        URL resource = Thread.currentThread().getContextClassLoader().getResource(path);
        try {
            return resource != null ? new File(resource.toURI()) : null;
        } catch (URISyntaxException e) {
            return null;
        }
    }
}