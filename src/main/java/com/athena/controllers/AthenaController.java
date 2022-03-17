package com.athena.controllers;

import com.athena.services.WebsiteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequiredArgsConstructor
public class AthenaController
{
    private final WebsiteService service;

    @GetMapping("/")
    public ModelAndView index() {return service.responseIndex();}
}