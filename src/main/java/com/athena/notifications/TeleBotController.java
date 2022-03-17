package com.athena.notifications;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Service
@Slf4j
public class TeleBotController
{
    @Getter
    private static TeleBot bot;
    private static TelegramBotsApi botsApi;

    static
    {
        try
        {
            bot = new TeleBot();
            botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(bot);
        }
        catch (TelegramApiException e) {e.printStackTrace();}
        log.info("Bot registered!");
    }
}
