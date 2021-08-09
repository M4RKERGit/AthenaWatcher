package com.athena.hardware;

import com.athena.linuxtools.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CPU extends Device
{
    private String manufacturer;
    private String modelName;
    private float cpuFreq;
    private long cacheSize;
    private int cores;
    private String FPU;
    private String curTemp;
    private String critTemp;
    private boolean valid;
    private final Logger logger = new Logger("[CPU]");

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
            logger.createLog("CPU's not formed");
        }
        this.valid = true;
    }

    public boolean determineOverheat()
    {
        if (!this.valid) return false;
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

    public String getManufacturer() {return manufacturer;}
    public void setManufacturer(String manufacturer) {this.manufacturer = manufacturer;}
    public String getModelName() {return modelName;}
    public void setModelName(String modelName) {this.modelName = modelName;}
    public float getCpuFreq() {return cpuFreq;}
    public void setCpuFreq(float cpuFreq) {this.cpuFreq = cpuFreq;}
    public long getCacheSize() {return cacheSize;}
    public void setCacheSize(long cacheSize) {this.cacheSize = cacheSize;}
    public int getCores() {return cores;}
    public void setCores(int cores) {this.cores = cores;}
    public String getFPU() {return FPU;}
    public void setFPU(String FPU) {this.FPU = FPU;}
    public String getCurTemp() {return curTemp;}
    public void setCurTemp(String curTemp) {this.curTemp = curTemp;}
    public String getCritTemp() {return critTemp;}
    public void setCritTemp(String critTemp) {this.critTemp = critTemp;}
    public boolean isValid() {return valid;}
    public void setValid(boolean valid) {this.valid = valid;}
}