package com.example.myregard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import com.example.myregard.utils.nfc_utils.nfc_activity;
import com.example.myregard.utils.nfc_utils.nfc_basic;
import com.example.myregard.utils.obj_utils.student_json;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class WriteNfcActivity extends nfc_activity {

    byte[] data;
    String mime_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_nfc);

        Intent intent =getIntent();
        data = intent.getStringExtra("data").getBytes();

        mime_type = intent.getStringExtra("mime_type");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Toast.makeText(this, "attach card to reader.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        try {
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            Ndef ndef = Ndef.get(tag);
            ndef.connect();
            NdefRecord ndefRecord = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                ndefRecord = NdefRecord.createMime(mime_type, data);
            }
            NdefRecord[] records = {ndefRecord};
            NdefMessage ndefMessage = new NdefMessage(records);
            ndef.writeNdefMessage(ndefMessage);
            Toast.makeText(this,"nfc success",Toast.LENGTH_LONG).show();


            this.finish();
        } catch (FormatException | IOException e) {
            Toast.makeText(this,"nfc fail",Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}