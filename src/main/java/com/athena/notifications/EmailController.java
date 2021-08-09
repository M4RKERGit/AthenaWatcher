package com.athena.notifications;

import com.athena.linuxtools.Logger;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class EmailController
{
    private String SmtpServer, login, password, address;
    private JavaMailSender mailSender;
    private Logger logger = new Logger("[EML]");
    public EmailController()
    {
        FileReader fr = null;
        try
        {
            fr = new FileReader("mail.txt");
        }
        catch (Exception e){logger.createLog("Error reading mail configuration file");}
        BufferedReader reader = new BufferedReader(fr);

        try
        {
            System.out.println(SmtpServer = reader.readLine());
            System.out.println(login = reader.readLine());
            System.out.println(password = reader.readLine());
            System.out.println(address = reader.readLine());
            mailSender = getJavaMailSender(SmtpServer, login, password);
        }
        catch (IOException e) {logger.createLog("Error creating mailSender");}
    }

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
        message.setFrom(this.login);
        message.setTo(this.address);
        message.setText(messageString);
        message.setSubject("System report");
        try
        {
            this.mailSender.send(message);
            logger.createLog("Email sent");
        }
        catch (Exception e)
        {
            logger.createLog("Mailing error: " + e.getMessage());
        }
    }
}
