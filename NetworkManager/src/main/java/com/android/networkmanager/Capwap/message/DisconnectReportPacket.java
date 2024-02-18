package com.android.networkmanager.Capwap.message;

import static com.android.networkmanager.Capwap.message.utils.Constant.DISCONNECT_REPORT_MESSAGE;

import com.android.networkmanager.Capwap.message.basemessage.BaseControlPacket;
import com.android.networkmanager.Capwap.message.basemessage.ControlHeader;
import com.android.networkmanager.Capwap.message.basemessage.Header;
import com.android.networkmanager.Capwap.message.basemessage.MessageElement;
import com.android.networkmanager.Capwap.message.msgelem.ACName;
import com.android.networkmanager.Capwap.message.msgelem.ACIpAddress;
import com.android.networkmanager.Capwap.message.msgelem.WTPIpAddress;
import com.android.networkmanager.Capwap.message.msgelem.WTPName;
import com.android.networkmanager.Capwap.message.utils.Util;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class DisconnectReportPacket extends BaseControlPacket {
    public static DisconnectReportPacket buildDisconnectReportPacket(byte[] acname, byte[] acadress,byte[] wtpname,byte[] wtpaddress) {
        DisconnectReportPacket disconnectReportPacket = new DisconnectReportPacket();
        disconnectReportPacket.mHeader  = Header.builder((byte)0, (byte)1, (byte)0,(byte)0,false,false,false);

        byte hlen = 4;

        short num = (short)(Math.random()*32767);
        while (num == 0) {
            num = (short)(Math.random()*32767);
        }
        disconnectReportPacket.mControlMessage = ControlHeader.builder(DISCONNECT_REPORT_MESSAGE, num);
        hlen += 4;

        disconnectReportPacket.mHeader.setHlen(hlen);

        disconnectReportPacket.mMsgElems = new ArrayList<>();
        byte messelemlen = 0;
        ACName acName = new ACName();
        acName.setName(acname);
        disconnectReportPacket.mMsgElems.add(acName);
        messelemlen += (acName.getLength()+4);

        ACIpAddress ACIpAddress = new ACIpAddress();
        ACIpAddress.setAddress(acadress);
        disconnectReportPacket.mMsgElems.add(ACIpAddress);
        messelemlen += (ACIpAddress.getLength()+4);

        WTPName wtpName = new WTPName();
        wtpName.setName(wtpname);
        disconnectReportPacket.mMsgElems.add(wtpName);
        messelemlen += (wtpName.getLength()+4);

        WTPIpAddress wtpIpAddress = new WTPIpAddress();
        wtpIpAddress.setAddress(wtpaddress);
        disconnectReportPacket.mMsgElems.add(wtpIpAddress);
        messelemlen += (wtpIpAddress.getLength()+4);

        disconnectReportPacket.mControlMessage.setMsgElemLength(messelemlen);
        return disconnectReportPacket;
    }

    public static DisconnectReportPacket decode(ByteBuffer input) {
        input.position(Util.tag.getBytes().length);
        Header header = Header.decodeHeader(input);
        ControlHeader controlHeader = ControlHeader.decodeControlHeader(input);
        List<MessageElement> messageElements = Util.buildMessageElement(input);
        DisconnectReportPacket disconnectReportPacket = new DisconnectReportPacket();
        disconnectReportPacket.setmHeader(header);
        disconnectReportPacket.setmControlMessage(controlHeader);
        disconnectReportPacket.setmMsgElems(messageElements);

        return disconnectReportPacket;
    }

}
