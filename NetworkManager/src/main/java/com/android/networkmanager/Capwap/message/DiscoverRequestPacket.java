package com.android.networkmanager.Capwap.message;

import static com.android.networkmanager.Capwap.message.utils.Constant.DISCOVER_REQUEST_MESSAGE;

import com.android.networkmanager.Capwap.message.basemessage.BaseControlPacket;
import com.android.networkmanager.Capwap.message.basemessage.ControlHeader;
import com.android.networkmanager.Capwap.message.basemessage.Header;
import com.android.networkmanager.Capwap.message.basemessage.MessageElement;
import com.android.networkmanager.Capwap.message.msgelem.WTPIpAddress;
import com.android.networkmanager.Capwap.message.msgelem.WTPName;
import com.android.networkmanager.Capwap.message.utils.Util;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class DiscoverRequestPacket extends BaseControlPacket {

    private DiscoverRequestPacket(){}
    public static DiscoverRequestPacket buildDisvcoverRequestPacket(byte[] wtpname, byte[] wtpadress) {
        DiscoverRequestPacket discoverRequestPacket = new DiscoverRequestPacket();
        discoverRequestPacket.mHeader  = Header.builder((byte)0, (byte)1, (byte)0,(byte)0,false,false,false);

        byte hlen = 4;

        short num = (short)(Math.random()*32767);
        while(num == 0) {
            num = (short)(Math.random()*32767);
        }
        discoverRequestPacket.mControlMessage = ControlHeader.builder(DISCOVER_REQUEST_MESSAGE, num);
        hlen += 4;

        discoverRequestPacket.mHeader.setHlen(hlen);

        discoverRequestPacket.mMsgElems = new ArrayList<>();
        byte messelemlen = 0;
        WTPName wtpName = new WTPName();
        wtpName.setName(wtpname);
        discoverRequestPacket.mMsgElems.add(wtpName);
        messelemlen += (wtpName.getLength()+4);

        WTPIpAddress wtpIpv4Addr = new WTPIpAddress();
        wtpIpv4Addr.setAddress(wtpadress);
        discoverRequestPacket.mMsgElems.add(wtpIpv4Addr);
        messelemlen += (wtpIpv4Addr.getLength()+4);

        discoverRequestPacket.mControlMessage.setMsgElemLength(messelemlen);
        return discoverRequestPacket;
    }

    public static DiscoverRequestPacket decode(ByteBuffer input) {
        input.position(Util.tag.getBytes().length);
        Header header = Header.decodeHeader(input);
        ControlHeader controlHeader = ControlHeader.decodeControlHeader(input);
        List<MessageElement> messageElements = Util.buildMessageElement(input);
        DiscoverRequestPacket discoverRequestPacket = new DiscoverRequestPacket();
        discoverRequestPacket.setmHeader(header);
        discoverRequestPacket.setmControlMessage(controlHeader);
        discoverRequestPacket.setmMsgElems(messageElements);

        return discoverRequestPacket;
    }

}
