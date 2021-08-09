package com.athena.webSSH;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Date;

public class Additional
{
    static String PRINT_PREFIX = " [ADD]: ";
    static String[] unableToExec = {};

    public static String executeUtil(String line, String userDir) throws IOException
    {
        for (int i = 0; i < unableToExec.length; i++) {if (line.split(" ")[0].equals(unableToExec[i])) return "This utility is unavailable";}
        Process process;
        File dir  = new File(userDir);
        if (!dir.exists())
        {
            Additional.appendLog("Can't make a call from this directory", PRINT_PREFIX);
            return "Can't make a call from this directory";
        }
        try {process = Runtime.getRuntime().exec(line, null, dir);}
        catch (IOException e)
        {
            e.printStackTrace();
            Additional.appendLog("Failed to execute " + line, PRINT_PREFIX);
            return "Failed to execute";
        }
        BufferedReader stdInput = null;
        if (process != null) {stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
            Additional.appendLog("Process started", PRINT_PREFIX);}
        String s = "", report = "";
        while (true)
        {
            try {if ((s = stdInput.readLine()) == null) break;}
            catch (IOException e){Additional.appendLog("Unable to read line", PRINT_PREFIX);}
            report += (s + "\n");
        }
        try {process.waitFor();}
        catch (InterruptedException e) {e.printStackTrace(); Additional.appendLog("Process crushed", PRINT_PREFIX);}
        Additional.appendLog("Process finished", PRINT_PREFIX);
        return report;
    }

    public static void appendLog(String text, String prefix)
    {
        String log = new Date() + prefix + text;// + " " + "From: " + user;
        System.out.println(log);
        try {Files.write(Path.of("log.txt"), (log + "\n").getBytes(), new StandardOpenOption[]{StandardOpenOption.APPEND});}
        catch (IOException e) {System.out.println("Logging error");}
    }

    public static String changeDir(String recDir, String path)
    {
        String toRet = recDir;
        if (path.equals(".."))
        {
            if (recDir.equals("/"))   return recDir;
            StringBuilder buf = new StringBuilder(toRet);
            buf.deleteCharAt(buf.length() - 1);
            for (int i = buf.length() - 1; i > 0; i--)
            {
                if (buf.charAt(i) != '/')   buf.deleteCharAt(i);
                else break;
            }
            toRet = buf.toString();
        }
        else if (path.charAt(0) == '/')  toRet = path;
        else toRet = recDir + path;
        if (!(toRet.charAt(toRet.length() - 1) == '/'))   toRet += '/';
        return toRet;
    }
}