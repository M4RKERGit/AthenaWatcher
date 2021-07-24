package com.athena.hardware;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GPU extends Device
{
    public String description;
    public String product;
    public String vendor;
    public String clock;
    public String curTemp;
    public String critTemp;
    public String power;

    public GPU()
    {
        ArrayList<String> lshwReport = getReport(new String[]{"lshw", "-C", "video"});
        ArrayList<String> sensorsReport = getReport(new String[]{"sensors"});
        this.description = parseParameter(lshwReport, "description").split(":")[1].strip();
        this.product = parseParameter(lshwReport, "product").split(":")[1].strip();
        this.vendor = parseParameter(lshwReport, "vendor").split(":")[1].strip();
        this.clock = parseParameter(lshwReport, "clock").split(":")[1].strip();

        this.curTemp = parseParameter(sensorsReport, "edge").split("\s{1,10}")[1];
        this.critTemp = parseParameter(sensorsReport, "edge").split("\s{1,10}")[4].replaceFirst(",", "");
        this.power = parseParameter(sensorsReport, "power").split("\s{2,10}")[1];
        System.out.println("GPU formed\n");
        determineOverheat();
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
            System.out.println("!!!Overheat!!!");
            return true;
        }
        else System.out.println("Normal temp");
        return false;
    }
}
