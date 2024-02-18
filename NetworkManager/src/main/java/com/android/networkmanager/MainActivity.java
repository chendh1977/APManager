package com.android.networkmanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.networkmanager.managerActivity.ApManagerActivity;
import com.android.networkmanager.managerActivity.WifiManagerActivity;

public class MainActivity extends Activity implements View.OnClickListener {

    private static String TAG = "NetworkManager::MainActivity";

    private Button wifiButton;

    private Button apButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wifiButton = (Button) findViewById(R.id.wifi_list);
        wifiButton.setOnClickListener(this);
        apButton = (Button) findViewById(R.id.ap_list);
        apButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.wifi_list) {
            Intent intent = new Intent(this, WifiManagerActivity.class);
            startActivity(intent);
        } else if(v.getId() == R.id.ap_list) {
            Intent intent = new Intent(this, ApManagerActivity.class);
            startActivity(intent);
        }

    }
}