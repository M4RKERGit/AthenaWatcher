package com.athena.hardware;

import com.athena.linuxtools.Logger;

public class CPU extends Device
{
    private String manufacturer;
    private String modelName;
    private float cpuFreq;
    private long cacheSize;
    private int cores;
    private String FPU;
    private boolean valid;

    public CPU(boolean sensorsEnabled)
    {
        try
        {
            logger = new Logger("[CPU]");
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

    public String getManufacturer() {return manufacturer;}
    public String getModelName() {return modelName;}
    public float getCpuFreq() {return cpuFreq;}
    public long getCacheSize() {return cacheSize;}
    public int getCores() {return cores;}
    public String getFPU() {return FPU;}
    public boolean isValid() {return valid;}
}