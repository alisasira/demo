package ua.alisasira;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ua.alisasira.bean.MessageBean;
import ua.alisasira.wrapper.MessageBeanWrapper;

import java.util.List;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = KafkaApp.class)
@SpringBootTest(webEnvironment = DEFINED_PORT)
@ActiveProfiles(value = "test")
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
public class BaseTestCase {

    public static final String MESSAGE_TOPIC = "message-stream";

    @Autowired
    private EmbeddedKafkaBroker broker;

    protected EmbeddedKafkaProducer<MessageBean> messageProducer;
    protected EmbeddedKafkaConsumer<MessageBeanWrapper> messageConsumer;

    @BeforeEach
    public void setup() {
        messageConsumer = new EmbeddedKafkaConsumer<>(MESSAGE_TOPIC, broker, input -> {
            List<MessageBean> result = Lists.newArrayList();
            input.forEach(it -> result.add(MessageBean.fromJson(it, MessageBean.class)));
            return new MessageBeanWrapper(result);
        });

        messageProducer = new EmbeddedKafkaProducer<>(MESSAGE_TOPIC, broker);
    }

    @AfterEach
    public void shutdown() {
        messageConsumer.close();
    }
}