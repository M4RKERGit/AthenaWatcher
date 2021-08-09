package com.athena.hardware;

import com.athena.linuxtools.Logger;

import java.util.ArrayList;

public class GPU extends Device
{
    private String description;
    private String product;
    private String vendor;
    private String clock;
    private String power;

    public GPU(boolean sensorsEnabled)
    {
        try
        {
            logger = new Logger("[GPU]");
            ArrayList<String> lshwReport = getReport(new String[]{"lshw", "-C", "video"});
            this.description = parseParameter(lshwReport, "description").split(":")[1].strip();
            this.product = parseParameter(lshwReport, "product").split(":")[1].strip();
            this.vendor = parseParameter(lshwReport, "vendor").split(":")[1].strip();
            this.clock = parseParameter(lshwReport, "clock").split(":")[1].strip();
            if (sensorsEnabled)
            {
                ArrayList<String> sensorsReport = getReport(new String[]{"sensors"});
                this.curTemp = parseParameter(sensorsReport, "edge").split("\s{1,10}")[1];
                this.critTemp = parseParameter(sensorsReport, "edge").split("\s{1,10}")[4].replaceFirst(",", "");
                this.power = parseParameter(sensorsReport, "power").split("\s{2,10}")[1];
            }
        }
        catch (Exception e)
        {
            logger.createLog("GPU's not formed");
        }
    }

    public String getDescription() {return description;}
    public String getProduct() {return product;}
    public String getVendor() {return vendor;}
    public String getClock() {return clock;}
    public String getPower() {return power;}
}
