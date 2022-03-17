package com.athena.systeminfo;

import com.athena.linuxtools.ProcessParsing;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class Neofetch extends ProcessParsing
{
    private String output;
    public Neofetch()
    {
        try {this.output = String.join("<br>", getReport(new String[]{"neofetch", "--stdout"}));}
        catch(Exception e)
        {
            log.info("Unable to call neofetch");
            if (getReport(new String[]{"which", "neofetch"}).isEmpty()) log.info("Neofetch isn't installed");
        }
    }
}
