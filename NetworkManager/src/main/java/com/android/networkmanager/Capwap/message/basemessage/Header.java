package com.android.networkmanager.Capwap.message.basemessage;

import java.nio.ByteBuffer;


//                  0  1  2  3  4  5  6  7  0  1  2  3  4  5  6  7  0  1  2  3  4  5  6  7  0  1  2  3  4  5  6  7
//                 +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
//                 |Version    |Type       |HLEN       |F|L |K |Rsvd|Fragment ID            |Frag Offset            |
//                 +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+

public class Header {
    private byte type =0;
    private byte version = 0;
    private byte hlen=0;

    private byte fragId = 0;
    private byte fragOffset = 0;

    private boolean fFlag = false;
    private boolean lFlag = false;
    private boolean kFlag = false;


    private static byte fFlagMask = 0b00001000;
    private static byte lFlagMask = 0b00000100;
    private static byte kFlagMask = 0b00000010;


    Header() {
    }

    byte[] createWord(){
        byte [] word = new byte[] {0,0,0,0};
        word[0] |= (byte) (getType() &  0x0F);
        word[0] |= (byte) (getVersion() << 4);
        //set hlen
        word[1] |= (byte) (getHlen() << 4);

        if (isSetFbit()){
            word[1] |= fFlagMask;
        }
        if (isSetLbit()){
            word[1] |= lFlagMask;
        }
        if (isSetKbit()){
            word[1] |= kFlagMask;
        }

        word[2] |= (byte) getFragId();
        word[3] |= getFragOffset();

        return word;
    }

    @Override
    public boolean equals(Object o)
    {
        if (o == this)
            return true;
        if(!(o instanceof Header))
            return false;
        if((type == ((Header) o).getType()) &&
                (version ==  ((Header) o).getVersion()) &&
                (hlen == ((Header) o).getHlen() )&&
                (fragId == ((Header) o).getFragId())&&
                (fragOffset == ((Header) o).getFragOffset()))
        {
            return true;
        }
        return false;

    }

    public void encodeHeader(ByteBuffer bbuf) {
        int start = bbuf.position();
        byte[] bytes = createWord();
        bbuf.put(bytes);
    }

    public static Header decodeHeader(ByteBuffer bbuf) {

        Header header = new Header();
        byte b = bbuf.get();
        header.setVersion((byte) (b>>>4));
        header.setType((byte) (b&0x0F));
        b = bbuf.get();
        header.setHlen((byte) (b>>>4));
        header.setFbit((b&fFlagMask) != 0);
        header.setLbit((b&lFlagMask) != 0);
        header.setKbit((b&kFlagMask) != 0);
        b = bbuf.get();
        header.setFragId(b);
        b = bbuf.get();
        header.setFragOffset(b);

        return header;
    }

    public boolean isSetFbit() {
        return fFlag;
    }

    public Header setFbit(boolean fFlag)
    {
        this.fFlag = fFlag;
        return this;
    }

    public boolean isSetLbit() {
        return lFlag;
    }

    public Header setLbit(boolean lFlag) {
        this.lFlag = lFlag;
        return this;
    }

    public boolean isSetKbit() {
        return kFlag;
    }

    public Header setKbit(boolean kFlag) {
        this.kFlag = kFlag;
        return this;
    }

    public byte getType() {
        return type;
    }

    public Header setType(byte type) {
        this.type = type;
        return this;
    }

    public byte getVersion() {
        return version;
    }

    public Header setVersion(byte version) {
        this.version = version;
        return this;
    }

    public byte getHlen() {
        return hlen;
    }

    public Header setHlen(byte hlen) {
        this.hlen = hlen;
        return this;
    }

    public int getFragId() {
        return fragId;
    }

    public Header setFragId(byte fragId) {
        this.fragId = fragId;
        return this;
    }

    public byte getFragOffset() {
        return fragOffset;
    }

    public Header setFragOffset(byte fragOffset) {
        this.fragOffset = fragOffset;
        return this;
    }

    public static Header builder(byte version, byte type, byte fragId, byte fragOffset, boolean fFlag, boolean lFlag,boolean kFlag) {
        Header header = new Header();
        header.setType(type)
                .setVersion(version)
                .setFragId(fragId)
                .setFragOffset(fragOffset)
                .setFbit(fFlag)
                .setLbit(lFlag)
                .setKbit(kFlag);
        return header;
    }
}
