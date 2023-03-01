package com.example.myregard.utils.obj_utils;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

public class book_json {
    public String book_id;
    public String book_name;
    static final public String mime_type = "obj/book_obj";

    @NonNull
    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
