package com.android.networkmanager.Capwap.message;

import static com.android.networkmanager.Capwap.message.utils.Constant.DISCONNECT_RESPONSE_MESSAGE;

import com.android.networkmanager.Capwap.message.basemessage.BaseControlPacket;
import com.android.networkmanager.Capwap.message.basemessage.ControlHeader;
import com.android.networkmanager.Capwap.message.basemessage.Header;
import com.android.networkmanager.Capwap.message.basemessage.MessageElement;
import com.android.networkmanager.Capwap.message.utils.Util;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class DisconnectResponsePacket extends BaseControlPacket {
    public static DisconnectResponsePacket buildDisconnectResponsePacket(short num) {
        DisconnectResponsePacket disconnectResponsePacket = new DisconnectResponsePacket();
        disconnectResponsePacket.mHeader  = Header.builder((byte)0, (byte)1, (byte)0,(byte)0,false,false,false);

        byte hlen = 4;

        disconnectResponsePacket.mControlMessage = ControlHeader.builder(DISCONNECT_RESPONSE_MESSAGE, num);
        hlen += 4;

        disconnectResponsePacket.mHeader.setHlen(hlen);

        disconnectResponsePacket.mMsgElems = new ArrayList<>();
        byte messelemlen = 0;

        disconnectResponsePacket.mControlMessage.setMsgElemLength(messelemlen);
        return disconnectResponsePacket;
    }

    public static DisconnectResponsePacket decode(ByteBuffer input) {
        input.position(Util.tag.getBytes().length);
        Header header = Header.decodeHeader(input);
        ControlHeader controlHeader = ControlHeader.decodeControlHeader(input);
        List<MessageElement> messageElements = Util.buildMessageElement(input);
        DisconnectResponsePacket disconnectResponsePacket = new DisconnectResponsePacket();
        disconnectResponsePacket.setmHeader(header);
        disconnectResponsePacket.setmControlMessage(controlHeader);
        disconnectResponsePacket.setmMsgElems(messageElements);

        return disconnectResponsePacket;
    }

}
