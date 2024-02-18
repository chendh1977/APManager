package com.android.networkmanager.Item;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

public class ApItem {

    public String mSsid;
    public String mBssid;
    public int mFrequency;
    public int mBand;
    public int mBandwidth;
    public int mChannelNum;
    public String mApInstanceIdentifier;

    public ApItem(@NonNull String ssid, @NonNull String bssid, int frequency, int band, int bandwidth, int channelNum, @NonNull String apInstanceIdentifie) {
        this.mSsid = ssid;
        this.mBssid = bssid;
        this.mFrequency = frequency;
        this.mBand = band;
        this.mBandwidth = bandwidth;
        this.mChannelNum = channelNum;
        this.mApInstanceIdentifier = apInstanceIdentifie;
    }

    @Override
    public boolean equals(@Nullable Object otherObj) {
        if (this == otherObj) {
            return true;
        }
        if (!(otherObj instanceof ApItem)) {
            return false;
        }
        ApItem other = (ApItem) otherObj;
        return Objects.equals(mSsid, other.mSsid)
                && Objects.equals(mBssid, other.mBssid)
                && Objects.equals(mFrequency, other.mFrequency)
                && Objects.equals(mBand, other.mBand)
                && Objects.equals(mBandwidth, other.mBandwidth)
                && Objects.equals(mChannelNum, other.mChannelNum)
                && Objects.equals(mApInstanceIdentifier, other.mApInstanceIdentifier);
    }
}