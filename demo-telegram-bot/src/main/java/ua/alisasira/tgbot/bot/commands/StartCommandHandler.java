package ua.alisasira.tgbot.bot.commands;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ua.alisasira.tgbot.constants.Actions;

import java.util.List;

@Component
public class StartCommandHandler extends BotCommand {
    private String startSecret;

    public StartCommandHandler(@Value("start") String commandIdentifier,
                               @Value("") String description,
                               @Value("${bot.start-secret}") String secret) {
        super(commandIdentifier, description);
        this.startSecret = secret;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        if(strings.length == 0){
            return;
        }
        var secret = strings[0];
        if(!startSecret.equals(secret)){
            return;
        }
        try {
            var replyMarkup = InlineKeyboardMarkup.builder()
                    .keyboardRow(List.of(
                            InlineKeyboardButton.builder()
                                    .text("I'm link")
                                    .url("https://www.google.com.ua/maps/@50.4851493,30.4721233,14z?hl=ru")
                                    .build(),
                            InlineKeyboardButton.builder()
                                    .text("I'm callback button")
                                    .callbackData(Actions.SOME_ACTION)
                                    .build()
                    ))
                    .build();
            var sendMessage = SendMessage.builder()
                    .chatId(chat.getId())
                    .text("Hi")
                    .replyMarkup(replyMarkup)
                    .build();
            absSender.execute(sendMessage);
            //absSender.execute(new SendMessage(chat.getId().toString(), "Hello"));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
