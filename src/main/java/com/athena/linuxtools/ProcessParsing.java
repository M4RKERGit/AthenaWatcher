package com.athena.linuxtools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ProcessParsing
{
    private static final Logger logger = new Logger("[PRO]");
    public String getLine(String line, int lineNum)
    {
        Process process;
        String s = "";
        try
        {
            BufferedReader execOutput = null;
            process = Runtime.getRuntime().exec(line);
            if (process == null) logger.createLog("Null process");
            else  {execOutput = new BufferedReader(new InputStreamReader(process.getInputStream()));}
            for (int i = 0; i < lineNum; i++) {assert execOutput != null; s = execOutput.readLine();}
        }
        catch (IOException | NullPointerException e) {logger.createLog("HWCall error");}
        return s;
    }

    public ArrayList<String> getReport(String[] command)
    {
        String s;
        ArrayList<String> report = new ArrayList<>();
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
        catch (IOException e) {e.printStackTrace();}
        return report;
    }

    public String parseParameter(ArrayList<String> GOT, String match)
    {
        for (String s : GOT) {if (s.contains(match)) return s;}
        return "";
    }
}
