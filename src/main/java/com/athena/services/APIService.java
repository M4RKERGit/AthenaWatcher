package com.athena.services;

import com.athena.hardware.HWInfo;
import com.athena.linuxtools.Additional;
import com.athena.linuxtools.ServiceControl;
import com.athena.notifications.Notificator;
import com.athena.systeminfo.Configuration;
import com.athena.systeminfo.SystemCtlReport;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;

@Service
@Getter
@Slf4j
public class APIService
{
    private HWInfo hwInfo = new HWInfo();
    private SystemCtlReport sysInfo = new SystemCtlReport();
    private final Configuration confInfo = new Configuration();

    public APIService()
    {
        refreshHWSYSCONF();
    }

    public static ModelAndView responseSlash()
    {
        Notificator.sendBotMsg("API visited");
        log.info("API Visit");
        return new ModelAndView("API.html", new HashMap<>());
    }

    public String responseMailing()
    {
        //String report = hwInfo + "\n\n\n" + systemCtlReport;
        //notificator.sendBotMsg(report);
        //Notificator.emailController.sendMailMessage(report);
        log.info("Sendmail call");
        return "Channels successfully checked!";
    }

    public String responseSystemCtl(String req)
    {
        log.info("Got from client: " + req);
        if (req.contains("reboot")) Additional.restartApplication(); //TODO: Athena Reboot
        if (req.contains("refreshSwitch"))
        {
            confInfo.setRefreshEnabled(!(confInfo.isRefreshEnabled()));
            return "refresh " + confInfo.isRefreshEnabled();
        }
        String servName, cmdType;
        if (req.contains("+"))
        {
            servName = req.split("\\+")[0];
            cmdType = req.split("\\+")[1].replaceAll("=", "");
        }
        else
        {
            servName = req.split(" ")[0];
            cmdType = req.split(" ")[1];
        }
        log.info(String.format("Called to execute (%s %s)", servName, cmdType));
        if (ServiceControl.servAction(servName, cmdType))
        {
            refreshHWSYSCONF();
            return String.format("Success (%s %s)", servName, cmdType);
        }
        else return "Failure";
    }

    @Scheduled(fixedDelayString = "${api.refreshRate}")
    public void refreshHWSYSCONF()
    {
        if (!confInfo.isRefreshEnabled()) return;
        if (hwInfo != null) hwInfo.refresh();
        else hwInfo = new HWInfo();
        if (sysInfo != null) sysInfo.refresh();
        else sysInfo = new SystemCtlReport();
    }
}
