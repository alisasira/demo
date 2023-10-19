package ua.alisasira;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class EmbeddedKafkaConsumer<W> {

    private static final long WAITING_TIME_MILLIS = 1000;
    private static final long INNER_WAITING_TIME_MILLIS = 1000;

    private final Consumer<String, String> consumer;

    private final ConsumerWrapper<W> wrapper;

    public EmbeddedKafkaConsumer(String destination, EmbeddedKafkaBroker broker, ConsumerWrapper<W> wrapper) {
        this.wrapper = wrapper;

        String clientIdSuffix = destination + "-group";

        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps(clientIdSuffix, "true", broker);
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(ConsumerConfig.CLIENT_ID_CONFIG, "test");
        consumerProps.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 1000);
        DefaultKafkaConsumerFactory<String, String> cf = new DefaultKafkaConsumerFactory<>(consumerProps);

        consumer = cf.createConsumer(clientIdSuffix);
        consumer.subscribe(Collections.singleton(destination));
    }

    public W consume() {
        return consume(WAITING_TIME_MILLIS);
    }

    public W consume(long waitingTime) {
        List<String> recordValues = new ArrayList<>();
        ConsumerRecords<String, String> records = consumer.poll(Duration.of(waitingTime, ChronoUnit.MILLIS));
        while (!records.isEmpty()) {
            records.forEach(r -> {
                recordValues.add(r.value());
            });
            records = consumer.poll(Duration.of(INNER_WAITING_TIME_MILLIS + 500, ChronoUnit.MILLIS));
        }

        return wrapper.wrap(recordValues);
    }

    public void close() {
        consumer.close();
    }

    public int discard() {
        ConsumerRecords<String, String> records = consumer.poll(Duration.of(100, ChronoUnit.MILLIS));
        return records.count();
    }

    public interface ConsumerWrapper<W> {
        W wrap(List<String> input);
    }
}