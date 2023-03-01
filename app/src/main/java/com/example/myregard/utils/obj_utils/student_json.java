package com.example.myregard.utils.obj_utils;
import android.widget.EditText;

import com.google.gson.Gson;
import androidx.annotation.NonNull;

public class student_json {
    public String id;
    public String pwd;

    static final public String mime_type = "obj/stu_obj";

    public student_json(EditText id_edit, EditText pwd_edit){
        id = id_edit.getText().toString();
        pwd = pwd_edit.getText().toString();
    }

    static public student_json get_from_json(String source){
        Gson gson = new Gson();
        return gson.fromJson(source, student_json.class);
    }

    @NonNull
    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
