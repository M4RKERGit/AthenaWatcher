package com.athena;

import com.athena.linuxtools.Additional;
import com.athena.linuxtools.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;


@Controller
public class AthenaController
{
    final VisitsRepository visitsRepository;
    private final Logger logger = new Logger("[IND]");

    public AthenaController(VisitsRepository visitsRepository)
    {
        this.visitsRepository = visitsRepository;
    }

    @GetMapping("/")
    public ModelAndView index()
    {
        Map<String, String> model = new HashMap<>();
        model.put("name", "Anon");

        Visitor visit = new Visitor();
        visit.description = String.format("Visited index at %s", Additional.getCurrentTime());
        visitsRepository.save(visit);
        logger.createLog("Index page visit");

        return new ModelAndView("index.html", model);
    }
}