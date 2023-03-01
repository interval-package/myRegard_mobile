package com.example.myregard;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myregard.utils.net_utils.base_net_api;
import com.example.myregard.utils.nfc_utils.nfc_activity;
import com.example.myregard.utils.obj_utils.book_net_json;
import com.example.myregard.utils.obj_utils.book_nfc_json;
import com.example.myregard.utils.obj_utils.student_json;
import com.google.gson.Gson;

import java.util.Arrays;

public class bookActivity extends nfc_activity {
    TextView book_name;
    TextView book_id;
    TextView book_detail;

    String TAG = "<book action>";

    book_nfc_json book_obj;

    byte[] raw_data;

    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        book_name = findViewById(R.id.book_name);
        book_id = findViewById(R.id.book_id);
        book_detail = findViewById(R.id.book_detail);

        Intent intent =getIntent();
        raw_data = intent.getByteArrayExtra("data");
        book_obj = gson.fromJson(new String(raw_data), book_nfc_json.class);

        book_name.setText(book_obj.book_name);
        book_id.setText(book_obj.book_id);
        book_detail.setText(book_obj.docs);

        if(book_obj.is_borrowed){
            Toast.makeText(this, "this book is rented", Toast.LENGTH_SHORT).show();
        }

//        book_id.setText(id);
    }

    public void btn_clicked(View v){
//        nfc_activity temp = this;
//        Thread th = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                String info = null;
//                try {
//                    info = base_net_api.get_from_tar(base_net_api.base + "/rent_book?book_id=" + book_obj.book_id);
//                    Looper.prepare();
//                    Toast.makeText(temp, "已经成功借阅", Toast.LENGTH_SHORT).show();
//                    Looper.loop();
//                    temp.finish();
//                } catch (Exception e) {
//                    info = "fail.";
//                    e.printStackTrace();
//                }
//                info = "get info: " + info;
//                Log.i(TAG, info);
//            }
//        });
//        th.start();

        Intent intent = new Intent(this, editActivity.class);
        intent.putExtra("book_id", book_obj.book_id);
        startActivity(intent);
    }
}