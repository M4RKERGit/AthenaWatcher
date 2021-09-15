package com.athena.notifications;

import com.athena.linuxtools.Logger;
import lombok.Getter;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Service
public class TeleBotController
{
    @Getter
    private final TeleBot bot = new TeleBot();
    private final Logger logger = new Logger("[BCT]");

    public TeleBotController()
    {
        TelegramBotsApi botsApi;
        try
        {
            botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(bot);
        }
        catch (TelegramApiException e) {logger.createLog("Error creating Telegram Bot");}
    }
}
