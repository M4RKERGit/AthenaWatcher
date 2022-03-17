package com.athena.systeminfo;

import com.athena.linuxtools.ProcessParsing;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;

@Getter
@Slf4j
public class Service extends ProcessParsing
{
    private final String serviceName;
    private boolean defined = false;
    private String loaded;
    private String activity;
    private String PID;
    private String memory;
    private String stringLog;

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
            log.info("Process isn't active, PID getting failed");
            this.PID = "PID unavailable";
        }
        try{this.memory = parseParameter(report, "Memory").strip();}
        catch (Exception e)
        {
            log.info("Process isn't active, memory getting failed");
            this.memory = "Memory unavailable";
        }
        var journal = getReport(new String[]{"journalctl", "-eu", serviceName});
        if (journal.size() < 15) this.stringLog = String.join("<br>", journal.subList(0, journal.size()));
        else this.stringLog = String.join("<br>", journal.subList(journal.size() - 15, journal.size()));
    }
}
