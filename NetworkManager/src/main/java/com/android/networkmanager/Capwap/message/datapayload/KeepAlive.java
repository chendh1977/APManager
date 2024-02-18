package com.android.networkmanager.Capwap.message.datapayload;

import static com.android.networkmanager.Capwap.message.utils.Constant.KEEPALIVE_MESSAGE;

import com.android.networkmanager.Capwap.message.basemessage.DataPayload;

import java.nio.ByteBuffer;

public class KeepAlive extends DataPayload {
    byte[] data=null;

    public KeepAlive() {
        type = KEEPALIVE_MESSAGE;
    }


    void createFirstWord(byte[] words) {
        words[0] |= (byte) (getType() >>> 8);
        words[1] |= (byte) (getType() & 0x000000FF);

        words[2] |= (byte) (getLength() >>> 8);
        words[3] |= (byte) (getLength() & 0x000000FF);
    }

    byte[] createWord(){
        byte [] words = new byte[length+4];
        createFirstWord(words);
        int i = 4;
        for(byte by: data) {
            words[i] |= by;
            i++;
        }
        return words;
    }

    @Override
    public int encode(ByteBuffer buf) {
        int start = buf.position();
        byte[] bytes = createWord();
        buf.put(bytes);
        return buf.position() - start;
    }

    public static KeepAlive decode(ByteBuffer buf) {
        KeepAlive data = new KeepAlive();
        buf.getShort();
        short len = buf.getShort();
        byte[] b = new byte[len];
        buf.get(b);
        data.setData(b);

        return data;
    }

    @Override
    public void setData(byte[] data) {
        this.data = data;
        this.setLength((short) data.length);
    }

    @Override
    public byte[] getData() {
        return data;
    }
}
