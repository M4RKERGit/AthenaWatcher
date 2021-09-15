package com.athena.controllers;

import com.athena.linuxtools.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;

@Controller
public class AthenaController
{
    private final Logger logger = new Logger("[IND]");

    @GetMapping("/")
    public ModelAndView index()
    {
        logger.createLog("Index page visit");
        return new ModelAndView("index.html", new HashMap<>());
    }
}