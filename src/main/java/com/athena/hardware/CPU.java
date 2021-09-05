package com.athena.hardware;

import com.athena.linuxtools.Logger;
import lombok.Getter;

@Getter
public class CPU extends Device
{
    private String manufacturer;
    private String modelName;
    private float cpuFreq;
    private long cacheSize;
    private int cores;
    private String FPU;
    private boolean sensorsEnabled;

    public CPU(boolean sensorsEnabled)
    {
        try
        {
            logger = new Logger("[CPU]");
            this.sensorsEnabled = sensorsEnabled;
            this.manufacturer = getLine("grep vendor_id /proc/cpuinfo", 1).split(":")[1].strip();
            this.modelName = getLine("grep model /proc/cpuinfo", 2).split(":")[1].strip();
            this.cpuFreq = Float.parseFloat(getLine("grep cpu /proc/cpuinfo", 2).split(":")[1].strip());
            this.cacheSize = Long.parseLong(getLine("grep cache /proc/cpuinfo", 1).split(":")[1].split(" ")[1].strip());
            this.cores = Integer.parseInt(getLine("nproc", 1).strip());
            this.FPU = getLine("grep fpu /proc/cpuinfo", 1).split(":")[1].strip();
            if (sensorsEnabled)
            {
                String[] report = parseParameter(getReport(new String[]{"sensors"}), "Package").split("\s{1,10}");
                this.curTemp = report[3];
                this.critTemp = report[9].replaceFirst("\\)", "");
            }
        }
        catch (Exception e) {logger.createLog("CPU's not formed");}
    }

    public void refresh()
    {
        this.cpuFreq = Float.parseFloat(getLine("grep cpu /proc/cpuinfo", 2).split(":")[1].strip());
        if (this.sensorsEnabled)
        {
            String[] report = parseParameter(getReport(new String[]{"sensors"}), "Package").split("\s{1,10}");
            this.curTemp = report[3];
            this.critTemp = report[9].replaceFirst("\\)", "");
        }
    }
}