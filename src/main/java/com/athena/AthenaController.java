package com.athena;

import com.athena.hardware.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@Controller
public class AthenaController
{
    final VisitsRepository visitsRepository;

    public AthenaController(VisitsRepository visitsRepository)
    {
        this.visitsRepository = visitsRepository;
    }

    @GetMapping("/")
    public ModelAndView index()
    {
        Map<String, String> model = new HashMap<>();
        model.put("name", "Alexey");

        Visitor visit = new Visitor();
        visit.description = String.format("Visited at %s", LocalDateTime.now());
        visitsRepository.save(visit);

        return new ModelAndView("index.html", model);
    }
}
