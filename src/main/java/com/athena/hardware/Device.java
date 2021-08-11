package com.athena.hardware;

import com.athena.linuxtools.Logger;
import com.athena.linuxtools.ProcessParsing;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Device extends ProcessParsing
{
    //TODO: kinds/types of devices
    protected String curTemp;
    protected String critTemp;
    protected static Logger logger;

    public boolean determineOverheat()
    {
        float cur = 0, crit = 0;
        Pattern pattern = Pattern.compile("[-]?[0-9]+(.[0-9]+)?");
        Matcher matcher = pattern.matcher(this.curTemp);
        if (matcher.find()) {String value = this.curTemp.substring(matcher.start(), matcher.end()); cur = Float.parseFloat(value);}
        matcher = pattern.matcher(this.critTemp);
        if (matcher.find()) {String value = this.critTemp.substring(matcher.start(), matcher.end()); crit = Float.parseFloat(value);}
        System.out.println();
        if ((crit * 0.8) < cur)
        {
            logger.createLog("Overheat: " + cur + "/" + crit);
            return true;
        }
        else logger.createLog("Normal temp: " + cur + "/" + crit);
        return false;
    }

    public String getCurTemp() {return curTemp;}
    public void setCurTemp(String curTemp) {this.curTemp = curTemp;}
    public String getCritTemp() {return critTemp;}
    public void setCritTemp(String critTemp) {this.critTemp = critTemp;}
}
