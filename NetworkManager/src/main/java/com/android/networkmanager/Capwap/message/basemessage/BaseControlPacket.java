package com.android.networkmanager.Capwap.message.basemessage;

import com.android.networkmanager.Capwap.message.utils.Util;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class BaseControlPacket extends BasePacket{
    protected ControlHeader mControlMessage;
    protected List<MessageElement> mMsgElems;

    @Override
    public ByteBuffer encode(){
        ByteBuffer buf = ByteBuffer.allocate(Util.tag.getBytes().length + mHeader.getHlen()+ mControlMessage.getMsgElemLength() + Util.end.getBytes().length);
        buf.put(Util.tag.getBytes());
        mHeader.encodeHeader(buf);
        mControlMessage.encodeControlHeader(buf);
        for(MessageElement messelem : mMsgElems) {
            messelem.encode(buf);
        }
        buf.put(Util.end.getBytes());
        return buf;
    }

    public ControlHeader getmControlMessage() {
        return mControlMessage;
    }

    public List<MessageElement> getmMsgElems() {
        return mMsgElems;
    }

    public void setmControlMessage(ControlHeader mControlMessage) {
        this.mControlMessage = mControlMessage;
    }

    public void setmMsgElems(List<MessageElement> mMsgElems) {
        this.mMsgElems = mMsgElems;
    }
}
