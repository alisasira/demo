package ua.alisasira.consumer;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import ua.alisasira.bean.MessageBean;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

@Component
public class MessageConsumer {

    private final List<MessageListener> listeners = new CopyOnWriteArrayList<>();

    @Bean
    public Consumer<String> processMessage() {
        return event -> {
            try {
                MessageBean message = MessageBean.fromJson(event, MessageBean.class);

                if(message != null) {
                    listeners.forEach(it -> it.onMessage(message));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

    public void register(MessageListener listener) {
        listeners.add(listener);
    }
}