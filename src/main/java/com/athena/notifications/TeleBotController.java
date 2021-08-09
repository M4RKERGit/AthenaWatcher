package com.athena.notifications;

import com.athena.linuxtools.Logger;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class TeleBotController
{
    TeleBot bot = new TeleBot();
    private Logger logger = new Logger("[BCT]");

    public TeleBotController()
    {
        TelegramBotsApi botsApi = null;
        try
        {
            botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(bot);
        }
        catch (TelegramApiException e)
        {
            e.printStackTrace();
        }
    }
}
