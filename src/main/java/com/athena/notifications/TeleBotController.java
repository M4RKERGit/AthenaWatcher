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
    private static TeleBot bot;
    private static final Logger logger = new Logger("[BCT]");
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
        logger.createLog("Bot registered!");
    }
}
