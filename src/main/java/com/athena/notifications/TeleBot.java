package com.athena.notifications;

import com.athena.AthenaSettings;
import com.athena.linuxtools.Additional;
import com.athena.linuxtools.Logger;
import com.athena.services.TerminalService;
import lombok.SneakyThrows;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TeleBot extends TelegramLongPollingBot
{
    SendMessage sendMessage = new SendMessage();
    private final String token = AthenaSettings.Bot.token;
    private final String username = AthenaSettings.Bot.username;
    private final String adminID = AthenaSettings.Bot.adminID;
    private final Logger logger = new Logger("[BOT]");
    private final TerminalService service = new TerminalService();

    @SneakyThrows
    @Override
    public String getBotToken() {return this.token;}

    @SneakyThrows
    @Override
    public String getBotUsername() {return this.username;}

    @Override
    public void onUpdateReceived(Update update)
    {
        Message message = update.getMessage();
        String id = message.getChatId().toString();
        if (!id.equals(adminID))
        {
            logger.createLog(String.format(
                    "Got non-admin update '%s' from %s at %s",
                    message.getText(), message.getFrom().getUserName(), Additional.getCurrentTime()));
            return;
        }
        String stdout = service.executeAndResponse(message.getText());
        sendMessage.setText(stdout);
        sendMessage.setChatId(id);
        try {execute(sendMessage);}
        catch (TelegramApiException e) {e.printStackTrace();}
    }

    public void sendReport(String report)
    {
        sendMessage.setChatId(this.adminID);
        sendMessage.setText(report);
        try{execute(sendMessage);}
        catch (TelegramApiException e) {logger.createLog("Error sending message");}
    }
}
