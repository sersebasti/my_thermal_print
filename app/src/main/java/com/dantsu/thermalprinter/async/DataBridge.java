package com.dantsu.thermalprinter.async;

public class DataBridge {

    private static String dataToPrint;

    public static synchronized void setData(String newData) {
        dataToPrint = newData;
    }

    public static synchronized String getData() {
        return dataToPrint;
    }
}
