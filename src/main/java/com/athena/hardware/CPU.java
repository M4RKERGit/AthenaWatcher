package com.athena.hardware;

public class CPU extends Device
{
    public String manufacturer;
    public String modelName;
    public float cpuFreq;
    public long cacheSize;
    public int cores;
    public String FPU;

    public CPU()
    {
        this.manufacturer = getLine("grep vendor_id /proc/cpuinfo", 1).split(":")[1];
        this.modelName = getLine("grep model /proc/cpuinfo", 2).split(":")[1];
        this.cpuFreq = Float.parseFloat(getLine("grep cpu /proc/cpuinfo", 2).split(":")[1]);
        this.cacheSize = Long.parseLong(getLine("grep cache /proc/cpuinfo", 1).split(":")[1].split(" ")[1]);
        this.cores = Integer.parseInt(getLine("nproc", 1));
        this.FPU = getLine("grep fpu /proc/cpuinfo", 1).split(":")[1];
        System.out.println("CPU formed\n");
    }


}