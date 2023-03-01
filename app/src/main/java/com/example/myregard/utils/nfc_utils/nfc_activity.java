package com.example.myregard.utils.nfc_utils;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class nfc_activity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            nfc_init();
        }else {
            Log.e(TAG,"SDK not support nfc.");
        }
    }

    //    NFC side
    public final String TAG = "<nfc activity>";

    protected NfcAdapter my_nfc_adapter = null;
    protected PendingIntent my_nfc_pending_Intent = null;
    protected IntentFilter[] my_nfcFilters = null;
    protected String[][] my_nfcTechList = null;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    protected void checkNFCInfo(){
        Log.d(TAG, "checking nfc info");
        my_nfc_adapter = NfcAdapter.getDefaultAdapter(this);
        if(my_nfc_adapter == null){
            Log.d(TAG, "nfc fail");
        }else{
            if(!my_nfc_adapter.isEnabled()){
                Intent setNFC = new Intent(Settings.ACTION_NFC_SETTINGS);
                startActivity(setNFC);
            }
            Log.d(TAG, "nfc started");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    protected void nfc_init(){
        checkNFCInfo();
//        my_nfc_adapter.setNdefPushMessageCallback(this,this);
        my_nfc_pending_Intent = PendingIntent.getActivity(this,0,
                new Intent(this,
                        this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),0);

        IntentFilter _if = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
//        IntentFilter _if_2 = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
        try {
            _if.addDataType("*/*");
        } catch (IntentFilter.MalformedMimeTypeException e) {
            e.printStackTrace();
        }
        my_nfcFilters = new IntentFilter[] {_if};
//        my_nfcTechList = new String[][]{new String[]{NfcF.class.getName()}};
        my_nfcTechList = null;
        Log.d(TAG, "nfc init success");
    }


    public static void writeNFCToTag(String data, Intent intent) throws IOException, FormatException {
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        Ndef ndef = Ndef.get(tag);
        ndef.connect();
        NdefRecord ndefRecord = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ndefRecord = NdefRecord.createMime("text/plain", data.getBytes());
        }

        NdefRecord[] records = {ndefRecord};
        NdefMessage ndefMessage = new NdefMessage(records);
        ndef.writeNdefMessage(ndefMessage);
    }

    @Override
    protected void onResume() {
        super.onResume();
        my_nfc_adapter.enableForegroundDispatch(this, my_nfc_pending_Intent, my_nfcFilters, my_nfcTechList);
    }

    @Override
    protected void onPause() {
        my_nfc_adapter.disableForegroundDispatch(this);
        super.onPause();
    }
}
