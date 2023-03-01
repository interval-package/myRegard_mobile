package com.example.myregard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myregard.utils.nfc_utils.nfc_activity;
import com.example.myregard.utils.obj_utils.book_json;
import com.example.myregard.utils.obj_utils.book_nfc_json;
import com.example.myregard.utils.obj_utils.student_json;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class editBookActivity extends nfc_activity {

    TextView edit_id, edit_name;
    book_nfc_json book_obj = new book_nfc_json();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book);

        edit_id = findViewById(R.id.edit_book_id);
        edit_name = findViewById(R.id.edit_book_name);

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

            if(Objects.equals(mime_type, book_json.mime_type)){
                Toast.makeText(this, "read from card", Toast.LENGTH_LONG).show();
                book_obj = book_nfc_json.get_from_json(data);
                edit_id.setText(book_obj.book_id);
                edit_name.setText(book_obj.book_name);
            }else {
                Toast.makeText(this, "type error, target: " +
                                student_json.mime_type + " get: " + mime_type,
                        Toast.LENGTH_LONG).show();
            }
            startActivity(new Intent(this, editActivity.class));
        }
    }

    public void write_book_info(View v){
        Intent intent = new Intent(this, WriteNfcActivity.class);
        book_obj.is_borrowed = false;
        book_obj.book_name = edit_name.getText().toString();
        book_obj.book_id = edit_id.getText().toString();
        intent.putExtra("data", book_obj.toString());
        intent.putExtra("mime_type", book_json.mime_type);
        startActivity(intent);
    }

    public void return_action(View v){
        Toast.makeText(this,"return book success", Toast.LENGTH_LONG).show();
    }
}