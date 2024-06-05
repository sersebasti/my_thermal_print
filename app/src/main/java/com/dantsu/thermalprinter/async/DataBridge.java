package com.dantsu.thermalprinter.async;

public class DataBridge {

    private static String dataToPrint;

    public static void setData(String newData) {
        dataToPrint = newData;
    }

    public static String getData() {
        return dataToPrint;
    }
}
