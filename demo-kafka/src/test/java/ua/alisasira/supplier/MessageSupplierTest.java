package ua.alisasira.supplier;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ua.alisasira.BaseTestCase;
import ua.alisasira.bean.MessageBean;

import java.util.List;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class MessageSupplierTest extends BaseTestCase {

    @Autowired
    private MessageSupplier supplier;
    @Test
    public void testCommonSupplierFlow() {
        messageConsumer.discard();

        String TEXT = "some text";
        supplier.send(MessageBean.build(TEXT));

        List<MessageBean> messages = messageConsumer.consume().getMessages();

        assertThat(messages, hasSize(1));
        assertThat(messages.get(0).getMessage(), equalTo(TEXT));
    }
}