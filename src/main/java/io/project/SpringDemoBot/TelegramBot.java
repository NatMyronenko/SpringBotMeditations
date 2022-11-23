package io.project.SpringDemoBot;

import com.vdurmont.emoji.EmojiParser;
import io.project.SpringDemoBot.model.User;
import io.project.SpringDemoBot.model.UserRepository;
import io.project.SpringDemoBot.service.TelegramService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Chat;
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
                case "/meditation1":
                    telegramService.sendAudio(chatId, "music/meditation1.mp3");
                    break;
                case "/meditation2":
                    telegramService.sendAudio(chatId, "music/meditation2.mp3");
                    break;
                case "/meditation3":
                    telegramService.sendAudio(chatId, "music/meditation3.mp3");
                   break;
//                case "/meditation4":
//                    telegramService.sendAudio(chatId, "music/meditation4.mp3");
//                    break;
//                case "/meditation5":
//                    telegramService.sendAudio(chatId, "music/meditation5.mp3");
//                    break;
//                case "/meditation6":
//                    telegramService.sendAudio(chatId, "music/meditation6.mp3");
//                    break;
//                case "/meditation7":
//                    telegramService.sendAudio(chatId, "music/meditation7.mp3");
//                    break;
//                case "/meditation8":
//                    telegramService.sendAudio(chatId, "music/meditation8.mp3");
//                    break;
//                case "/meditation9":
//                    telegramService.sendAudio(chatId, "music/meditation9.mp3");
//                    break;
//
//                case "/meditation10":
//                    telegramService.sendAudio(chatId, "music/meditation10.mp3");
//                    break;
                case "/list_meditations":
                    update.getMessage();
                    listMeditation(chatId, update.getMessage().getChat());
                    break;
                default:
                    telegramService.sendMessage(chatId, "Sorry, command was not recognized");

            }
        }
    }

    private void registerUser(Message message) {
        if (userRepository.findById(message.getChatId()).isEmpty()) {
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
        String answer = EmojiParser.parseToUnicode("Привет " + name + ", приятно познакомиться! " +
                "\nнажми /list_meditations  и выбери нужную медитацию" + ":leaves:");
        log.info("Replied to user: " + name);
        telegramService.sendMessage(chatId, answer);

    }

    private void listMeditation(long chatId, Chat name) {
        String answer = EmojiParser.parseToUnicode("Хочу обратитть Ваше внимание" +
                "\nдля загрузки медитации может понадобиться пара минут." +
                "\nВыбери пожалуйста нужную медитацию: " +
                "\n/meditation1 'Встраивание нового состояния'" +
                "\n/meditation2  'Устранение тревог'" +
                "\n/meditation3 'Возврат ценности'" +
                "\n/meditation4 'Возврат ценности 2'" +
                "\n/meditation5 'Избавление от запретов'" +
                "\n/meditation6 'Закрепление уверенности'" +
                "\n/meditation7 'Избавление от чувства стыда. Возврат собственного Я'" +
                "\n/meditation8 'Практика на маму. Мое новое Я'" +
                "\n/meditation9 'Практика на образ папы'" +
                "\n/meditation10 'Снятие стресса : Ручей'" +
                "\n Приятного прослушивания!");
        telegramService.sendMessage(chatId, answer);
    }

}
