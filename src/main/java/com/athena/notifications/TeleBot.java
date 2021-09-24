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
    private final int DIVIDER = 4000;
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
            String log = String.format(
                    "Got non-admin update '%s' from %s at %s",
                    message.getText(), message.getFrom().getUserName(), Additional.getCurrentTime());
            logger.createLog(log);
            execMessage("You don't have admin access, this incident will be reported", id);
            execMessage(log, adminID);
            return;
        }
        String stdout = service.executeAndResponse(message.getText(), true);
        execMessage(stdout, id);
    }

    public void sendReport(String report)
    {
        execMessage(report, adminID);
    }

    public void execMessage(String text, String id)
    {
        if (text.length() == 0) return;
        if (text.length() > DIVIDER)
        {
            int count = text.length()/DIVIDER;
            for (int i = 0; i < count; i++)
            {
                String buf = text.substring(i * DIVIDER, (i+1) * DIVIDER);
                sendMessage.setText(buf);
                sendMessage.setChatId(id);
            }
        }
        else
        {
            sendMessage.setText(text);
            sendMessage.setChatId(id);
        }
        try {execute(sendMessage);}
        catch (TelegramApiException e) {e.printStackTrace();}
    }
}
