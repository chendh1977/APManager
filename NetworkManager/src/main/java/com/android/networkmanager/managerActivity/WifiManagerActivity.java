package com.android.networkmanager.managerActivity;

import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.android.networkmanager.R;
import com.android.networkmanager.Utils.ToastUtils;
import com.android.networkmanager.Utils.Utils;
import com.android.networkmanager.Item.WifiItem;
import com.android.networkmanager.Item.WifiItemAdapter;

import java.util.ArrayList;
import java.util.List;

public class WifiManagerActivity extends Activity implements View.OnClickListener {

    private static String TAG = "NetworkManager::WifiManagerActivity";
    private WifiManager mWifiManager;
    private ListView mWifiList;
    private ImageButton mBackButton;
    private ImageButton mFlushButton;
    private BroadcastReceiver mBroadcastReceiver;
    private Thread mScanThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_manager);

        initViewItem();
        initBroadcast();

        mWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        if(!mWifiManager.isWifiEnabled()){
            mWifiManager.setWifiEnabled(true);
        }

        mScanThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!Thread.interrupted()) {
                    mWifiManager.startScan();
                    try {
                        Thread.sleep(5000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        mScanThread.start();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.back) {ToastUtils.show(getApplicationContext(), "退出");
            finish();
        } else if(v.getId() == R.id.flush) {
            ToastUtils.show(getApplicationContext(), "刷新");
            mWifiManager.startScan();
        }
    }

    @Override
    protected void onDestroy() {
        if (mScanThread != null) {
            mScanThread.interrupt(); // 中断线程的执行
        }
        unregisterReceiver(mBroadcastReceiver);
        super.onDestroy();
    }

    private void initViewItem() {
        mWifiList = (ListView)findViewById(R.id.wifi_list);
        mBackButton = (ImageButton) findViewById(R.id.back);
        mFlushButton = (ImageButton) findViewById(R.id.flush);

        mBackButton.setOnClickListener(this);
        mFlushButton.setOnClickListener(this);
        mWifiList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ToastUtils.show(getApplicationContext(), "点击第"+(position+1)+"个item");
                Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                startActivity(intent);
            }
        });
    }

    private void initBroadcast() {
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context c, Intent intent) {
                boolean success = intent.getBooleanExtra(
                        WifiManager.EXTRA_RESULTS_UPDATED, false);
                Log.i(TAG, "scan result onReceive");
                if (success) {
                    Log.i(TAG, "wifi scan success");
                    List<WifiItem> mDatas = getListViewItem();
                    WifiItemAdapter wifiItemAdapter = new WifiItemAdapter(mDatas, WifiManagerActivity.this);
                    mWifiList.setAdapter(wifiItemAdapter);
                } else {
                    Log.i(TAG, "wifi scan faild");
                }
            }
        };
        registerReceiver(mBroadcastReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
    }

    private List<WifiItem> getListViewItem(){
        List<WifiItem> mDatas = new ArrayList<WifiItem>();
        if (ActivityCompat.checkSelfPermission(WifiManagerActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return mDatas;
        }
        List<ScanResult> scanResults = mWifiManager.getScanResults();
        Log.i(TAG, "scanResults size : " + scanResults.size());
        for (ScanResult scanresult: scanResults) {
            String ssid = scanresult.SSID;
            String bssid = scanresult.BSSID;
            int frequency = scanresult.frequency;
            int band = Utils.getBand(frequency);
            int channelWidth = scanresult.channelWidth;
            int channelNum = Utils.convertFrequencyMhzToChannelIfSupported(frequency);
            int rssiValue = scanresult.level;
            mDatas.add(new WifiItem(ssid, bssid, frequency, band, channelWidth, channelNum, rssiValue));
        }
        return mDatas;
    }
}