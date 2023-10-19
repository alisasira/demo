package ua.alisasira.supplier;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import ua.alisasira.bean.MessageBean;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Supplier;

@Component
public class MessageSupplier {

    private final BlockingQueue<String> messages = new LinkedBlockingQueue<>();
    @Bean
    public Supplier<String> produceMessage() {
        return () -> messages.poll();
    }

    public void send(MessageBean message) {
        messages.offer(message.toJson());
    }
}