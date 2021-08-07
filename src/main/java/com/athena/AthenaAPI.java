package com.athena;

import com.athena.hardware.HWInfo;
import com.athena.systeminfo.SystemCtlReport;
import com.athena.notifications.Notificator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api")
public class AthenaAPI
{
    private final VisitsRepository visitsRepository;

    public AthenaAPI(VisitsRepository visitsRepository) {this.visitsRepository = visitsRepository;}
    public Notificator notificator = new Notificator();

    @GetMapping("/")
    public ModelAndView slashIndex()
    {
        Map<String, String> model = new HashMap<>();
        Visitor visit = new Visitor();
        visit.description = String.format("Visited API at %s", LocalDateTime.now());
        visitsRepository.save(visit);

        return new ModelAndView("API.html", model);
    }

    @GetMapping("/sendmail")
    public String mailing()
    {
        String report = getHWInfo() + "\n\n\n" + getServInfo();
        notificator.sendBotMsg(report);
        Notificator.emailController.sendMailMessage(report);
        return "Channels successfully checked!";
    }

    @GetMapping("/visits")
    public Iterable<Visitor> getVisits() {return visitsRepository.findAll();}

    @RequestMapping(value = "/hwinfo", method = RequestMethod.GET, produces = "application/json")
    public String getHWInfo()
    {
        HWInfo info = new HWInfo();
        System.out.println("JSOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOn"); //SERVDEBUG
        ObjectMapper JSONMapper = new ObjectMapper();
        JSONMapper.enable(SerializationFeature.INDENT_OUTPUT);
        String buf = "";
        try {buf = JSONMapper.writeValueAsString(info);}
        catch (JsonProcessingException e) {e.printStackTrace();}
        return buf;
    }

    @RequestMapping(value = "/servinfo", method = RequestMethod.GET, produces = "application/json")
    public String getServInfo()
    {
        SystemCtlReport info = new SystemCtlReport();
        ObjectMapper JSONMapper = new ObjectMapper();
        JSONMapper.enable(SerializationFeature.INDENT_OUTPUT);
        String buf = "";
        try {buf = JSONMapper.writeValueAsString(info);}
        catch (JsonProcessingException e) {e.printStackTrace();}
        return buf;
    }
}