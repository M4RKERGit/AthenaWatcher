package com.athena.controllers;

import com.athena.linuxtools.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Controller
public class AthenaController
{
    private final Logger logger = new Logger("[IND]");

    @GetMapping("/")
    public ModelAndView index()
    {
        Map<String, String> model = new HashMap<>();
        model.put("name", "Anon");

        logger.createLog("Index page visit");

        return new ModelAndView("index.html", model);
    }
}