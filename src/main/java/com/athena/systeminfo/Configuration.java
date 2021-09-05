package com.athena.systeminfo;

import lombok.Getter;

@Getter
public class Configuration
{
    private final int cooldown;
    private boolean refreshEnabled = true;

    public Configuration()
    {
        this.cooldown = Integer.parseInt("3000")/1000;
    }

    public void setRefreshEnabled(boolean refreshEnabled) {this.refreshEnabled = refreshEnabled;}
}
