package com.athena.linuxtools;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServiceControl
{
    public static boolean servAction(String servName, String cmdType)
    {
        Process process;
        String line = String.format("systemctl %s %s", cmdType, servName);
        try
        {
            log.info("Trying to execute " + line);
            process = Runtime.getRuntime().exec(line);
            process.waitFor();
        }
        catch (Exception e) {log.info("Failed to execute " + line); return false;}
        return true;
    }
}
