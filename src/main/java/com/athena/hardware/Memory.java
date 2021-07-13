package com.athena.hardware;

public class Memory extends Device
{
    public RAM ram;
    public Swap swap;

    public Memory()
    {
        this.ram = new RAM();
        this.swap = new Swap();
    }

    public class RAM
    {
        public String total;
        public String used;
        public String free;
        public String shared;
        public String cache;
        public String available;

        public RAM()
        {
            String[] reportRAM = getLine("free -h", 2).split("\s{1,20}");
            this.total = reportRAM[1];
            this.used = reportRAM[2];
            this.free = reportRAM[3];
            this.shared = reportRAM[4];
            this.cache = reportRAM[5];
            this.available = reportRAM[6];
        }
    }

    public class Swap
    {
        public String total;
        public String used;
        public String free;

        public Swap()
        {
            String[] reportCache = getLine("free -h", 3).split("\s{1,20}");
            this.total = reportCache[1];
            this.used = reportCache[2];
            this.free = reportCache[3];
        }
    }
}