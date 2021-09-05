package com.athena.systeminfo;

import com.athena.linuxtools.Logger;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

public class SystemCtlReport
{
    private final String infoType = "SERVICE";
    private final Service[] serviceList = new Service[3];
    private ArrayList<String> report;
    private Neofetch neofetch;
    private static final Logger logger = new Logger("[SCR]");

    @SneakyThrows
    public SystemCtlReport()
    {
        FileReader fr = null;
        try {fr = new FileReader("services.txt");}
        catch (FileNotFoundException e) {logger.createLog("File 'services.txt' not found");}

        BufferedReader reader = new BufferedReader(fr);
        for (int i = 0; i < serviceList.length; i++)
        {
            this.serviceList[i] = new Service(reader.readLine());
        }
        this.report = serviceAnalyze();
        this.neofetch = new Neofetch();
        logger.createLog("Service report successfully created");
    }

    @SneakyThrows
    public void refresh()
    {
        FileReader fr = null;
        try {fr = new FileReader("services.txt");}
        catch (FileNotFoundException e) {logger.createLog("File 'services.txt' not found");}

        BufferedReader reader = new BufferedReader(fr);
        for (int i = 0; i < serviceList.length; i++)
        {
            this.serviceList[i] = new Service(reader.readLine());
        }
        this.report = serviceAnalyze();
        this.neofetch = new Neofetch();
    }

    public ArrayList<String> serviceAnalyze()
    {
        ArrayList<String> toRet = new ArrayList<>();
        for (Service service : this.serviceList)
        {
            if (!service.defined)
            {
                toRet.add("Service " + service.serviceName + "undefined");
                continue;
            }
            if (service.activity.contains("dead"))
            {
                toRet.add("Service " + service.serviceName + " isn't active, press button to restart");
            }
            else toRet.add("Service " + service.serviceName + " is running well");
        }
        return toRet;
    }

    public String getInfoType() {return infoType;}
    public Service[] getServiceList() {return serviceList;}
    public ArrayList<String> getReport() {return report;}
    public Neofetch getNeofetch() {return neofetch;}
}
