package com.athena.linuxtools;

public class ServiceControl
{
    private static Logger logger = new Logger("[SER]");
    public static boolean servAction(String servName, String cmdType)
    {
        Process process = null;
        var line = "sudo systemctl" + " " + cmdType + " " + servName;
        try
        {
            logger.createLog("Trying to execute " + line);
            process = Runtime.getRuntime().exec(line);
            process.waitFor();
        }
        catch (Exception e) {logger.createLog("Failed to execute " + line); return false;}
        return true;
    }
}
