package com.example.plugindemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;

import dji.ux.c.q;
import dji.ux.internal.SlidingDialog;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        q.a(this, "", "", new SlidingDialog.OnEventListener() {
            @Override
            public void onLeftBtnClick(DialogInterface dialogInterface, int i) {

            }

            @Override
            public void onRightBtnClick(DialogInterface dialogInterface, int i) {

            }

            @Override
            public void onCbChecked(DialogInterface dialogInterface, boolean b, int i) {

            }
        });
    }
}