package io.project.SpringDemoBot.service;

import io.project.SpringDemoBot.sender.BotSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * This service allows to communicate with Telegram API
 */
@Slf4j
@Component
public class TelegramService {

    private final BotSender botSender;

    public TelegramService(BotSender botSender) {
        this.botSender = botSender;
    }

    public void sendMessage(Long chatId, String text) {
        sendMessage(chatId, text, null);
    }

    public void sendMessage(Long chatId, String text, ReplyKeyboard replyKeyboard) {
        SendMessage sendMessage = SendMessage
                .builder()
                .text(text)
                .chatId(chatId.toString())
                //Other possible parse modes: MARKDOWNV2, MARKDOWN, which allows to make text bold, and all other things
                .parseMode(ParseMode.HTML)
                .replyMarkup(replyKeyboard)
                .build();

        try {
            botSender.execute(sendMessage);
        } catch (Exception e) {
            log.error("Exception: ", e);
        }
    }

    public void sendAudio(Long chatId, String path) {
        InputFile file = new InputFile(new File(path));

        SendAudio sendAudio = SendAudio
                .builder()
                .audio(file)
                .chatId(chatId.toString())
                .build();

        try {
            botSender.execute(sendAudio);
        } catch (Exception e) {
            log.error("Exception: ", e);
        }
    }

    public void setCommands() {
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start", "get a welcome message"));
        listOfCommands.add(new BotCommand("/help", "info, how to use this bot"));
        listOfCommands.add(new BotCommand("/list_meditations", "choose meditation: "));
        listOfCommands.add(new BotCommand("/audio", "get audio file"));

        SetMyCommands setMyCommands = new SetMyCommands();
        setMyCommands.setCommands(listOfCommands);

        try {
            botSender.execute(setMyCommands);
        } catch (TelegramApiException e) {
            log.error("Error setting bots command list" + e.getMessage());
        }
    }
}
