package com.android.networkmanager.Capwap.message.basemessage;

import java.nio.ByteBuffer;

//                  0  1  2  3  4  5  6  7  0  1  2  3  4  5  6  7  0  1  2  3  4  5  6  7  0  1  2  3  4  5  6  7
//                 +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
//                 |Type                                           |length                                         |
//                 +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
//                 +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
//                 |value
//                 +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
public abstract class MessageElement {

    protected short type;
    protected short length;

    public abstract int encode(ByteBuffer buf);
    public short getType() {
        return type;
    }
    public short getLength() {
        return length;
    }
    public void setLength(short length) {
        this.length = length;
    }

}
