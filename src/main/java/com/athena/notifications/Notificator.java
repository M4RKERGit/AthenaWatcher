package com.athena.notifications;

public class Notificator
{
    static public EmailController emailController;
    static public TeleBotController teleBotController;
    public Notificator()
    {
        emailController = new EmailController();
        teleBotController = new TeleBotController();
        //teleBotController.bot.sendReport("Controllers launched");
    }
    public void sendBotMsg(String text)
    {
        teleBotController.bot.sendReport(text);
    }
}
