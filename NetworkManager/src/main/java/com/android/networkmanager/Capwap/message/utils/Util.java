package com.android.networkmanager.Capwap.message.utils;

import static com.android.networkmanager.Capwap.message.utils.Constant.ELMT_TYPE_AC_NAME;
import static com.android.networkmanager.Capwap.message.utils.Constant.ELMT_TYPE_CONTROL_IPV4_ADDR;
import static com.android.networkmanager.Capwap.message.utils.Constant.ELMT_TYPE_WTP_IP_ADDR;
import static com.android.networkmanager.Capwap.message.utils.Constant.ELMT_TYPE_WTP_MAC_ADDR;
import static com.android.networkmanager.Capwap.message.utils.Constant.ELMT_TYPE_WTP_NAME;

import com.android.networkmanager.Capwap.message.basemessage.MessageElement;
import com.android.networkmanager.Capwap.message.msgelem.ACName;
import com.android.networkmanager.Capwap.message.msgelem.ACIpAddress;
import com.android.networkmanager.Capwap.message.msgelem.WTPIpAddress;
import com.android.networkmanager.Capwap.message.msgelem.WTPMacAddress;
import com.android.networkmanager.Capwap.message.msgelem.WTPName;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class Util {

    public static String tag = "CSAPP tag";
    public static String end = "end";
    public static int PacketType(ByteBuffer buffer) {

        buffer.position(Util.tag.getBytes().length);
        byte firstByte = buffer.get();  // 读取第一个字节
        buffer.position(0);
        return firstByte & 0x0F;
    }

    public static int DateType(ByteBuffer buffer) {

        buffer.position(Util.tag.getBytes().length+4);
        short type = buffer.getShort();
        buffer.position(0);
        return type;
    }

    public static int ControlMessageType(ByteBuffer buffer) {

        buffer.position(Util.tag.getBytes().length+4);
        byte type = buffer.get();
        buffer.position(0);
        return type;
    }

    public static boolean isEnd(byte[] input) {
        byte[] end = Util.end.getBytes();
        for(int i = 0; i<end.length; i++) {
            if(end[i] != input[i]) {
                return false;
            }
        }
        return true;
    }

    public static List<MessageElement> buildMessageElement(ByteBuffer input) {
        List<MessageElement> messageElements = new ArrayList<>();
        int position;
        short type;
        short len;
        byte[] data;
        byte[] end = Util.end.getBytes();
        byte[] buf = new byte[end.length];
        position = input.position();
        input.get(buf);
        input.position(position);
        while (!isEnd(buf)) {
            position = input.position();
            type = input.getShort();
            input.position(position);
            switch (type) {
                case ELMT_TYPE_AC_NAME :
                    ACName acName = ACName.decode(input);
                    messageElements.add(acName);
                    break;
                case ELMT_TYPE_CONTROL_IPV4_ADDR :
                    ACIpAddress acIpAddress = ACIpAddress.decode(input);
                    messageElements.add(acIpAddress);
                    break;
                case ELMT_TYPE_WTP_NAME :
                    WTPName wtpName = WTPName.decode(input);
                    messageElements.add(wtpName);
                    break;
                case ELMT_TYPE_WTP_IP_ADDR :
                    WTPIpAddress wtpIpAddress = WTPIpAddress.decode(input);
                    messageElements.add(wtpIpAddress);
                    break;
                case ELMT_TYPE_WTP_MAC_ADDR:
                    WTPMacAddress wtpMacAddress = WTPMacAddress.decode(input);
                    messageElements.add(wtpMacAddress);
                    break;
                default:
                    return null;
            }
            position = input.position();
            input.get(buf);
            input.position(position);
        }

        return  messageElements;
    }
}
