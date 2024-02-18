package com.android.networkmanager.Capwap.message.msgelem;

import static com.android.networkmanager.Capwap.message.utils.Constant.ELMT_TYPE_CONTROL_IPV4_ADDR;

import com.android.networkmanager.Capwap.message.basemessage.MessageElement;

import java.nio.ByteBuffer;

public class ACIpAddress extends MessageElement {
    byte [] address = null;


    public ACIpAddress(){
        this.type = ELMT_TYPE_CONTROL_IPV4_ADDR;

    }

    public byte[] getAddress() {
        return address;
    }

    public void setAddress(byte[] address) {
        this.address = address;
        setLength((short)address.length);
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
        for(byte by: address) {
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

    public static ACIpAddress decode(ByteBuffer buf) {
        ACIpAddress ACIpAddress = new ACIpAddress();
        buf.getShort();
        short len = buf.getShort();
        byte[] b = new byte[len];
        buf.get(b);
        ACIpAddress.setAddress(b);

        return ACIpAddress;
    }

    @Override
    public boolean equals(Object o)
    {
        if (o == this)
            return true;
        if (!(o instanceof ACIpAddress))
            return false;
        if(type != ((ACIpAddress) o).getType())
        {
            return false;
        }
        if (getLength() != ((ACIpAddress)o).getLength())
        {
            return false;
        }

        byte[] tmp = ((ACIpAddress)o).getAddress();

        for (int i = 0; i < getLength(); i++)
        {

            if(address[i] != tmp[i])
            {
                return false;
            }
        }
        return true;
    }

}
