package com.android.networkmanager.Capwap.message.basemessage;

import java.nio.ByteBuffer;

public abstract class DataPayload {

    protected short type;
    protected short length;

    public abstract int encode(ByteBuffer buf);
    public void setLength(short length) {
        this.length = length;
    }
    public abstract void setData(byte[] data);
    public short getType() {
        return type;
    }
    public short getLength() {
        return length;
    }
    public abstract byte[] getData();
}
