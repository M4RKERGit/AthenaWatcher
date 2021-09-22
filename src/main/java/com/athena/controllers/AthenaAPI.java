package com.athena.controllers;


import com.athena.hardware.HWInfo;
import com.athena.services.APIService;
import com.athena.systeminfo.Configuration;
import com.athena.systeminfo.SystemCtlReport;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/api")
public class AthenaAPI
{
    private final APIService service = new APIService();

    @GetMapping("/")
    public ModelAndView slashIndex() {return APIService.responseSlash();}

    @GetMapping("/sendmail")
    public String mailing() {return service.responseMailing();}

    @RequestMapping(value = "/hwinfo", method = RequestMethod.GET, produces = "application/json")
    public HWInfo getHWInfo() {return service.getHwInfo(); }

    @RequestMapping(value = "/servinfo", method = RequestMethod.GET, produces = "application/json")
    public SystemCtlReport getServInfo() {return service.getSysInfo(); }

    @RequestMapping(value = "/confinfo", method = RequestMethod.GET, produces = "application/json")
    public Configuration getConfInfo() {return service.getConfInfo();}

    @RequestMapping(value="/", method=RequestMethod.POST, produces = "application/json")
    @ResponseStatus(value = HttpStatus.OK)
    public String serviceAction(@RequestBody String req) {return service.responseSystemCtl(req);}
}