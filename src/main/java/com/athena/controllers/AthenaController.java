package com.athena.controllers;

import com.athena.services.WebsiteService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class AthenaController
{
    private final WebsiteService service = new WebsiteService();

    @GetMapping("/")
    public ModelAndView index() {return service.responseIndex();}
}