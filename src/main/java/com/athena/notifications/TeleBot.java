package com.athena.notifications;

import lombok.SneakyThrows;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;

public class TeleBot extends TelegramLongPollingBot
{
    SendMessage sendMessage = new SendMessage();
    private String token;
    private String username;
    private String adminID;

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
        return;
    }

    public void sendReport(String report)
    {
        sendMessage.setChatId(this.adminID);
        sendMessage.setText(report);
        try{execute(sendMessage);}
        catch (TelegramApiException e) {System.out.println("Error sending bot message");}
    }
}
