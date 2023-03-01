package com.example.myregard.utils.nfc_utils;

import android.app.Activity;
import android.content.Context;
import android.database.Observable;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.os.Build;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class nfc_basic {
//    private IsoDep mIsoDep;
    private String TAG;
    public nfc_basic(AppCompatActivity tar_act, String TAG){
        this.TAG = TAG;
    }

    public static String parse_nfc_info_str(NdefMessage mNdefMsg){
        String msg = "";
        NdefRecord mRecord = mNdefMsg.getRecords()[0];

        msg += "\nMime Type: ";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            msg += mRecord.toMimeType();
        }else {
            msg += "not avail.";
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            msg += "\nPayload: " + new String(mRecord.getPayload(), StandardCharsets.US_ASCII);
        }

        return msg;
    }

    public static IsoDep parse_tag(Tag tag){
        IsoDep mIsoDep = null;
        if (tag != null) {
            mIsoDep = IsoDep.get(tag);
            try {
                mIsoDep.connect();  //这里建立我们应用和IC卡
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        return mIsoDep;
    }

    public String readCardId(Tag tag) {
        String uid = "uid: ";
        if (tag != null) {
            byte[] ids = tag.getId();
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                uid = uid + new String(ids, StandardCharsets.US_ASCII);
            }else {
                uid += "SDK low";
            }
        }else {
            uid += "null tag";
        }
        return uid;
    }
}
