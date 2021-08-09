package com.athena.hardware;

import com.athena.linuxtools.Logger;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GPU extends Device
{
    private String description;
    private String product;
    private String vendor;
    private String clock;
    private String curTemp;
    private String critTemp;
    private String power;
    private final Logger logger = new Logger("[GPU]");

    public GPU(boolean sensorsEnabled)
    {
        try
        {
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

    public boolean determineOverheat()
    {
        float cur = 0, crit = 0;
        Pattern pattern = Pattern.compile("[-]?[0-9]+(.[0-9]+)?");
        Matcher matcher = pattern.matcher(this.curTemp);
        if (matcher.find()) {String value = this.curTemp.substring(matcher.start(), matcher.end()); cur = Float.parseFloat(value);}
        matcher = pattern.matcher(this.critTemp);
        if (matcher.find()) {String value = this.critTemp.substring(matcher.start(), matcher.end()); crit = Float.parseFloat(value);}
        System.out.println("Temp: " + cur + "/" + crit);
        if ((crit * 0.8) < cur)
        {
            logger.createLog("!!!Overheat!!!");
            return true;
        }
        else logger.createLog("Normal temp");
        return false;
    }

    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}
    public String getProduct() {return product;}
    public void setProduct(String product) {this.product = product;}
    public String getVendor() {return vendor;}
    public void setVendor(String vendor) {this.vendor = vendor;}
    public String getClock() {return clock;}
    public void setClock(String clock) {this.clock = clock;}
    public String getCurTemp() {return curTemp;}
    public void setCurTemp(String curTemp) {this.curTemp = curTemp;}
    public String getCritTemp() {return critTemp;}
    public void setCritTemp(String critTemp) {this.critTemp = critTemp;}
    public String getPower() {return power;}
    public void setPower(String power) {this.power = power;}
}
