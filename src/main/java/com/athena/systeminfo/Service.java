package com.athena.systeminfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Service
{
    //public String kind = getClass().toString();
    public String serviceName;
    public String loaded;
    public String activity;
    public String PID;
    public String memory;
    public String[] log;

    public Service(String serviceName)
    {
        this.serviceName = serviceName;
        ArrayList<String> report = getReport(new String[]{"systemctl", "status", serviceName});
        System.out.println("\n\n" + report + "\n\n");
        this.loaded = parseParameter(report, "Loaded").strip();
        this.activity = parseParameter(report, "Active").strip();
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
    }

    ArrayList<String> getReport(String[] command)
    {
        String s;
        ArrayList<String> report = new ArrayList<String>();
        ProcessBuilder pB = new ProcessBuilder(command);
        try
        {
            BufferedReader execOutput;
            Process process = pB.start();
            execOutput = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while(true)
            {
                s = execOutput.readLine();
                if (s == null)  break;
                report.add(s);
            }
        }
        catch (IOException e){e.printStackTrace();}
        return report;
    }

    String parseParameter(ArrayList<String> GOT, String match)
    {
        for (String s : GOT) {if (s.contains(match)) return s;}
        return null;
    }
}
