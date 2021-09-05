package com.athena.linuxtools;

import com.athena.linuxtools.Logger;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.File;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;

@Getter
public class TerminalExtra
{
    private static final Logger logger = new Logger("[EXT]");
    private static final String[] unableToExec = {"zhzh"};
    private static String browseText;
    private static String userDir = "/";

    public static String parseRequest(String cmd)
    {
        logger.createLog("Got POST request with " + cmd);
        if (cmd.equals("clear")) browseText = "";
        else if (cmd.startsWith("cd"))
        {
            userDir = changeDir(cmd.substring(2));
            browseText += "\nPath changed to: " + userDir;
        }
        else browseText += "\n" + executeUtil(cmd, userDir);
        return browseText;
    }

    public static String executeUtil(String line, String userDir)
    {
        for (String value : unableToExec) {if (line.split(" ")[0].equals(value)) return "This utility is unavailable";}
        ArrayList<String> list = new ArrayList<>();
        Collections.addAll(list, line.split(" "));
        File dir  = new File(userDir);
        if (!dir.exists())
        {
            logger.createLog("Can't make a call from this directory");
            return "Can't make a call from this directory";
        }
        try
        {
            ProcessBuilder builder = new ProcessBuilder("/bin/sh", "-c", String.join(" ", list.toArray(new String[0])));
            builder.directory(dir);
            builder.redirectErrorStream(true);
            Process process = builder.start();

            BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
            logger.createLog("Process started" + process.info());

            String s = "";
            StringBuilder report = new StringBuilder();

            while((s = stdInput.readLine()) != null)
                if (!s.equals("null")) report.append(s).append("\n");

            process.waitFor();
            logger.createLog("Process finished");
            return report.toString().replaceAll("null", "");
        }
        catch (IOException | InterruptedException e)
        {
            e.printStackTrace();
            logger.createLog("Failed to execute " + line);
            return "Failed to execute";
        }
    }

    public static String changeDir(String path)
    {
        path = path.strip();
        logger.createLog("Got path: " + path);
        String toRet = userDir;
        if (path.equals(".."))
        {
            if (userDir.equals("/")) return userDir;
            StringBuilder buf = new StringBuilder(toRet);
            buf.deleteCharAt(buf.length() - 1);
            for (int i = buf.length() - 1; i > 0; i--)
            {
                if (buf.charAt(i) != '/') buf.deleteCharAt(i);
                else break;
            }
            toRet = buf.toString();
        }
        else if (path.charAt(0) == '/')  toRet = path;
        else toRet = userDir + path;
        if (!(toRet.charAt(toRet.length() - 1) == '/'))   toRet += '/';
        return toRet;
    }

    @SneakyThrows
    public static String recoverPost(String GOT)
    {
        String recovered = URLDecoder.decode(GOT, StandardCharsets.UTF_8);
        return recovered.replaceFirst("=", "").replaceAll("\\+", " ");
    }
}