package com.athena.notifications;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class Notificator
{
    @Getter
    private static EmailController emailController;
    static
    {
        //emailController = new EmailController();
        log.info("Created notification controllers");
        TeleBotController.getBot().sendReport("Controllers launched");
    }

    public static void sendBotMsg(String text) {TeleBotController.getBot().sendReport(text);}
}
