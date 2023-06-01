package ua.alisasira.spt.parse;

import org.junit.jupiter.api.Test;
import ua.alisasira.spt.parse.DataContext;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import static ua.alisasira.spt.parse.DataParser.parse;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class DataParserTest {

    @Test
    public void testParseInputData() {
        DataContext context = parse(get("input.txt"));

        assertThat(context.getCities(), hasSize(4));
        assertThat(context.getPaths(), hasSize(2));

        assertThat(context.getCities().getFirst().getConnections(), hasSize(2));
        assertThat(context.getCities().getLast().getName(), equalTo("warszawa"));

        assertThat(context.getPaths().getFirst().getFrom().getName(), equalTo("gdansk"));
        assertThat(context.getPaths().getFirst().getTo().getName(), equalTo("warszawa"));
    }

    public File get(String path) {
        URL resource = Thread.currentThread().getContextClassLoader().getResource(path);
        try {
            return resource != null ? new File(resource.toURI()) : null;
        } catch (URISyntaxException e) {
            return null;
        }
    }
}