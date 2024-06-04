package com.dantsu.thermalprinter;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class UtcDateTimeHandler {
    private String dateTimeStr;

    // Static method to set the current date-time in UTC
    public void setNowAsUtc() {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        this.dateTimeStr = now.format(formatter);
    }

    // Method to set date-time from a string
    public void setDateTimeFromString(String dateTimeStr) {
        this.dateTimeStr = dateTimeStr;
    }

    // Method to get the date-time string
    public String getDateTimeStr() {
        return this.dateTimeStr;
    }

}