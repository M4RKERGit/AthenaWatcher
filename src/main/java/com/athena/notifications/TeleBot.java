package com.athena.notifications;

import com.athena.linuxtools.Logger;
import lombok.SneakyThrows;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.nio.file.Files;
import java.nio.file.Path;

public class TeleBot extends TelegramLongPollingBot
{
    SendMessage sendMessage = new SendMessage();
    private String token;
    private String username;
    private String adminID;
    private final Logger logger = new Logger("[BOT]");

    @SneakyThrows
    @Override
    public String getBotToken()
    {
        this.token = Files.readAllLines(Path.of("bot.txt")).get(0);
        return this.token;
    }

    @SneakyThrows
    @Override
    public String getBotUsername()
    {
        this.username = Files.readAllLines(Path.of("bot.txt")).get(1);
        this.adminID = Files.readAllLines(Path.of("bot.txt")).get(2);
        return this.username;
    }

    @Override
    public void onUpdateReceived(Update update)
    {
        logger.createLog("Got useless update:" + update.getMessage().getFrom().getUserName() + ' ' + update.getMessage().getText());
    }

    public void sendReport(String report)
    {
        logger.createLog("Token: " + this.token);
        logger.createLog("Username: " + this.username);
        logger.createLog("Admin ID: " + this.adminID);
        sendMessage.setChatId(this.adminID);
        sendMessage.setText(report);
        try{execute(sendMessage);}
        catch (TelegramApiException e) {logger.createLog("Error sending message");}
    }
}
