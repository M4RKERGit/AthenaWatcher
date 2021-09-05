package com.athena.hardware;

import com.athena.linuxtools.Logger;
import com.athena.linuxtools.ProcessParsing;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Memory extends ProcessParsing
{
    public RAM ram;
    public Swap swap;
    private static final Logger logger = new Logger("[MEM]");

    public Memory()
    {
        this.ram = new RAM();
        this.swap = new Swap();
    }

    public void refresh()
    {
        this.ram.refresh();
        this.swap.refresh();
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
            refresh();
        }

        public void refresh()
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
            //logger.createLog("RAM: " + cur + "/" + crit);
            if ((crit * 0.8) < cur)
            {
                //logger.createLog("Filled");
                return true;
            }
            //else logger.createLog("OK");
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

        public void refresh()
        {
            String[] reportCache = getLine("free -h", 3).split("\s{1,20}");
            this.total = reportCache[1];
            this.used = reportCache[2];
            this.free = reportCache[3];
        }
    }
}