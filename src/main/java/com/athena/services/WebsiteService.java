package com.athena.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;

@Service
@Slf4j
public class WebsiteService
{
    public ModelAndView responseIndex()
    {
        log.info("Index page visit");
        return new ModelAndView("index.html", new HashMap<>());
    }
}
