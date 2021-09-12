package com.athena.controllers;

import com.athena.visitors.Visitor;
import com.athena.visitors.VisitsRepository;
import com.athena.hardware.HWInfo;
import com.athena.linuxtools.Additional;
import com.athena.linuxtools.Logger;
import com.athena.linuxtools.ServiceControl;
import com.athena.systeminfo.Configuration;
import com.athena.systeminfo.SystemCtlReport;
import com.athena.notifications.Notificator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api")
public class AthenaAPI
{
    private final VisitsRepository visitsRepository;
    //public static Notificator notificator = new Notificator();    //TODO: включить после тестов
    private static final Logger logger = new Logger("[API]");
    private static HWInfo hwRaw = new HWInfo();
    private static SystemCtlReport sysRaw = new SystemCtlReport();
    private static final Configuration configurationRaw = new Configuration();
    private static String hwInfo;
    private static String systemCtlReport;
    private static String configurationStr;
    private final ObjectMapper JSONMapper;

    public AthenaAPI(VisitsRepository visitsRepository)
    {
        this.JSONMapper = new ObjectMapper();
        JSONMapper.enable(SerializationFeature.INDENT_OUTPUT);
        this.visitsRepository = visitsRepository;
        refreshHWSYSCONF();
    }

    @GetMapping("/")
    public ModelAndView slashIndex()
    {
        Map<String, String> model = new HashMap<>();
        Visitor visit = new Visitor();
        visit.description = String.format("Visited API at %s", Additional.getCurrentTime());
        visitsRepository.save(visit);
        //notificator.sendBotMsg("API visited");
        logger.createLog("API Visit");

        return new ModelAndView("API.html", model);
    }

    @GetMapping("/sendmail")
    public String mailing()
    {
        String report = getHWInfo() + "\n\n\n" + getServInfo();
        //notificator.sendBotMsg(report);
        Notificator.emailController.sendMailMessage(report);
        logger.createLog("Sendmail call");
        return "Channels successfully checked!";
    }

    @GetMapping("/visits")
    public Iterable<Visitor> getVisits() {return visitsRepository.findAll();}

    @RequestMapping(value = "/hwinfo", method = RequestMethod.GET, produces = "application/json")
    public String getHWInfo()
    {
        return hwInfo;
    }

    @RequestMapping(value = "/servinfo", method = RequestMethod.GET, produces = "application/json")
    public String getServInfo()
    {
        return systemCtlReport;
    }

    @RequestMapping(value = "/confinfo", method = RequestMethod.GET, produces = "application/json")
    public String getConfInfo() {return configurationStr;}

    @RequestMapping(value="/", method=RequestMethod.POST, produces = "application/json")
    @ResponseStatus(value = HttpStatus.OK)
    public String serviceAction(@RequestBody String req)
    {
        logger.createLog("Got from client: " + req);
        if (req.contains("reboot")) Additional.restartApplication(); //TODO: Athena Reboot
        if (req.contains("refreshSwitch"))
        {
            configurationRaw.setRefreshEnabled(!(configurationRaw.isRefreshEnabled()));
            refreshCONF();
            return "refresh " + configurationRaw.isRefreshEnabled();
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
        logger.createLog("Called to execute " + cmdType + " " + servName);
        if (ServiceControl.servAction(servName, cmdType))
        {
            refreshHWSYSCONF();
            return "Success (" + servName + ' ' + cmdType + ')';
        }
        else return "Failure";
    }

    @Scheduled(fixedDelayString = "${api.refreshRate}")
    public void refreshHWSYSCONF()
    {
        if (!configurationRaw.isRefreshEnabled()) return;

        if (hwRaw != null) hwRaw.refresh();
        else hwRaw = new HWInfo();

        if (sysRaw != null) sysRaw.refresh();
        else sysRaw = new SystemCtlReport();

        String buf = "";

        try {buf = this.JSONMapper.writeValueAsString(hwRaw);}
        catch (JsonProcessingException e) {logger.createLog("HW refresh error");}
        hwInfo = buf;

        try {buf = this.JSONMapper.writeValueAsString(sysRaw);}
        catch (JsonProcessingException e) {logger.createLog("SYS refresh error");}
        systemCtlReport = buf;

        try {buf = this.JSONMapper.writeValueAsString(configurationRaw);}
        catch (JsonProcessingException e) {logger.createLog("CONF refresh error");}
        configurationStr = buf;
    }

    public void refreshCONF()
    {
        String buf = "";
        try {buf = this.JSONMapper.writeValueAsString(configurationRaw);}
        catch (JsonProcessingException e) {logger.createLog("CONF refresh error");}
        configurationStr = buf;
    }
}