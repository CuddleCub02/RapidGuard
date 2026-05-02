package com.example.rapidguard;

public class IncidentItem {
    public String type;
    public String dateTime;
    public String location;
    public String status;
    public String riskLevel;
    public int iconRes;

    public IncidentItem(String type, String dateTime, String location,
                        String status, String riskLevel, int iconRes) {
        this.type      = type;
        this.dateTime  = dateTime;
        this.location  = location;
        this.status    = status;
        this.riskLevel = riskLevel;
        this.iconRes   = iconRes;
    }
}