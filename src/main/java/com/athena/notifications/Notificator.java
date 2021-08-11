package com.athena.notifications;

import com.athena.linuxtools.Logger;

public class Notificator
{
    static public EmailController emailController;
    static public TeleBotController teleBotController;
    private final Logger logger = new Logger("[NOT]");
    public Notificator()
    {
        //emailController = new EmailController();
        teleBotController = new TeleBotController();
        logger.createLog("Created notification controllers");
        //teleBotController.bot.sendReport("Controllers launched");
    }
    public void sendBotMsg(String text)
    {
        teleBotController.bot.sendReport(text);
    }
}
