package com.athena;

import com.athena.hardware.HWInfo;
import com.athena.linuxtools.Additional;
import com.athena.linuxtools.Logger;
import com.athena.linuxtools.ServiceControl;
import com.athena.systeminfo.SystemCtlReport;
import com.athena.notifications.Notificator;
import com.athena.webSSH.EndServer;
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
    public Notificator notificator = new Notificator();
    private final Logger logger = new Logger("[API]");
    private HWInfo hwRaw = new HWInfo();
    private SystemCtlReport sysRaw = new SystemCtlReport();
    private String hwInfo;
    private String systemCtlReport;

    public AthenaAPI(VisitsRepository visitsRepository)
    {
        this.visitsRepository = visitsRepository;
        refreshHWSYS();
    }

    @GetMapping("/")
    public ModelAndView slashIndex()
    {
        Map<String, String> model = new HashMap<>();
        Visitor visit = new Visitor();
        visit.description = String.format("Visited API at %s", Additional.getCurrentTime());
        visitsRepository.save(visit);
        logger.createLog("API Visit");

        return new ModelAndView("API.html", model);
    }

    @GetMapping("/sendmail")
    public String mailing()
    {
        String report = getHWInfo() + "\n\n\n" + getServInfo();
        notificator.sendBotMsg(report);
        Notificator.emailController.sendMailMessage(report);
        logger.createLog("Sendmail call");
        return "Channels successfully checked!";
    }

    @GetMapping("/visits")
    public Iterable<Visitor> getVisits() {return visitsRepository.findAll();}

    @RequestMapping(value = "/hwinfo", method = RequestMethod.GET, produces = "application/json")
    public String getHWInfo()
    {
        logger.createLog("HWInfo successful request");
        return this.hwInfo;
    }

    @RequestMapping(value = "/servinfo", method = RequestMethod.GET, produces = "application/json")
    public String getServInfo()
    {
        logger.createLog("ServInfo successful request");
        return this.systemCtlReport;
    }

    @RequestMapping(value = "/terminal", method = RequestMethod.GET, produces = "application/json")
    public String enterTerminal()
    {
        EndServer.craftServer();
        logger.createLog("Called terminal");
        return  ""; //TODO: terminal
    }

    @RequestMapping(value="/", method=RequestMethod.POST, produces = "application/json")
    @ResponseStatus(value = HttpStatus.OK)
    public String serviceAction(@RequestBody String req)
    {
        logger.createLog("Got from client: " + req);
        if (req.contains("reboot")) Additional.restartApplication(); //TODO: Athena Reboot
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
        logger.createLog("Called to execute " + cmdType + ' ' + servName);
        if (ServiceControl.servAction(servName, cmdType))
        {
            refreshHWSYS();
            return "Success (" + servName + ' ' + cmdType + ')';
        }
        else return "Failure";
    }

    @Scheduled(fixedDelay = 3000)
    public void refreshHWSYS()
    {
        this.hwRaw.refresh();
        this.sysRaw = new SystemCtlReport();
        ObjectMapper JSONMapper = new ObjectMapper();
        JSONMapper.enable(SerializationFeature.INDENT_OUTPUT);
        String buf = "";
        try {buf = JSONMapper.writeValueAsString(this.hwRaw);}
        catch (JsonProcessingException e) {e.printStackTrace();}
        this.hwInfo = buf;
        try {buf = JSONMapper.writeValueAsString(this.sysRaw);}
        catch (JsonProcessingException e) {e.printStackTrace();}
        this.systemCtlReport = buf;
    }
}