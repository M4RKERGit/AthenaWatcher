package com.athena;

import com.athena.linuxtools.Logger;
import lombok.Getter;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class AthenaSettings
{
    private static final Logger logger = new Logger("[PRP]");
    private static final Properties properties = new java.util.Properties();
    @Getter
    private static String serverPort;

    static
    {
        try
        {
            properties.load(new FileReader("config.properties"));
            serverPort = properties.getProperty("server.port", "8080");
        }
        catch (IOException e) {logger.createLog("Error reading config.properties file");}
    }

    @SuppressWarnings("TextBlockMigration")
    public static void printSettingAfterLoading()
    {
        System.out.printf("\nBot settings:\n" +
                        "\tAdmin ID: %s\n" +
                        "\tToken: %s\n" +
                        "\tUsername: %s\n",
                        Bot.adminID, Bot.token, Bot.username);

        System.out.printf("\nMail settings:\n" +
                        "\tAdmin mail: %s\n" +
                        "\tToken: %s\n" +
                        "\tUsername: %s\n" +
                        "\tSMTP server: %s\n",
                        Mail.adminMail, Mail.mailbox, Mail.password, Mail.SMTP_Server);

        System.out.printf("\nServices settings:\n" +
                        "\tFirst service: %s\n" +
                        "\tSecond service: %s\n" +
                        "\tThird service: %s\n",
                        Services.I, Services.II, Services.III);
    }

    @Getter
    public static class Bot
    {
        public static final String adminID; //TODO: unable to get not-public fields
        public static final String token;
        public static final String username;

        static
        {
            adminID = properties.getProperty("bot.adminID");
            token = properties.getProperty("bot.token");
            username = properties.getProperty("bot.username");
        }
    }

    @Getter
    public static class Mail
    {
        public static final String adminMail;
        public static final String mailbox;
        public static final String password;
        public static final String SMTP_Server;

        static
        {
            adminMail = properties.getProperty("mail.adminMail");
            mailbox = properties.getProperty("mail.mailbox");
            password = properties.getProperty("mail.password");
            SMTP_Server = properties.getProperty("mail.SMTP_Server");
        }
    }

    @Getter
    public static class Services
    {
        public static final String I;
        public static final String II;
        public static final String III;

        static
        {
            I = properties.getProperty("services.I");
            II = properties.getProperty("services.II");
            III = properties.getProperty("services.III");
        }
    }
}
