package com.android.networkmanager.Capwap.message.msgelem;

import static com.android.networkmanager.Capwap.message.utils.Constant.ELMT_TYPE_WTP_NAME;

import com.android.networkmanager.Capwap.message.basemessage.MessageElement;

import java.nio.ByteBuffer;

public class WTPName extends MessageElement {
    byte [] name = null;


    public WTPName(){
        this.type = ELMT_TYPE_WTP_NAME;

    }

    public byte[] getName() {
        return name;
    }

    public void setName(byte[] name) {
        this.name = name;
        this.setLength((short)name.length);
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
        for(byte by: name) {
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

    public static WTPName decode(ByteBuffer buf) {
        WTPName wtpName = new WTPName();
        buf.getShort();
        short len = buf.getShort();
        byte[] b = new byte[len];
        buf.get(b);
        wtpName.setName(b);

        return wtpName;
    }

    @Override
    public boolean equals(Object o)
    {
        if (o == this)
            return true;
        if (!(o instanceof WTPName))
            return false;
        if(type != ((WTPName) o).getType())
        {
            return false;
        }
        if (name.length != ((WTPName)o).getLength())
        {
            return false;
        }

        byte[] tmp = ((WTPName)o).getName();

        for (int i = 0; i < getLength(); i++)
        {

            if(name[i] != tmp[i])
            {
                return false;
            }
        }
        return true;
    }
}
