package com.dantsu.thermalprinter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import com.dantsu.thermalprinter.EsitoStampa;
import com.dantsu.thermalprinter.async.DataBridge;

@RequiresApi(api = Build.VERSION_CODES.O)
public class PollingWorker extends Worker {

    private boolean esitoStampa = false;
    private static String bluetoothData;

    private static String Data;

    private static final String ACTION_PRINT_BLUETOOTH = "com.example.ACTION_PRINT_BLUETOOTH";

    private static final String ACTION_PRINT_USB = "com.example.ACTION_PRINT_USB";

    private static final String TAG = "PollingWorker";

    public PollingWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }


    @NonNull
    @Override
    public Result doWork() {
        Log.d(TAG, "PollingWorker started");

        try {
            // Perform your periodic task here
            Log.d(TAG, "Performing periodic task...");

            // Example: Make API request
            makeApiRequest();

            Log.d(TAG, "PollingWorker completed successfully");
            return Result.success();
        } catch (Exception e) {
            Log.e(TAG, "Error in PollingWorker: " + e.getMessage(), e);
            return Result.failure();
        }
    }

    // Method to make API request (implement your actual API call here)
    private void makeApiRequest() {
        DataBridge.setData("");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://sersebasti.ddns.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        Call<List<ApiResponseItem>> call = apiService.getData();

        try {
            Response<List<ApiResponseItem>> response = call.execute();
            if (response.isSuccessful() && response.body() != null) {

                //Log.d(TAG, lastToProductionISODate);
                Log.d(TAG, "Start API Response");


                List<ApiResponseItem> commandas = null;
                if (response.isSuccessful() && response.body() != null) {
                    commandas = response.body();
                    Log.d(TAG, "API Response");
                    for (ApiResponseItem item : commandas) {
                        Log.d(TAG, formatApiResponseItem(item));
                    }

                    //List<ApiResponseItem> collectionNewThings = filterByLastToProductionDate(commandas, lastToProductionISODate);
                } else {
                    Log.e(TAG, "Response code: " + response.code());
                }

                Log.d(TAG, "End API Response");

                List<ApiResponseItem> recentItems = filterRecentProductionItems(commandas);

                if(recentItems.size() > 0){

                    /*
                    int k = 0;
                    while (k < 10){
                        Log.d(TAG, "Da stampare: " + "test");
                        Data = "Da stampare: " + "test";
                        updateBluetoothData(Data);
                        Intent intent = new Intent(ACTION_PRINT_BLUETOOTH);
                        getApplicationContext().sendBroadcast(intent);
                        Thread.sleep(4000);
                        k++;
                    }
                    /**/


                        Map<Integer, List<ApiResponseItem>> resultMap = splitListByTavoloId(recentItems);

                        // Now resultMap contains lists of ApiResponseItem objects grouped by commanda__id
                        for (Map.Entry<Integer, List<ApiResponseItem>> entry : resultMap.entrySet()) {
                            int commandaId = entry.getKey();
                            List<ApiResponseItem> subList = entry.getValue();

                            DataBridge.setData(prepareTextToPrint(subList));
                            //DataBridge.setData("Ramdom data to check");

                            Intent intent = new Intent(ACTION_PRINT_USB);
                            getApplicationContext().sendBroadcast(intent);

                            while(!esitoStampa){
                                esitoStampa = EsitoStampa.getEsito();
                                Thread.sleep(4000);
                            }
                            esitoStampa = false;

                            Thread.sleep(1000);

                        }




                        //DataBridge.setData("Ramdom data to check");

                        Intent intent = new Intent(ACTION_PRINT_USB);
                        getApplicationContext().sendBroadcast(intent);

                        while(!esitoStampa){
                            esitoStampa = EsitoStampa.getEsito();
                            Thread.sleep(4000);
                        }
                        esitoStampa = false;

                        Thread.sleep(1000);





                }




            } else {
                Log.e(TAG, "API request failed with response code: " + response.code());
            }
        } catch (Exception e) {
            Log.e(TAG, "API request error: " + e.getMessage(), e);
        }
    }






    public List<ApiResponseItem> filterRecentProductionItems(List<ApiResponseItem> apiResponseItems) {

        Log.d(TAG, "Start filterRecentProductionItems");

        List<ApiResponseItem> recentProductionItems = new ArrayList<>();

        for (ApiResponseItem item : apiResponseItems) {

            String itemDateTimeStr = item.getCommanda__to_production();
            String handlerDateTimeStr = MainActivity.UTCDate_handler.getDateTimeStr();

            Log.d(TAG, "Commanda item ID: " + item.getCommanda__id());
            Log.d(TAG, "Initial Internal DateTime value: " + handlerDateTimeStr);
            Log.d(TAG, "Commanda item DateTime value: " + itemDateTimeStr);

            if (itemDateTimeStr != null) {
                // Compare the item datetime with handler datetime
                // && item.getCommanda__production_status() == "B" && item.getCommanda__product__collection_id() == 1

                if((itemDateTimeStr.compareTo(handlerDateTimeStr) > 0)){
                    Log.d(TAG, "Condition datetime - ok ");
                }else {
                    Log.d(TAG, "Condition datetime - ko " + itemDateTimeStr.compareTo(handlerDateTimeStr));
                }
                if(item.getCommanda__product__collection_id() == 1){
                    Log.d(TAG, "Condition collection - ok ");
                }else {
                    Log.d(TAG, "Condition collection - ko ");
                }

                if("B".equals(item.getCommanda__production_status())){
                    Log.d(TAG, "Condition status B - ok ");
                }
                else {
                    Log.d(TAG, "Condition status B - ko " + item.getCommanda__production_status());
                }

                if ( (itemDateTimeStr.compareTo(handlerDateTimeStr) > 0)  && (item.getCommanda__product__collection_id() == 1) && ("B".equals(item.getCommanda__production_status())) ) {
                    recentProductionItems.add(item);
                }
            }


        }

        if(getMaxProductionDateTime(recentProductionItems) != null){MainActivity.UTCDate_handler.setDateTimeFromString(getMaxProductionDateTime(recentProductionItems));}


        Log.d(TAG, "Number of recent production items: " + recentProductionItems.size());
        Log.d(TAG, "Final Internal DateTime value: " + MainActivity.UTCDate_handler.getDateTimeStr());
        Log.d(TAG, "Stop filterRecentProductionItems");

        return recentProductionItems;
    }

    public String prepareTextToPrint(List<ApiResponseItem> sublist){

        //String originalString = item.getCommanda__product__title();
        //String wordToRemove = "Pizza";


        // Use regular expression to remove the word
        //String result_product__title = originalString.replaceAll("\\b" + wordToRemove + "\\b", "").trim();
        int Commanda_ID = sublist.get(0).getCommanda__id();
        String Tavolo = sublist.get(0).getNome();


        String noteLine = "";




        // "\n[L]<font size='big'>Tav: " + removeSpecialChars(item.getNome()) + "</font>" +

        String restult = "[L]\n" + "[L]\n" +

                "\n[L]<u><font size='big'>Tavolo: " + Tavolo + "</font></u>" + "[L]\n";
                //"\n[L]<u><font size='big'>" + item.getCommanda__quantity() + "x: " + item.getCommanda__product__title() + "</font></u>" +
                //noteLine +


        for (ApiResponseItem item : sublist) {

            restult += "\n" + "[L]<u><font size='big'>" + item.getCommanda__quantity() + "x: " + item.getCommanda__product__title() + "</font></u>" + "[L]\n";

            if (item.getCommanda__note() != null && item.getCommanda__note().length() > 0) {
                restult += "\n[L]<u><font size='big'>Note: " + item.getCommanda__note() + "</font></u>" + "[L]\n";
            }

        }

        restult += "[L]\n" + "[L]\n";


        return  restult;
    }

    public static String removeSpecialChars(String input) {
        // Regular expression to match special characters
        String regex = "[^a-zA-Z0-9\\s]";

        // Replace special characters with spaces
        String result = input.replaceAll(regex, " ");

        // Remove extra spaces
        result = result.replaceAll("\\s+", " ");

        return result;
    }

    public String getMaxProductionDateTime(List<ApiResponseItem> recentProductionItems) {
        String maxDateTime = null;

        for (ApiResponseItem item : recentProductionItems) {
            String itemDateTime = item.getCommanda__to_production();

            // Compare itemDateTime with maxDateTime
            if (maxDateTime == null || itemDateTime.compareTo(maxDateTime) > 0) {
                maxDateTime = itemDateTime;
            }
        }

        return maxDateTime;
    }

    public static boolean checkNewCondition(String data1, String data2){
        return true;
    }

    public static void updateBluetoothData(String data) {bluetoothData = data;}

    public static String getBluetoothData() {return bluetoothData;}

    private String formatApiResponseItem(ApiResponseItem item) {
        return "ID: " + item.getId() +
                "\nNome: " + item.getNome() +
                "\nProduct Title: " + item.getCommanda__product__title() +
                //"\nCoperti: " + item.getCoperti() +
                //"\nCommanda ID: " + item.getCommanda__id() +
                //"\nProduct ID: " + item.getCommanda__product_id() +
                "\nQuantity: " + item.getCommanda__quantity() +
                //"\nProduction Status: " + item.getCommanda__production_status() +
                "\nNote: " + item.getCommanda__note();
                //"\nTo Production: " + item.getCommanda__to_production() +
                //"\nProduct Price: " + item.getCommanda__product__price() +
                //"\nProduct Collection ID: " + item.getCommanda__product__collection_id() +
                //"\nProduct Tipo Prodotto ID: " + item.getCommanda__product__tipo_prodotto_id()

    }

    public Map<Integer, List<ApiResponseItem>> splitListByTavoloId(List<ApiResponseItem> itemList) {
        Map<Integer, List<ApiResponseItem>> resultMap = new HashMap<>();

        for (ApiResponseItem item : itemList) {
            int TavoloId = item.getId();
            List<ApiResponseItem> subList = resultMap.getOrDefault(TavoloId, new ArrayList<>());
            subList.add(item);
            resultMap.put(TavoloId, subList);
        }

        return resultMap;
    }


}