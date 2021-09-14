package com.athena.systeminfo;

import com.athena.linuxtools.Logger;
import com.athena.linuxtools.ProcessParsing;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public class Service extends ProcessParsing
{
    private final Logger logger = new Logger("UNT");
    private final String serviceName;
    private boolean defined = false;
    private String loaded;
    private String activity;
    private String PID;
    private String memory;
    private String log;

    public Service(String serviceName)
    {
        this.serviceName = serviceName;
        ArrayList<String> report = getReport(new String[]{"systemctl", "status", serviceName});
        if (report.isEmpty()) return;
        this.loaded = parseParameter(report, "Loaded").strip();
        this.activity = parseParameter(report, "Active").strip();
        this.defined = true;
        try{this.PID = parseParameter(report, "PID").strip();}
        catch (Exception e)
        {
            logger.createLog("Process isn't active, PID getting failed");
            this.PID = "PID unavailable";
        }
        try{this.memory = parseParameter(report, "Memory").strip();}
        catch (Exception e)
        {
            logger.createLog("Process isn't active, memory getting failed");
            this.memory = "Memory unavailable";
        }
        var journal = getReport(new String[]{"journalctl", "-eu", serviceName});
        if (journal.size() < 15) this.log = String.join("<br>", journal.subList(0, journal.size()));
        else this.log = String.join("<br>", journal.subList(journal.size() - 15, journal.size()));
    }
}
