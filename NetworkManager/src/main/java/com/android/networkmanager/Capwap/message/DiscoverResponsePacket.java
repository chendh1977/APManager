package com.android.networkmanager.Capwap.message;

import static com.android.networkmanager.Capwap.message.utils.Constant.DISCOVER_RESPONSE_MESSAGE;

import com.android.networkmanager.Capwap.message.basemessage.BaseControlPacket;
import com.android.networkmanager.Capwap.message.basemessage.ControlHeader;
import com.android.networkmanager.Capwap.message.basemessage.Header;
import com.android.networkmanager.Capwap.message.basemessage.MessageElement;
import com.android.networkmanager.Capwap.message.msgelem.ACName;
import com.android.networkmanager.Capwap.message.msgelem.ACIpAddress;
import com.android.networkmanager.Capwap.message.utils.Util;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class DiscoverResponsePacket extends BaseControlPacket {

    public static DiscoverResponsePacket buildDisvcoverResponsePacket(short num, byte[] acname, byte[] acaddress) {
        DiscoverResponsePacket discoverResponsePacket = new DiscoverResponsePacket();
        discoverResponsePacket.mHeader  = Header.builder((byte)0, (byte)1, (byte)0,(byte)0,false,false,false);

        byte hlen = 4;

        discoverResponsePacket.mControlMessage = ControlHeader.builder(DISCOVER_RESPONSE_MESSAGE, num);
        hlen += 4;

        discoverResponsePacket.mHeader.setHlen(hlen);

        discoverResponsePacket.mMsgElems = new ArrayList<>();
        byte messelemlen = 0;
        ACName acName = new ACName();
        acName.setName(acname);
        discoverResponsePacket.mMsgElems.add(acName);
        messelemlen += (acName.getLength()+4);

        ACIpAddress acIpAddress = new ACIpAddress();
        acIpAddress.setAddress(acaddress);
        discoverResponsePacket.mMsgElems.add(acIpAddress);
        messelemlen += (acIpAddress.getLength()+4);

        discoverResponsePacket.mControlMessage.setMsgElemLength(messelemlen);
        return discoverResponsePacket;
    }

    public static DiscoverResponsePacket decode(ByteBuffer input) {
        input.position(Util.tag.getBytes().length);
        Header header = Header.decodeHeader(input);
        ControlHeader controlHeader = ControlHeader.decodeControlHeader(input);
        List<MessageElement> messageElements = Util.buildMessageElement(input);
        DiscoverResponsePacket discoverResponsePacket = new DiscoverResponsePacket();
        discoverResponsePacket.setmHeader(header);
        discoverResponsePacket.setmControlMessage(controlHeader);
        discoverResponsePacket.setmMsgElems(messageElements);

        return discoverResponsePacket;
    }
}
