package com.example.myregard.utils.obj_utils;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

public class book_nfc_json extends book_json{
    public boolean is_borrowed=false;
    public String docs = "no docs";

    static public book_nfc_json get_from_json(String source){
        Gson gson = new Gson();
        return gson.fromJson(source, book_nfc_json.class);
    }

    @NonNull
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
