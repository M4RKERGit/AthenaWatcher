package com.athena.webSSH;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class EndServer
{
    static String PRINT_PREFIX = " [SERV]: ";
    static String browseText = "Welcome to webSSH\n";
    static String htmlCode = "";
    static String userDir = "/";
    public static void craftServer()
    {
        try (ServerSocket serverSocket = new ServerSocket(80)) {
            Additional.appendLog("Server started!", PRINT_PREFIX);
            String request = "";
            while (true)
            {
                Socket socket = serverSocket.accept();
                try (BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                     PrintWriter output = new PrintWriter(socket.getOutputStream()))
                {
                    while (!input.ready());
                    if (input.ready()) {request = input.readLine();}
                    Additional.appendLog(request, PRINT_PREFIX);
                    parseRequest(request);
                    htmlCode = HTMLCode.refreshHTML(browseText, userDir);
                    output.println(htmlCode);
                }
            }
        }
        catch (IOException ex) {Additional.appendLog("Connection error", PRINT_PREFIX);}
    }

    private static void parseRequest(String req)
    {
        String[] parsed = req.split(" ");
        String mode = parsed[0];
        String command = parsed[1].replaceAll("%20", " ");
        String[] parsCom = command.split(" ");
        if (command.equals("/favicon.ico"))  return;
        switch (mode)
        {
            case "GET":
                Additional.appendLog("Got GET request", PRINT_PREFIX);
                return;
            case "POST":
                Additional.appendLog("Got POST request with " + command, PRINT_PREFIX);
                try
                {
                    if (parsCom[0].equals("/clear")) {browseText = ""; return;}
                    if (parsCom[0].equals("/cd")) {userDir = Additional.changeDir(userDir, parsCom[1]); return;}
                    browseText += Additional.executeUtil(command.substring(1), userDir);
                }
                catch (IOException e) {System.out.println("Invalid command");}
                break;
        }
    }
}