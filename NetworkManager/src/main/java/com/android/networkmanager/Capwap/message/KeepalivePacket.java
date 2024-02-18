package com.android.networkmanager.Capwap.message;

import com.android.networkmanager.Capwap.message.basemessage.BaseDataPacket;
import com.android.networkmanager.Capwap.message.basemessage.Header;
import com.android.networkmanager.Capwap.message.datapayload.Data;
import com.android.networkmanager.Capwap.message.datapayload.KeepAlive;
import com.android.networkmanager.Capwap.message.utils.Util;

import java.nio.ByteBuffer;

public class KeepalivePacket extends BaseDataPacket {
    public static KeepalivePacket buildKeepalivePacket() {
        KeepalivePacket keepalivePacket = new KeepalivePacket();
        keepalivePacket.mHeader = Header.builder((byte)0, (byte)0, (byte)0,(byte)0,false,false,false);
        int hlen = 4;
        keepalivePacket.mHeader.setHlen(((byte)hlen));
        KeepAlive mData = new KeepAlive();
        String data = "keep-alive";
        mData.setData(data.getBytes());
        keepalivePacket.mData = mData;
        return keepalivePacket;
    }

    public static KeepalivePacket decode(ByteBuffer input) {
        input.position(Util.tag.getBytes().length);
        Header header = Header.decodeHeader(input);
        KeepAlive keepAlive = KeepAlive.decode(input);
        KeepalivePacket keepalivePacket = new KeepalivePacket();
        keepalivePacket.setmData(keepAlive);
        keepalivePacket.setmHeader(header);

        return keepalivePacket;
    }
}
