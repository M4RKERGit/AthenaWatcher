package com.athena.services;

import com.athena.linuxtools.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;

@Service
public class WebsiteService
{
    private final Logger logger = new Logger("[IND]");

    public ModelAndView responseIndex()
    {
        logger.createLog("Index page visit");
        return new ModelAndView("index.html", new HashMap<>());
    }
}
