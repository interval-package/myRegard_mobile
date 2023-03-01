package com.example.myregard.utils.net_utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.collection.ArraySet;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class wifi_api {

    String TAG = "<wifi scan>";

    Context context;
    WifiManager wifiManager;
    BroadcastReceiver wifiScanReceiver;
    IntentFilter intentFilter;

    public wifi_api(Context _context){
        context = _context;
        wifiManager = (WifiManager)
                context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        wifiScanReceiver = new BroadcastReceiver() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onReceive(Context c, Intent intent) {
                boolean success = intent.getBooleanExtra(
                        WifiManager.EXTRA_RESULTS_UPDATED, false);
                if (success) {
                    scanSuccess();
                } else {
                    // scan failure handling
                    scanFailure();
                }
            }
        };

        intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        context.registerReceiver(wifiScanReceiver, intentFilter);

        wifiManager.setWifiEnabled(true);

    }

    public void get_wifi_info(){
        boolean success = wifiManager.startScan();
        if (!success) {
            // scan failure handling
            Toast.makeText(context, "scan fail", Toast.LENGTH_SHORT).show();
            scanFailure();
        }else {
            Toast.makeText(context, "scan success", Toast.LENGTH_SHORT).show();
        }

    }

    public List<ScanResult> results;

    private void scanSuccess() {
        results = wifiManager.getScanResults();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            results.sort(new Comparator<ScanResult>() {
                @Override
                public int compare(ScanResult o1, ScanResult o2) {
                    return o1.level - o2.level;
                }
            });
        }

        ArrayList<String> arrayList = new ArrayList<>();

        for (ScanResult scanResult : results){
            String tar = parse_wifi_res(scanResult);
            Log.d(TAG, tar);
            arrayList.add(tar);
        }

    }

    static public String[][] parse_wifi_info_str_mat(List<ScanResult> results){
        String[][] res = new String[results.size()][2];
        for (int i=0; i<results.size(); i++){
            res[i][0] = results.get(i).BSSID;
            res[i][1] = String.valueOf(results.get(i).level);
        }
        return res;
    }

    static public String parse_wifi_res(ScanResult scanResult){
        return "SSID: "+scanResult.SSID +"\tBSSID: " + scanResult.BSSID+ "\tLEVEL: "+scanResult.level;
    }

    private void scanFailure() {
        // handle failure: new scan did NOT succeed
        // consider using old scan results: these are the OLD results!
        results = wifiManager.getScanResults();
    }


    boolean scanWifi(WifiManager wifiManagerToScan) {

//        String[] perm = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.CHANGE_WIFI_STATE};

        boolean scanStarted = wifiManagerToScan.startScan();

        if (scanStarted) {
            Toast.makeText(context, "Scanning...", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}


