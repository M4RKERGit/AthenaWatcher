package com.athena.systeminfo;

public class Configuration
{
    private int cooldown;
    private boolean refreshEnabled = true;

    public Configuration()
    {
        this.cooldown = Integer.parseInt("3000")/1000;
    }

    public int getCooldown() {return cooldown;}
    public boolean isRefreshEnabled() {return refreshEnabled;}
    public void setRefreshEnabled(boolean refreshEnabled) {this.refreshEnabled = refreshEnabled;}
}
