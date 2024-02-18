package com.android.networkmanager.Capwap.message;

import static com.android.networkmanager.Capwap.message.utils.Constant.CONNECT_ALLOW_MESSAGE;

import com.android.networkmanager.Capwap.message.basemessage.BaseControlPacket;
import com.android.networkmanager.Capwap.message.basemessage.ControlHeader;
import com.android.networkmanager.Capwap.message.basemessage.Header;
import com.android.networkmanager.Capwap.message.basemessage.MessageElement;
import com.android.networkmanager.Capwap.message.msgelem.ACIpAddress;
import com.android.networkmanager.Capwap.message.msgelem.ACName;
import com.android.networkmanager.Capwap.message.utils.Util;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class ConnectAllowPacket extends BaseControlPacket {
    public static ConnectAllowPacket buildDisvcoverResponsePacket(short num, byte[] acname, byte[] acaddress) {
        ConnectAllowPacket connectAllowPacket = new ConnectAllowPacket();
        connectAllowPacket.mHeader  = Header.builder((byte)0, (byte)1, (byte)0,(byte)0,false,false,false);

        byte hlen = 4;

        connectAllowPacket.mControlMessage = ControlHeader.builder(CONNECT_ALLOW_MESSAGE, num);
        hlen += 4;

        connectAllowPacket.mHeader.setHlen(hlen);

        connectAllowPacket.mMsgElems = new ArrayList<>();
        byte messelemlen = 0;
        ACName acName = new ACName();
        acName.setName(acname);
        connectAllowPacket.mMsgElems.add(acName);
        messelemlen += (acName.getLength()+4);

        ACIpAddress acIpAddress = new ACIpAddress();
        acIpAddress.setAddress(acaddress);
        connectAllowPacket.mMsgElems.add(acIpAddress);
        messelemlen += (acIpAddress.getLength()+4);

        connectAllowPacket.mControlMessage.setMsgElemLength(messelemlen);
        return connectAllowPacket;
    }

    public static ConnectAllowPacket decode(ByteBuffer input) {
        input.position(Util.tag.getBytes().length);
        Header header = Header.decodeHeader(input);
        ControlHeader controlHeader = ControlHeader.decodeControlHeader(input);
        List<MessageElement> messageElements = Util.buildMessageElement(input);
        ConnectAllowPacket connectAllowPacket = new ConnectAllowPacket();
        connectAllowPacket.setmHeader(header);
        connectAllowPacket.setmControlMessage(controlHeader);
        connectAllowPacket.setmMsgElems(messageElements);

        return connectAllowPacket;
    }
}
