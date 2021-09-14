package com.athena.systeminfo;

import com.athena.linuxtools.Logger;
import com.athena.linuxtools.ProcessParsing;
import lombok.Getter;

@Getter
public class Neofetch extends ProcessParsing
{
    private String output;
    private final Logger logger = new Logger("[NEO]");

    public Neofetch()
    {
        try {this.output = String.join("<br>", getReport(new String[]{"neofetch", "--stdout"}));}
        catch(Exception e)
        {
            logger.createLog("Unable to call neofetch");
            if (getReport(new String[]{"which", "neofetch"}).isEmpty()) logger.createLog("Neofetch isn't installed");
        }
    }
}
