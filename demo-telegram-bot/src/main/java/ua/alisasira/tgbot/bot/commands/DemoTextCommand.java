package ua.alisasira.tgbot.bot.commands;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ua.alisasira.tgbot.constants.TextCommands;

@Component
public class DemoTextCommand extends BaseTextCommand{
    public DemoTextCommand(@Value(TextCommands.DEMO_TEXT_COMMAND) String commandIdentifier,
                           @Value("") String description) {
        super(commandIdentifier, description);
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] strings) {
        var sendMessage = SendMessage.builder()
                .text("I'm reply markup response")
                .chatId(message.getChatId())
                .build();
        try {
            absSender.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
