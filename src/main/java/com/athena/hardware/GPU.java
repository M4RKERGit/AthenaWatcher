package com.athena.hardware;

public class GPU extends Device
{
    public String description, product, vendor, clock;

    public GPU()
    {
        this.description = getLine("lshw -c video", 3);
        this.product = getLine("lshw -c video", 4);
        this.vendor = getLine("lshw -c video", 5);
        this.clock = getLine("lshw -c video", 6);
        System.out.println("GPU formed\n");
    }
}
