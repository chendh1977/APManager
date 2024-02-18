package com.android.networkmanager.accessPointController.utils;

import androidx.annotation.Nullable;

import com.android.networkmanager.Capwap.message.msgelem.WTPIpAddress;

public class WTPInfo {
    private String mIpAddress;
    private String mName;
    private String mMacAddress;

    public WTPInfo(String mIpAddress, String mName, String mMacAddress) {
        this.mIpAddress = mIpAddress;
        this.mName = mName;
        this.mMacAddress = mMacAddress;
    }

    public String getIpAddress() {
        return mIpAddress;
    }

    public String getMacAddress() {
        return mMacAddress;
    }

    public String getName() {
        return mName;
    }

    public void setIpAddress(String mIpAddress) {
        this.mIpAddress = mIpAddress;
    }

    public void setMacAddress(String mMacAddress) {
        this.mMacAddress = mMacAddress;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (o == this)
            return true;
        if (!(o instanceof WTPInfo))
            return false;

        return ((WTPInfo) o).getIpAddress().equals(mIpAddress) &&
                ((WTPInfo) o).getMacAddress().equals(mMacAddress) &&
                ((WTPInfo) o).getName().equals(mName);
    }
}
