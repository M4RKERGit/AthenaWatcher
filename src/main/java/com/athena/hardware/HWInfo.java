package com.athena.hardware;

import java.util.ArrayList;

public class HWInfo extends Device
{
    public boolean sensorsEnabled;
    public CPU cpu;
    public GPU gpu;
    public Memory memory;
    public ArrayList<String> report;

    public HWInfo()
    {
        if (getReport(new String[]{"sensors"}).size() < 5) this.sensorsEnabled = false;
        else this.sensorsEnabled = true;
        this.cpu = new CPU(this.sensorsEnabled);
        this.gpu = new GPU(this.sensorsEnabled); //TODO: GPU info is very slow, optimization needed (lshw is heavy)
        this.memory = new Memory();
        report = hardwareAnalyze(this.sensorsEnabled);
    }

    public ArrayList<String> hardwareAnalyze(boolean sensorsEnabled)
    {
        ArrayList<String> toRet = new ArrayList<>();
        System.out.println("Analyze...");
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
}
