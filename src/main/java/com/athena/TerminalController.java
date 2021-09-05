package com.athena;

import com.athena.linuxtools.Logger;
import com.athena.linuxtools.TerminalExtra;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

@RestController
@RequestMapping("/terminal")
public class TerminalController
{
    private static final Logger logger = new Logger("[TER]");
    private static final String browseText = "Started terminal at " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

    @GetMapping("/")
    public ModelAndView slashIndex()
    {
        logger.createLog("Terminal Visit");
        return new ModelAndView("terminalForm.html", new HashMap<>());
    }

    @GetMapping("/text")
    public String getBrowseText() {return browseText;}

    @PostMapping("/")
    @ResponseStatus(value = HttpStatus.OK)
    public String executeCommand(@RequestBody String cmd)
    {
        logger.createLog("POST: " + cmd);
        return browseText + ("\n\n" + TerminalExtra.parseRequest(TerminalExtra.recoverPost(cmd)) + "\n\n");
    }
}
