package org.itstep.service;

import lombok.extern.slf4j.Slf4j;
import org.itstep.config.BotConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScope;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    private final BotConfig botConfig;

    @Autowired

    public TelegramBot(BotConfig botConfig) {
        this.botConfig = botConfig;
        //добавляем меню
        List<BotCommand> botCommands = new ArrayList<>();
        //строчными буквами
        botCommands.add(new BotCommand("/start", "Get a welcome message"));
        botCommands.add(new BotCommand("/mydata", "get your data stored"));
        botCommands.add(new BotCommand("/delete", "delete your data"));
        botCommands.add(new BotCommand("/help", "info how to use the bot"));
        botCommands.add(new BotCommand("/settings", "set your preferences"));
       // this.execute(new SetMyCommands(botCommands, new BotCommandScopeDefault(), null);
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()){
            String messageText = update.getMessage().getText();
            System.out.println(messageText);
            long chatId = update.getMessage().getChatId();
            String firstName = update.getMessage().getChat().getFirstName();
            System.out.println(firstName);
            switch (messageText){
                case "/start":
                    start(chatId,firstName);
                    break;
                default:
                    sendMessage(chatId,"Sorry, the command was not recognized");
            }
        }
    }

    private void start(long chatId, String firstName){
        String answer = String.format("Hi, %s!", firstName);
        sendMessage(chatId, answer);
    }

    private void sendMessage(long chatId, String answer) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(answer);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
/*
    private void registerUser(Message message){
        //Существует ли в базе такой пользователь
        if (userRepository.findById(message.getChatId()).isEmpty()){
            User user = new User(message.getChatId(),
                    message.getChat().getFirstName(),
                    message.getChat().getLastName(),
                    message.getChat().getUserName(),
                    new Timestamp(System.currentTimeMillis()));

            userRepository.save(user);
            log.info("user saved: "+user);
        }
    }

 */
} 