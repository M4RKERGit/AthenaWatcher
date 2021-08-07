package com.athena.hardware;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CPU extends Device
{
    public String manufacturer;
    public String modelName;
    public float cpuFreq;
    public long cacheSize;
    public int cores;
    public String FPU;
    public String curTemp;
    public String critTemp;
    public boolean valid = false;

    public CPU(boolean sensorsEnabled)
    {
        try
        {
            this.manufacturer = getLine("grep vendor_id /proc/cpuinfo", 1).split(":")[1].strip();
            this.modelName = getLine("grep model /proc/cpuinfo", 2).split(":")[1].strip();
            this.cpuFreq = Float.parseFloat(getLine("grep cpu /proc/cpuinfo", 2).split(":")[1].strip());
            this.cacheSize = Long.parseLong(getLine("grep cache /proc/cpuinfo", 1).split(":")[1].split(" ")[1].strip());
            this.cores = Integer.parseInt(getLine("nproc", 1).strip());
            this.FPU = getLine("grep fpu /proc/cpuinfo", 1).split(":")[1].strip();
            if (sensorsEnabled)
            {
                this.curTemp = parseParameter(getReport(new String[]{"sensors"}), "Package").split("\s{1,10}")[3];
                this.critTemp = parseParameter(getReport(new String[]{"sensors"}), "Package").split("\s{1,10}")[9].replaceFirst("\\)", "");
            }
        }
        catch (Exception e)
        {
            System.out.println("CPU's not formed");
            System.out.println(this);
        }
        this.valid = true;
        System.out.println("CPU formed\n");
    }

    public boolean determineOverheat()
    {
        if (this.valid == false) return false;
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