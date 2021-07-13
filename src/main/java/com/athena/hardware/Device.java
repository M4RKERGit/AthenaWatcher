package com.athena.hardware;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Device
{
    String getLine(String line, int lineNum)
    {
        Process process;
        String s = "";
        try
        {
            BufferedReader execOutput = null;
            process = Runtime.getRuntime().exec(line);
            if (process == null) System.out.print("Null process\n");
            else  {execOutput = new BufferedReader(new InputStreamReader(process.getInputStream()));}
            for (int i = 0; i < lineNum; i++) s = execOutput.readLine();
        }
        catch (IOException e) {System.out.print("HWCall error\n");}
        return s;
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
        for (int i = 0; i < GOT.size(); i++) {if (GOT.get(i).contains(match)) return GOT.get(i);}
        return null;
    }
}
