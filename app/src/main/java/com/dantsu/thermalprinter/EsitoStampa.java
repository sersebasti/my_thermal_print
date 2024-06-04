package com.dantsu.thermalprinter;

public class EsitoStampa {

    private static boolean esito;

    // Method to set esito value
    public static void setEsito(boolean esito) {
        EsitoStampa.esito = esito;
    }

    // Method to get esito value
    public static boolean getEsito() {
        return esito;
    }
}
