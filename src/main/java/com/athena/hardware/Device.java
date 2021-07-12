package com.athena.hardware;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Device
{
    public String className = getClass().getSimpleName();

    String getLine(String line, int lineNum)
    {
        Process process;
        String s = "";
        try
        {
            BufferedReader execOutput = null;
            process = Runtime.getRuntime().exec(line);
            if (process != null) {execOutput = new BufferedReader(new InputStreamReader(process.getInputStream()));}
            for (int i = 0; i < lineNum; i++) s = execOutput.readLine();
        }
        catch (IOException e) {System.out.print("HWCall error\n");}
        return s;
    }
}
