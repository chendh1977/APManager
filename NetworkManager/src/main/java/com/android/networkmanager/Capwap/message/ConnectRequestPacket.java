package com.android.networkmanager.Capwap.message;

import static com.android.networkmanager.Capwap.message.utils.Constant.CONNECT_REQUEST_MESSAGE;

import com.android.networkmanager.Capwap.message.basemessage.BaseControlPacket;
import com.android.networkmanager.Capwap.message.basemessage.ControlHeader;
import com.android.networkmanager.Capwap.message.basemessage.Header;
import com.android.networkmanager.Capwap.message.basemessage.MessageElement;
import com.android.networkmanager.Capwap.message.msgelem.WTPIpAddress;
import com.android.networkmanager.Capwap.message.msgelem.WTPMacAddress;
import com.android.networkmanager.Capwap.message.msgelem.WTPName;
import com.android.networkmanager.Capwap.message.utils.Util;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class ConnectRequestPacket extends BaseControlPacket {
    private ConnectRequestPacket(){}
    public static ConnectRequestPacket buildConnectSuccessPacket(short seqNum, byte[] wtpname, byte[] wtpaddress, byte[] wtpmacaddress) {
        ConnectRequestPacket connectRequestPacket = new ConnectRequestPacket();
        connectRequestPacket.mHeader  = Header.builder((byte)0, (byte)1, (byte)0,(byte)0,false,false,false);

        byte hlen = 4;

        connectRequestPacket.mControlMessage = ControlHeader.builder(CONNECT_REQUEST_MESSAGE, seqNum);
        hlen += 4;

        connectRequestPacket.mHeader.setHlen(hlen);

        connectRequestPacket.mMsgElems = new ArrayList<>();
        byte messelemlen = 0;
        WTPName wtpName = new WTPName();
        wtpName.setName(wtpname);
        connectRequestPacket.mMsgElems.add(wtpName);
        messelemlen += (wtpName.getLength()+4);

        WTPIpAddress wtpIpv4Addr = new WTPIpAddress();
        wtpIpv4Addr.setAddress(wtpaddress);
        connectRequestPacket.mMsgElems.add(wtpIpv4Addr);
        messelemlen += (wtpIpv4Addr.getLength()+4);

        WTPMacAddress wtpMacAddress = new WTPMacAddress();
        wtpMacAddress.setAddress(wtpmacaddress);
        connectRequestPacket.mMsgElems.add(wtpMacAddress);
        messelemlen += (wtpMacAddress.getLength()+4);

        connectRequestPacket.mControlMessage.setMsgElemLength(messelemlen);
        return connectRequestPacket;
    }

    public static ConnectRequestPacket decode(ByteBuffer input) {
        input.position(Util.tag.getBytes().length);
        Header header = Header.decodeHeader(input);
        ControlHeader controlHeader = ControlHeader.decodeControlHeader(input);
        List<MessageElement> messageElements = Util.buildMessageElement(input);
        ConnectRequestPacket connectRequestPacket = new ConnectRequestPacket();
        connectRequestPacket.setmHeader(header);
        connectRequestPacket.setmControlMessage(controlHeader);
        connectRequestPacket.setmMsgElems(messageElements);

        return connectRequestPacket;
    }
}
