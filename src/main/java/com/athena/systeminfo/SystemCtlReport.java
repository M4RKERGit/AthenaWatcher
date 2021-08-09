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
    private Service[] serviceList = new Service[3];
    private ArrayList<String> report;
    private Logger logger = new Logger("[SCR]");

    @SneakyThrows
    public SystemCtlReport()
    {
        FileReader fr = null;
        try
        {
            fr = new FileReader("services.txt");
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(fr);
        for (int i = 0; i < serviceList.length; i++)
        {
            serviceList[i] = new Service(reader.readLine());
        }
        report = serviceAnalyze();
        logger.createLog("Service report successfully created");
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
}
