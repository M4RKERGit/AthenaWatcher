package com.athena.systeminfo;

import com.athena.linuxtools.ProcessParsing;

import java.util.ArrayList;

public class Service extends ProcessParsing
{
    public String serviceName;
    public boolean defined = false;
    public String loaded;
    public String activity;
    public String PID;
    public String memory;
    public String log;

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
            System.out.println("Process isn't active, PID getting failed");
            this.PID = "PID unavailable";
        }
        try{this.memory = parseParameter(report, "Memory").strip();}
        catch (Exception e)
        {
            System.out.println("Process isn't active, memory getting failed");
            this.memory = "Memory unavailable";
        }
        var journal = getReport(new String[]{"journalctl", "-eu", serviceName});
        this.log = String.join("<br>", journal.subList(journal.size() - 15, journal.size()));
    }
}
