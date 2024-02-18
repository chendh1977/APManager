package com.android.networkmanager.Capwap.message.basemessage;

import com.android.networkmanager.Capwap.message.datapayload.Data;
import com.android.networkmanager.Capwap.message.utils.Util;

import java.nio.ByteBuffer;
import java.util.List;

public class BaseDataPacket extends BasePacket{
    protected DataPayload mData;

    @Override
    public ByteBuffer encode(){
        ByteBuffer buf = ByteBuffer.allocate(Util.tag.getBytes().length + mData.getLength() + mHeader.getHlen() + Util.end.getBytes().length);
        buf.put(Util.tag.getBytes());
        mHeader.encodeHeader(buf);
        mData.encode(buf);
        buf.put(Util.end.getBytes());
        return buf;
    }

    public DataPayload getmData() {
        return mData;
    }

    public void setmData(DataPayload mData) {
        this.mData = mData;
    }
}
