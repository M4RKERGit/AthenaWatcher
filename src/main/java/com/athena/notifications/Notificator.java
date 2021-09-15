package com.athena.notifications;

import com.athena.linuxtools.Logger;
import org.springframework.stereotype.Service;

@Service
public class Notificator
{
    static public EmailController emailController;
    static public TeleBotController teleBotController;
    private static final Logger logger = new Logger("[NOT]");

    public Notificator()
    {
        //emailController = new EmailController();
        teleBotController = new TeleBotController();
        logger.createLog("Created notification controllers");
        teleBotController.getBot().sendReport("Controllers launched");
    }
    public void sendBotMsg(String text) {teleBotController.getBot().sendReport(text);}
}
