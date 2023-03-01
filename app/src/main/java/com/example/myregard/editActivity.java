package com.example.myregard;

import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.myregard.utils.net_utils.base_net_api;
import com.example.myregard.utils.nfc_utils.nfc_activity;
import com.example.myregard.utils.nfc_utils.nfc_basic;
import com.example.myregard.utils.obj_utils.student_json;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

//import com.tencent.tencentmap.mapsdk.maps.TencentMapInitializer;

public class editActivity extends nfc_activity {

    EditText id_edit;
    EditText pwd_edit;

    student_json stu_obj;

    byte[] data;

    String book_id;

    Button btn_rent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Intent intent =getIntent();
        String temp = intent.getStringExtra("data");
        if(temp != null){
            Toast.makeText(this, "get info from upper page.", Toast.LENGTH_LONG).show();
            data = temp.getBytes();
        }

        temp = intent.getStringExtra("book_id");
        if(temp != null){
            book_id = temp;
            LinearLayout linear=(LinearLayout) findViewById(R.id.edit_lin);
            btn_rent = new Button(getApplicationContext());
            btn_rent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rent();
                }
            });
            linear.addView(btn_rent);
        }

        id_edit = (EditText) findViewById(R.id.edit_id);
        pwd_edit = (EditText) findViewById(R.id.edit_pwd);
    }

    public void btn_clicked(View view) {
        Intent intent = new Intent(this, WriteNfcActivity.class);
        stu_obj = new student_json(id_edit, pwd_edit);
        intent.putExtra("data", stu_obj.toString());
        intent.putExtra("mime_type", student_json.mime_type);
        startActivity(intent);
    }


    private void showNormalDialog(){
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(this);
        normalDialog.setIcon(R.drawable.ic_launcher_foreground);
        normalDialog.setTitle("user check fail.");
        normalDialog.setMessage("do you want register?");
        normalDialog.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                    }
                });
        normalDialog.setNegativeButton("not",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                    }
                });
        // 显示
        normalDialog.show();
    }

    public void check_login(View view){
        this.showNormalDialog();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Parcelable[] rawArray = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        NdefMessage mNdefMsg = (NdefMessage) rawArray[0];
        NdefRecord mRecord = mNdefMsg.getRecords()[0];
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            String data = new String(mRecord.getPayload(), StandardCharsets.US_ASCII);
            String mime_type = mRecord.toMimeType();

            if(Objects.equals(mime_type, student_json.mime_type)){
                Toast.makeText(this, "read from card", Toast.LENGTH_LONG).show();
                stu_obj = student_json.get_from_json(data);
                id_edit.setText(stu_obj.id);
                pwd_edit.setText(stu_obj.pwd);
            }else {
                Toast.makeText(this, "type error, target: " +
                        student_json.mime_type + " get: " + mime_type,
                        Toast.LENGTH_LONG).show();
            }
//            startActivity(new Intent(this, editActivity.class));
        }
    }

    public void rent() {
        stu_obj = new student_json(id_edit, pwd_edit);
        nfc_activity temp = this;
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                String info = null;
                try {
                    info = base_net_api.get_from_tar(base_net_api.base +
                            "/rent_book?book_id=" + book_id +
                            "&user_id=" + stu_obj.id +
                            "&user_pwd=" + stu_obj.pwd);
                    Looper.prepare();
                    Toast.makeText(temp, "已经成功借阅", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                    temp.finish();
                } catch (Exception e) {
                    info = "fail.";
                    e.printStackTrace();
                }
                info = "get info: " + info;
                Log.i(TAG, info);
            }
        });
        th.start();

    }
}