package com.athena.hardware;

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

    public CPU()
    {
        this.manufacturer = getLine("grep vendor_id /proc/cpuinfo", 1).split(":")[1].strip();
        this.modelName = getLine("grep model /proc/cpuinfo", 2).split(":")[1].strip();
        this.cpuFreq = Float.parseFloat(getLine("grep cpu /proc/cpuinfo", 2).split(":")[1].strip());
        this.cacheSize = Long.parseLong(getLine("grep cache /proc/cpuinfo", 1).split(":")[1].split(" ")[1].strip());
        this.cores = Integer.parseInt(getLine("nproc", 1).strip());
        this.FPU = getLine("grep fpu /proc/cpuinfo", 1).split(":")[1].strip();
        this.curTemp = parseParameter(getReport(new String[]{"sensors"}), "Package").split("\s{1,10}")[3];
        this.critTemp = parseParameter(getReport(new String[]{"sensors"}), "Package").split("\s{1,10}")[9];
        System.out.println("CPU formed\n");
    }
}