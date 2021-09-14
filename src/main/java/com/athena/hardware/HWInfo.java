package com.athena.hardware;

import com.athena.linuxtools.Logger;
import java.util.ArrayList;

public class HWInfo extends Device
{
    private final String infoType = "HARDWARE";
    private final boolean sensorsEnabled;
    private static CPU cpu;
    private static GPU gpu;
    private static Memory memory;
    private ArrayList<String> report;
    private final Logger logger = new Logger("[HWI]");

    public HWInfo()
    {
        this.sensorsEnabled = getReport(new String[]{"sensors"}).size() >= 5;
        cpu = new CPU(this.sensorsEnabled);
        gpu = new GPU(this.sensorsEnabled);
        memory = new Memory();
        report = hardwareAnalyze(this.sensorsEnabled);
        logger.createLog("Got hardware info, sensors condition: " + this.sensorsEnabled);
    }

    public void refresh()
    {
        cpu.refresh();
        gpu.refresh();
        memory.refresh();
        report = hardwareAnalyze(this.sensorsEnabled);
    }

    public ArrayList<String> hardwareAnalyze(boolean sensorsEnabled)
    {
        ArrayList<String> toRet = new ArrayList<>();
        if (sensorsEnabled)
        {
            if (cpu.determineOverheat()) toRet.add("CPU is hot, do you want to halt services?");
            else toRet.add("CPU temperature is OK");
            if (gpu.determineOverheat()) toRet.add("GPU is hot, do you want to halt DE?");
            else toRet.add("GPU temperature is OK");
        }
        if (memory.ram.determineBusy()) toRet.add("Memory is filled, do you want to free memory?");
        else toRet.add("Memory is OK");
        return toRet;
    }

    public boolean isSensorsEnabled() {return sensorsEnabled;}
    public CPU getCpu() {return cpu;}
    public GPU getGpu() {return gpu;}
    public Memory getMemory() {return memory;}
    public ArrayList<String> getReport() {return report;}
    public String getInfoType() {return infoType;}
}
