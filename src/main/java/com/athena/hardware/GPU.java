package com.athena.hardware;

import com.athena.linuxtools.Logger;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public class GPU extends Device
{
    private String description;
    private String product;
    private String vendor;
    private String clock;
    private String power;
    private boolean sensorsEnabled;

    public GPU(boolean sensorsEnabled)
    {
        try
        {
            logger = new Logger("[GPU]");
            this.sensorsEnabled = sensorsEnabled;
            ArrayList<String> lshwReport = getReport(new String[]{"lshw", "-C", "video"});
            this.description = parseParameter(lshwReport, "description").split(":")[1].strip();
            this.product = parseParameter(lshwReport, "product").split(":")[1].strip();
            this.vendor = parseParameter(lshwReport, "vendor").split(":")[1].strip();
            this.clock = parseParameter(lshwReport, "clock").split(":")[1].strip();
            if (this.sensorsEnabled) {scanTemp();}
        }
        catch (Exception e) {logger.createLog("GPU's not formed");}
    }

    public void refresh()
    {
        if (this.sensorsEnabled) {scanTemp();}
    }

    private void scanTemp()
    {
        ArrayList<String> sensorsReport = getReport(new String[]{"sensors"});
        try
        {
            this.curTemp = parseParameter(sensorsReport, "edge").split("\s{1,10}")[1];
            this.critTemp = parseParameter(sensorsReport, "edge").split("\s{1,10}")[4].replaceFirst(",", "");
            this.power = parseParameter(sensorsReport, "power").split("\s{2,10}")[1];
        }
        catch(Exception e)
        {
            logger.createLog("Sensors error, integrated GPU maybe");
            this.curTemp = "Same as CPU";
            this.critTemp = "Same as CPU";
            this.power = "Same as CPU";
        }
    }
}
