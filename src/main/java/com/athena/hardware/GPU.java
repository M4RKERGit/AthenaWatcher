package com.athena.hardware;

import java.util.ArrayList;

public class GPU extends Device
{
    public String description, product, vendor, clock, curTemp, critTemp;

    public GPU()
    {
        ArrayList<String> fullReport = getReport(new String[]{"lshw", "-C", "video"});
        this.description = parseParameter(fullReport, "description").split(":")[1].strip();
        this.product = parseParameter(fullReport, "product").split(":")[1].strip();
        this.vendor = parseParameter(fullReport, "vendor").split(":")[1].strip();
        this.clock = parseParameter(fullReport, "clock").split(":")[1].strip();
        this.curTemp = parseParameter(getReport(new String[]{"sensors"}), "edge").split("\s{1,10}")[1];
        this.critTemp = parseParameter(getReport(new String[]{"sensors"}), "edge").split("\s{1,10}")[4].replaceFirst(",", "");
        System.out.println("GPU formed\n");
    }
}
