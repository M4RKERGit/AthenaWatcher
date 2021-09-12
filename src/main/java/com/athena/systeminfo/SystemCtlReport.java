package com.athena.systeminfo;

import com.athena.AthenaSettings;
import com.athena.linuxtools.Logger;
import lombok.Getter;
import lombok.SneakyThrows;

import java.util.ArrayList;

@Getter
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
        this.serviceList[0] = new Service(AthenaSettings.Services.I);
        this.serviceList[1] = new Service(AthenaSettings.Services.II);
        this.serviceList[2] = new Service(AthenaSettings.Services.III);
        this.report = serviceAnalyze();
        this.neofetch = new Neofetch();
        logger.createLog("Service report successfully created");
    }

    @SneakyThrows
    public void refresh()
    {
        this.serviceList[0] = new Service(AthenaSettings.Services.I);
        this.serviceList[1] = new Service(AthenaSettings.Services.II);
        this.serviceList[2] = new Service(AthenaSettings.Services.III);
        this.report = serviceAnalyze();
        this.neofetch = new Neofetch();
    }

    public ArrayList<String> serviceAnalyze()
    {
        ArrayList<String> toRet = new ArrayList<>();
        for (Service service : this.serviceList)
        {
            if (!service.isDefined())
            {
                toRet.add("Service " + service.getServiceName() + "undefined");
                continue;
            }
            if (service.getActivity().contains("dead"))
            {
                toRet.add("Service " + service.getServiceName() + " isn't active, press button to restart");
            }
            else toRet.add("Service " + service.getServiceName() + " is running well");
        }
        return toRet;
    }
}
