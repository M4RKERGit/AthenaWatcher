package com.athena.hardware;

import com.athena.linuxtools.ProcessParsing;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@Slf4j
public class Memory extends ProcessParsing
{
    private final RAM ram;
    private final Swap swap;

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

    @Getter
    public class RAM
    {
        private String total;
        private String used;
        private String free;
        private String shared;
        private String cache;
        private String available;

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
            return (crit * 0.8) < cur;
        }
    }

    @Getter
    public class Swap
    {
        private String total;
        private String used;
        private String free;

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