package com.android.networkmanager.managerActivity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import com.android.networkmanager.R;
import com.android.networkmanager.Item.ClientItem;
import com.android.networkmanager.Item.ClientItemAdapter;
import com.android.networkmanager.Utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class WifiClientManagerActivity extends Activity implements View.OnClickListener {

    private static String TAG = "NetworkManager::WifiClientManagerActivity";
    private BroadcastReceiver mBroadcastReceiverWificlient;
    private ImageButton mBackButton;
    private ImageButton mFlushButton;
    private List<ClientItem> mClientItemList = new ArrayList<>();
    private ListView mClientList;
    private String mInterfaceName;
    private String mBssid;
    private ArrayList<String> mBssidList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_client_manager);

        Intent intent = getIntent();
        mBssid = intent.getExtras().getString("bssid");
        mInterfaceName = intent.getExtras().getString("interfaceName");
        mBssidList = intent.getExtras().getStringArrayList("bssidList");

        intent = new Intent("com.example.applicantion.get.WifiClient");
        intent.putExtra("apInstanceIdentifier", mInterfaceName);
        getApplicationContext().sendBroadcast(intent);

    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mBroadcastReceiverWificlient);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.back) {
            ToastUtils.show(getApplicationContext(), "退出");
            finish();
        } else if(v.getId() == R.id.flush) {
            ToastUtils.show(getApplicationContext(), "刷新");
        }
    }

    private void initViewItem() {
        mBackButton = (ImageButton) findViewById(R.id.back);
        mFlushButton = (ImageButton) findViewById(R.id.flush);
        mClientList = (ListView) findViewById(R.id.client_list);

        mBackButton.setOnClickListener(this);
        mFlushButton.setOnClickListener(this);
    }

    private void initBroadcast() {
        mBroadcastReceiverWificlient = new BroadcastReceiver() {
            @Override
            public void onReceive(Context c, Intent intent) {
                Log.i(TAG, "wifi client info onReceive");
                getListViewItem(intent);
                ClientItemAdapter clientItemAdapter = new ClientItemAdapter(mClientItemList, WifiClientManagerActivity.this, mInterfaceName, mBssid, mBssidList);
                mClientList.setAdapter(clientItemAdapter);
            }
        };

        registerReceiver(mBroadcastReceiverWificlient, new IntentFilter("com.example.applicantion.send.WifiClient"), RECEIVER_EXPORTED);
    }

    private void getListViewItem(Intent intent){
        boolean isConnected = intent.getExtras().getBoolean("isConnected");
        String macAddress = intent.getExtras().getString("MacAddress");
        String apInstanceIdentifier = intent.getExtras().getString("ApInstanceIdentifier");
        if(isConnected) {
            mClientItemList.add(new ClientItem(macAddress, apInstanceIdentifier));
        } else {
            mClientItemList.remove(new ClientItem(macAddress, apInstanceIdentifier));
        }
    }
}