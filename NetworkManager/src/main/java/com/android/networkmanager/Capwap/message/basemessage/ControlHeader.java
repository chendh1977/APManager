package com.android.networkmanager.Capwap.message.basemessage;


import java.nio.ByteBuffer;

//                  0  1  2  3  4  5  6  7  0  1  2  3  4  5  6  7  0  1  2  3  4  5  6  7  0  1  2  3  4  5  6  7
//                 +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
//                 |Msg type               |ME Leng                 |Seq Sum                                       |
//                 +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
public class ControlHeader {
    private byte type =0;
    private byte msgElemLength = 0;
    private short seqSum=0;

    ControlHeader() {
    }

    byte[] createWord(){
        byte [] word = new byte[] {0,0,0,0};
        word[0] |= getType();
        word[1] |= getMsgElemLength();

        word[2] |= (byte) (getSeqSum() >>> 8);
        word[3] |= (byte) (getSeqSum() & 0x00FF);

        return word;
    }

    @Override
    public boolean equals(Object o)
    {
        if (o == this)
            return true;
        if(!(o instanceof ControlHeader))
            return false;
        if((type == ((ControlHeader) o).getType()) &&
                (seqSum ==  ((ControlHeader) o).getSeqSum()) &&
                (msgElemLength == ((ControlHeader) o).getMsgElemLength() ))
        {
            return true;
        }
        return false;

    }

    public void encodeControlHeader(ByteBuffer bbuf) {
        int start = bbuf.position();
        byte[] bytes = createWord();
        bbuf.put(bytes);
    }

    public static ControlHeader decodeControlHeader(ByteBuffer bbuf) {

        ControlHeader header = new ControlHeader();
        byte b = bbuf.get();
        header.setType(b);
        b = bbuf.get();
        header.setMsgElemLength(b);
        short sum = bbuf.getShort();
        header.setSeqSum(sum);

        return header;
    }


    public byte getType() {
        return type;
    }

    public ControlHeader setType(byte type) {
        this.type = type;
        return this;
    }

    public short getSeqSum() {
        return seqSum;
    }

    public ControlHeader setSeqSum(short seqSum) {
        this.seqSum = seqSum;
        return this;
    }

    public byte getMsgElemLength() {
        return msgElemLength;
    }

    public ControlHeader setMsgElemLength(byte msgElemLength) {
        this.msgElemLength = msgElemLength;
        return this;
    }


    public static ControlHeader builder(byte type, short seqSum) {
        ControlHeader controlHeader = new ControlHeader();
        controlHeader.setType(type)
                .setSeqSum(seqSum);
        return controlHeader;
    }

}
