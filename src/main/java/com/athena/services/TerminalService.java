package com.athena.services;

import com.athena.linuxtools.Additional;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

@Service
@Slf4j
public class TerminalService
{
    @Getter
    private final String browseText = String.format("Started terminal at %s", Additional.getCurrentTime());
    private final String[] unableToExec = {"zhzh"};
    private String termOut;
    private String userDir = "/";

    public ModelAndView responseIndex()
    {
        log.info("Terminal Visit");
        return new ModelAndView("terminalForm.html", new HashMap<>());
    }

    public String executeAndResponse(String cmd, boolean single)
    {
        if (single) log.info("Telegram: " + cmd);
        else log.info("POST: " + cmd);
        if (single) return parseRequest(recoverPost(cmd), true);
        else return browseText + ("\n\n" + parseRequest(recoverPost(cmd), false) + "\n\n");
    }

    public String parseRequest(String cmd, boolean single)
    {
        if (single) log.info("Got Telegram request with " + cmd);
        else log.info("Got POST request with " + cmd);
        if (cmd.equals("clear")) termOut = "";
        else if (cmd.startsWith("cd"))
        {
            userDir = changeDir(cmd.substring(2));
            termOut += "\nPath changed to: " + userDir;
        }
        else
        {
            if (single) termOut = executeUtil(cmd, userDir);
            else termOut += "\n" + executeUtil(cmd, userDir);
        }
        return termOut;
    }

    public String executeUtil(String line, String userDir)
    {
        for (String value : unableToExec) {if (line.split(" ")[0].equals(value)) return "This utility is unavailable";}
        ArrayList<String> list = new ArrayList<>();
        Collections.addAll(list, line.split(" "));
        File dir  = new File(userDir);
        if (!dir.exists())
        {
            log.info("Can't make a call from this directory");
            return "Can't make a call from this directory";
        }
        try
        {
            ProcessBuilder builder = new ProcessBuilder("/bin/sh", "-c", String.join(" ", list.toArray(new String[0])));
            builder.directory(dir);
            builder.redirectErrorStream(true);
            Process process = builder.start();

            BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String s;
            StringBuilder report = new StringBuilder();

            while((s = stdInput.readLine()) != null)
                if (!s.equals("null")) report.append(s).append("\n");

            process.waitFor();
            log.info("Process finished");
            return report.toString().replaceAll("null", "");
        }
        catch (IOException | InterruptedException e)
        {
            e.printStackTrace();
            log.info("Failed to execute " + line);
            return "Failed to execute";
        }
    }

    public String changeDir(String path)
    {
        path = path.strip();
        log.info("Got path: " + path);
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
