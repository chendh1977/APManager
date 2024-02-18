package com.android.networkmanager.Capwap.message.basemessage;

import java.nio.ByteBuffer;

public abstract class BasePacket {
    protected Header mHeader;

    public abstract ByteBuffer encode();

    public Header getmHeader() {
        return mHeader;
    }

    public void setmHeader(Header mHeader) {
        this.mHeader = mHeader;
    }
}
