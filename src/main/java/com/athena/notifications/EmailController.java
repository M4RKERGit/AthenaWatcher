package com.athena.notifications;

import com.athena.AthenaSettings;
import com.athena.linuxtools.Logger;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

public class EmailController
{
    private final String adminMail = AthenaSettings.Mail.adminMail;
    private final String mailbox = AthenaSettings.Mail.mailbox;
    private final String password = AthenaSettings.Mail.password;
    private final String SmtpServer = AthenaSettings.Mail.SMTP_Server;

    private final JavaMailSender mailSender;
    private final Logger logger = new Logger("[EML]");

    public EmailController() {mailSender = getJavaMailSender(SmtpServer, mailbox, password);}

    private JavaMailSender getJavaMailSender(String SmtpServer, String login, String password)
    {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(SmtpServer);
        mailSender.setPort(465);

        mailSender.setUsername(login);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtps");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }

    public void sendMailMessage(String messageString)
    {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(this.mailbox);
        message.setTo(this.adminMail);
        message.setText(messageString);
        message.setSubject("System report");
        try
        {
            this.mailSender.send(message);
            logger.createLog("Email sent");
        }
        catch (Exception e) {logger.createLog("Mailing error: " + e.getMessage());}
    }
}
