package ua.alisasira;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.util.concurrent.ListenableFuture;
import ua.alisasira.bean.BaseBean;

import java.util.Map;

public class EmbeddedKafkaProducer<T extends BaseBean> {

    private final KafkaTemplate<String, String> template;

    public EmbeddedKafkaProducer(String destination, EmbeddedKafkaBroker broker) {
        Map<String, Object> properties = Map.of(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, broker.getBrokersAsString(),
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class
        );

        DefaultKafkaProducerFactory<String, String> pf = new DefaultKafkaProducerFactory(properties);
        template = new KafkaTemplate<>(pf, true);
        template.setDefaultTopic(destination);
    }

    public ListenableFuture<SendResult<String, String>> send(T event) {
        return template.sendDefault(event.toJson());
    }
}