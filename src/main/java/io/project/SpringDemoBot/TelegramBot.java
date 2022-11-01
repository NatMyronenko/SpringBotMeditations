package io.project.SpringDemoBot;

import com.vdurmont.emoji.EmojiParser;
import io.project.SpringDemoBot.model.User;
import io.project.SpringDemoBot.model.UserRepository;
import io.project.SpringDemoBot.service.TelegramService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.sql.Timestamp;

@Slf4j
@Component //automatically create an example
public class TelegramBot extends TelegramLongPollingBot {

    @Value("${bot.token}")
    private String botToken;

    @Value("${bot.name}")
    private String botUsername;

    private final UserRepository userRepository;
    private final TelegramService telegramService;

    static final String HELP_TEXT = "This bot is created to demonstrate Spring capabilities\n" +
            "type /mydata to get your data stored";

    public TelegramBot(UserRepository userRepository, TelegramService telegramService) {
        this.userRepository = userRepository;
        this.telegramService = telegramService;
        telegramService.setCommands();
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            switch (messageText) {
                case "/start":
                    registerUser(update.getMessage());
                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    break;
                case "/help":
                    telegramService.sendMessage(chatId, HELP_TEXT);
                    break;
                case "/audio":
                    telegramService.sendAudio(chatId, "music/test.mp3");
                    break;
                default:
                    telegramService.sendMessage(chatId, "Sorry, command was not recognized");

            }
        }
    }

    private void registerUser(Message message) {
        if (userRepository.findById(message.getChatId()).isEmpty()){
            var chatId = message.getChatId();
            var chat = message.getChat();
            User user = new User();
            user.setChatId(chatId);
            user.setFirstName(chat.getFirstName());
            user.setLastName(chat.getLastName());
            user.setUsername(chat.getUserName());
            user.setRegisteredAt(new Timestamp(System.currentTimeMillis()));

            userRepository.save(user);
            log.info("user saved: " + user);
        }
    }

    private void startCommandReceived(long chatId, String name) {
        //String answer = "Hi " + name + " nice to meet you!";
        String answer = EmojiParser.parseToUnicode("Hi " + name + " nice to meet you!" + ":leaves:");
        log.info("Replied to user: " + name);
        telegramService.sendMessage(chatId, answer);

    }

}
