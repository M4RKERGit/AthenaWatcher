package com.athena.notifications;

import com.athena.linuxtools.Logger;
import lombok.Getter;
import org.springframework.stereotype.Service;

@Service
public class Notificator
{
    @Getter
    private static EmailController emailController;
    @Getter
    private static final Logger logger = new Logger("[NOT]");

    static
    {
        //emailController = new EmailController();
        logger.createLog("Created notification controllers");
        TeleBotController.getBot().sendReport("Controllers launched");
    }

    public static void sendBotMsg(String text) {TeleBotController.getBot().sendReport(text);}
}
