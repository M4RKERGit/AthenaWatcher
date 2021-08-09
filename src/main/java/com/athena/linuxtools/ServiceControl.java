package com.athena.linuxtools;

public class ServiceControl
{
    public static void servAction(String servName, String cmdType)
    {
        Process process = null;
        try
        {
            System.out.println("systemctl" + " " + servName + " " + cmdType);
            process = Runtime.getRuntime().exec("systemctl" + " " + servName + " " + cmdType);
            process.waitFor();
        }
        catch (Exception e) {e.printStackTrace();}
    }
}
