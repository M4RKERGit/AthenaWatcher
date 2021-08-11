package com.athena.linuxtools;

import com.athena.AthenaApplication;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Additional
{
    private static Logger logger = new Logger("[ADD]");
    public static String getCurrentTime()
    {
        LocalDateTime dateTime = LocalDateTime.now();
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public static void restartApplication()
    {
        /*logger.createLog("Restarting Athena...");
        String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
        File currentJar = null;
        logger.createLog(javaBin);
        try
        {
            URI jj = AthenaApplication.class.getProtectionDomain().getCodeSource().getLocation().toURI();
            logger.createLog("URI:" + jj.toString());
            currentJar = new File(jj); //TODO:now working
            logger.createLog(currentJar.toString());
        }
        catch (URISyntaxException e) {logger.createLog("URI exception");}

        if(!currentJar.getName().endsWith(".jar"))
            return;

        final ArrayList<String> command = new ArrayList<String>();
        command.add(javaBin);
        command.add("-jar");
        command.add(currentJar.getPath());

        final ProcessBuilder builder = new ProcessBuilder(command);
        try {builder.start();}
        catch (IOException e) {logger.createLog("Restart failed");}
        System.exit(0);*/
    }
}
