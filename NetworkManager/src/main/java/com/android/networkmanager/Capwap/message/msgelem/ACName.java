package com.android.networkmanager.Capwap.message.msgelem;

import static com.android.networkmanager.Capwap.message.utils.Constant.ELMT_TYPE_AC_NAME;

import com.android.networkmanager.Capwap.message.basemessage.MessageElement;

import java.nio.ByteBuffer;

public class ACName extends MessageElement {


    byte [] name = null;


    public ACName(){
        this.type = ELMT_TYPE_AC_NAME;
    }

    public byte[] getName() {
        return name;
    }

    public void setName(byte[] name) {
        this.name = name;
        setLength((short) name.length);
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

    public static ACName decode(ByteBuffer buf) {
        ACName acName = new ACName();
        buf.getShort();
        short len = buf.getShort();
        byte[] b = new byte[len];
        buf.get(b);
        acName.setName(b);

        return acName;
    }

    @Override
    public boolean equals(Object o)
    {
        if (o == this)
            return true;
        if (!(o instanceof ACName))
            return false;
        if(type != ((ACName) o).getType())
        {
            return false;
        }
        if (name.length != ((ACName)o).getLength())
        {
            return false;
        }

        byte[] tmp = ((ACName)o).getName();

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
