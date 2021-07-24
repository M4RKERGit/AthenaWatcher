package com.athena.hardware;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        public boolean determineBusy()
        {
            float cur = 0, crit = 0;
            Pattern pattern = Pattern.compile("[-]?[0-9]+(.[0-9]+)?");
            Matcher matcher = pattern.matcher(this.used);
            if (matcher.find())
            {
                String value = this.used.substring(matcher.start(), matcher.end());
                value = value.replaceAll(",", ".");
                cur = Float.parseFloat(value);
            }
            matcher = pattern.matcher(this.total);
            if (matcher.find())
            {
                String value = this.total.substring(matcher.start(), matcher.end());
                value = value.replaceAll(",", ".");
                crit = Float.parseFloat(value);
            }
            System.out.println("RAM: " + cur + "/" + crit);
            if ((crit * 0.8) < cur)
            {
                System.out.println("!!!Filled!!!");
                return true;
            }
            else System.out.println("OK");
            return false;
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