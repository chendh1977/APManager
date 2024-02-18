package com.android.networkmanager.Item;


import androidx.annotation.NonNull;

public class WifiItem {

    public String mSsid;
    public String mBssid;
    public int mFrequency;
    public int mBand;
    public int mBandwidth;
    public int mChannelNum;
    public int mRssiValue;

    public WifiItem(@NonNull String ssid, String bssid, int frequency, int band, int bandWidth, int channelNum, int rssiValue) {
        this.mSsid = ssid;
        this.mBssid = bssid;
        this.mFrequency = frequency;
        this.mBand = band;
        this.mBandwidth = bandWidth;
        this.mChannelNum = channelNum;
        this.mRssiValue = rssiValue;
    }



}
