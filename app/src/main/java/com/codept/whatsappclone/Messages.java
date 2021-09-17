package com.codept.whatsappclone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Messages extends AppCompatActivity {
    private String messageID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        messageID=getIntent().getStringExtra("messageID");



    }
}