package com.athena.hardware;

import java.util.ArrayList;

public class HWInfo
{
    public CPU cpu = new CPU();
    public GPU gpu = new GPU(); //TODO: GPU info is very slow, optimization needed (lshw is heavy)
    public Memory memory = new Memory();
    public ArrayList<String> report = hardwareAnalyze();

    public ArrayList<String> hardwareAnalyze()
    {
        ArrayList<String> toRet = new ArrayList<>();
        if (cpu.determineOverheat()) toRet.add("CPU is hot, do you want to halt services?");
        else toRet.add("CPU temperature is OK");
        if (gpu.determineOverheat()) toRet.add("GPU is hot, do you want to halt DE?");
        else toRet.add("GPU temperature is OK");
        if (memory.ram.determineBusy()) toRet.add("Memory is filled, do you want to free memory?");
        else toRet.add("Memory is OK");
        return toRet;
    }
}
