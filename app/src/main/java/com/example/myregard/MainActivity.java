package com.example.myregard;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myregard.databinding.ActivityMainBinding;
import com.example.myregard.utils.nfc_utils.nfc_activity;
import com.example.myregard.utils.nfc_utils.nfc_basic;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.Objects;

public class MainActivity extends nfc_activity {

    private ActivityMainBinding binding;

    private TextView text_home;
    private TextView text_dashboard;

    private final String TAG = "<main activity>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

//        init texts
        text_home = findViewById(R.id.text_home);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            nfc_init();
        }else {
            Log.e(TAG,"SDK not support nfc.");
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        Parcelable[] rawArray = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        NdefMessage mNdefMsg = (NdefMessage) rawArray[0];
        NdefRecord mRecord = mNdefMsg.getRecords()[0];

        String mime = "null";
        text_home = findViewById(R.id.text_home);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            mime = mRecord.toMimeType();
        }

        switch (mime){
            case "obj/stu_obj":{
                Intent tar = new Intent(this, editActivity.class);
                startActivity(tar);
                break;
            }
            case "obj/book_obj":{
                Intent tar = new Intent(this, bookActivity.class);
                tar.putExtra("data", mRecord.getPayload());
                startActivity(tar);
                break;
            }
            default:{
                Toast.makeText(this,"card type unfit.", Toast.LENGTH_LONG).show();
                text_home.setText(mime);
                break;
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}