package com.athena.webSSH;

public class HTMLCode
{
    public static String refreshHTML(String browseText, String curDir)
    {
        browseText += "\n\nExecution by root user, current directory: " + curDir;
        return  "<!DOCTYPE HTML>\n" +
                "<html>\n" +
                "<head>\n" +
                "  <meta charset=\"utf-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "    <form>\n" +
                "        <p><textarea style = \"width: 90%; height: 800px;\" name=\"message\">"+browseText+"</textarea></p>\n" +
                "        <p><input style = \"width: 50%;\" id=\"txtLine\" type=\"txtLine\" name=\"txtLine\" value=\"\"></p>\n" +
                "        <button onclick=\"sendCMD()\">Send</button>\n" +
                "    </form>\n" +
                "    <script>\n" +
                "        function sendCMD() \n" +
                "        {\n" +
                "        var val = document.getElementById(\"txtLine\").value;\n" +
                "        var xhr = new XMLHttpRequest();\n" +
                "        xhr.open('POST', val, false);\n" +
                "        xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');\n" +
                "        xhr.send(val);\n" +
                "        }\n" +
                "    </script>\n" +
                "</body>\n" +
                "</html>";
    }
}
