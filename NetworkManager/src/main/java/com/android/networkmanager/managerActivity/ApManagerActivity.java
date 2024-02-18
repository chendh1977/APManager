package com.android.networkmanager.managerActivity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;


import com.android.networkmanager.R;
import com.android.networkmanager.Item.ApItem;
import com.android.networkmanager.Item.ApItemAdapter;
import com.android.networkmanager.trafficStatistics.LocalApStatustics;
import com.android.networkmanager.Utils.ToastUtils;
import com.android.networkmanager.Utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class ApManagerActivity extends Activity implements View.OnClickListener {

    private static String TAG = "NetworkManager::ApManagerActivity";
    private WifiManager mWifiManager;
    private ListView mApList;
    private ImageButton mBackButton;
    private ImageButton mFlushButton;
    private BroadcastReceiver mBroadcastReceiverSoftApInfo;
    private List<ApItem> mSoftApInfoList = new ArrayList<>();
    private LocalApStatustics mLocalApStatustics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ap_manager);

        initViewItem();
        initBroadcast();

        mWifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        mLocalApStatustics = new LocalApStatustics(getApplicationContext());

        Intent intent = new Intent("com.example.applicantion.get.SoftApInfo");
        getApplicationContext().sendBroadcast(intent);

        mLocalApStatustics.start();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.back) {ToastUtils.show(getApplicationContext(), "退出");
            finish();
        } else if(v.getId() == R.id.flush) {
            ToastUtils.show(getApplicationContext(), "刷新");
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mBroadcastReceiverSoftApInfo);
        mLocalApStatustics.stop();
        super.onDestroy();
    }

    private void initViewItem() {
        mApList = (ListView)findViewById(R.id.ap_list);
        mBackButton = (ImageButton) findViewById(R.id.back);
        mFlushButton = (ImageButton) findViewById(R.id.flush);

        mBackButton.setOnClickListener(this);
        mFlushButton.setOnClickListener(this);

        mApList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ToastUtils.show(getApplicationContext(), "点击第"+(position+1)+"个item");
                TextView ap_nstance_identifier = (TextView)view.findViewById(R.id.ap_instance_identifier);
                TextView bssidView = (TextView)view.findViewById(R.id.bssid);
                String interfaceName = ap_nstance_identifier.getText().toString();
                String bssid = bssidView.getText().toString();
                Intent intent = new Intent(ApManagerActivity.this, WifiClientManagerActivity.class);
                intent.putExtra("bssid", bssid);
                intent.putExtra("interfaceName", interfaceName);
                ArrayList<String> bssidList = new ArrayList<>();
                for(ApItem apItem : mSoftApInfoList) {
                    bssidList.add(apItem.mBssid);
                }
                intent.putStringArrayListExtra("bssidList", bssidList);
                startActivity(intent);

            }
        });
    }

    private void initBroadcast() {
        mBroadcastReceiverSoftApInfo = new BroadcastReceiver() {
            @Override
            public void onReceive(Context c, Intent intent) {
                Log.i(TAG, "ap info onReceive");
                getListViewItem(intent);
                ApItemAdapter apItemAdapter = new ApItemAdapter(mSoftApInfoList, ApManagerActivity.this);
                mApList.setAdapter(apItemAdapter);
            }
        };

        registerReceiver(mBroadcastReceiverSoftApInfo, new IntentFilter("com.example.applicantion.send.SoftApInfo"), RECEIVER_EXPORTED);
    }

    private void getListViewItem(Intent intent){
        boolean isClear = intent.getExtras().getBoolean("isClear");
        if(isClear) {
            mSoftApInfoList.clear();
            mLocalApStatustics.clearAp();
        } else {
            String ssid = intent.getExtras().getString("Ssid");
            String bssid = intent.getExtras().getString("Bssid");
            int frequency = intent.getExtras().getInt("Frequency");
            int band = Utils.getBand(frequency);
            int bandwidth = intent.getExtras().getInt("Bandwidth");
            int channelNum = Utils.convertFrequencyMhzToChannelIfSupported(frequency);
            String apInstanceIdentifier = intent.getExtras().getString("ApInstanceIdentifier");
            boolean isRemoved = intent.getExtras().getBoolean("isRemoved");
            ApItem apItem = new ApItem(ssid, bssid, frequency, band, bandwidth, channelNum, apInstanceIdentifier);
            if(isRemoved) {
                mSoftApInfoList.remove(apItem);
                mLocalApStatustics.removeAp(apItem);
            } else {
                mSoftApInfoList.add(apItem);
                mLocalApStatustics.addAp(apItem);
            }
        }
    }
}