package com.athena;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

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
        visit.description = String.format("Visited index at %s", LocalDateTime.now());
        visitsRepository.save(visit);

        return new ModelAndView("index.html", model);
    }
}