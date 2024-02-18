package com.android.networkmanager.Capwap.message;

import com.android.networkmanager.Capwap.message.basemessage.BaseDataPacket;
import com.android.networkmanager.Capwap.message.basemessage.Header;
import com.android.networkmanager.Capwap.message.datapayload.Data;
import com.android.networkmanager.Capwap.message.utils.Util;

import java.nio.ByteBuffer;

public class DataPacket extends BaseDataPacket {
    public static DataPacket buildDataPacket(byte[] data) {
        DataPacket dataPacket = new DataPacket();
        dataPacket.mHeader = Header.builder((byte)0, (byte)0, (byte)0,(byte)0,false,false,false);
        int hlen = 4;
        dataPacket.mHeader.setHlen(((byte)hlen));
        Data mData = new Data();
        mData.setData(data);
        dataPacket.mData = mData;
        return dataPacket;
    }
    public static DataPacket decode(ByteBuffer input) {
        input.position(Util.tag.getBytes().length);
        Header header = Header.decodeHeader(input);
        Data data = Data.decode(input);
        DataPacket dataPacket = new DataPacket();
        dataPacket.setmHeader(header);
        dataPacket.setmData(data);
        return dataPacket;
    }
}
