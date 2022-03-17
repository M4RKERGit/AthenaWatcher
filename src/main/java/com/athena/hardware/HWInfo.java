package com.athena.hardware;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@Slf4j
@Getter
public class HWInfo extends Device
{
    private final String infoType = "HARDWARE";
    private final boolean sensorsEnabled;
    private static CPU cpu;
    private static GPU gpu;
    private static Memory memory;
    private ArrayList<String> report;

    public HWInfo()
    {
        this.sensorsEnabled = getReport(new String[]{"sensors"}).size() >= 5;
        cpu = new CPU(this.sensorsEnabled);
        gpu = new GPU(this.sensorsEnabled);
        memory = new Memory();
        report = hardwareAnalyze(this.sensorsEnabled);
        log.info("Got hardware info, sensors condition: " + this.sensorsEnabled);
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
        if (memory.getRam().determineBusy()) toRet.add("Memory is filled, do you want to free memory?");
        else toRet.add("Memory is OK");
        return toRet;
    }
}