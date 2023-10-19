package ua.alisasira.consumer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ua.alisasira.BaseTestCase;
import ua.alisasira.bean.MessageBean;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class MessageConsumerTest extends BaseTestCase {

    @Autowired
    private MessageConsumer consumer;

    @Test
    public void testCommonConsumerFlow() throws Exception {
        String TEXT = "some consumer text";

        QueueMessageListener listener = new QueueMessageListener();
        consumer.register(listener);

        messageProducer.send(MessageBean.build(TEXT));

        MessageBean message = listener.getMessages().poll(3, TimeUnit.SECONDS);
        assertThat(message, is(notNullValue()));
        assertThat(message.getMessage(), equalTo(TEXT));
    }

    class QueueMessageListener implements MessageListener {

        private BlockingQueue<MessageBean> messages = new LinkedBlockingQueue<>();

        @Override
        public void onMessage(MessageBean message) {
            messages.offer(message);
        }

        public BlockingQueue<MessageBean> getMessages() {
            return messages;
        }
    }
}