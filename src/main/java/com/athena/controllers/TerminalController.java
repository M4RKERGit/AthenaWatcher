package com.athena.controllers;

import com.athena.services.TerminalService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/terminal")
public class TerminalController
{
    private final TerminalService service = new TerminalService();

    @GetMapping("/")
    public ModelAndView slashIndex() {return service.responseIndex();}

    @GetMapping("/text")
    public String getBrowseText() {return service.getBrowseText();}

    @PostMapping("/")
    @ResponseStatus(value = HttpStatus.OK)
    public String executeCommand(@RequestBody String cmd) {return service.executeAndResponse(cmd, false);}
}
