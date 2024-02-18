package com.android.networkmanager.accessPointController.utils;

import static com.android.networkmanager.Capwap.message.utils.Constant.DATA_MESSAGE;
import static com.android.networkmanager.Capwap.message.utils.Constant.DISCONNECT_REPORT_MESSAGE;
import static com.android.networkmanager.Capwap.message.utils.Constant.ELMT_TYPE_WTP_IP_ADDR;
import static com.android.networkmanager.Capwap.message.utils.Constant.ELMT_TYPE_WTP_NAME;
import static com.android.networkmanager.Capwap.message.utils.Constant.KEEPALIVE_MESSAGE;

import android.util.Log;

import androidx.annotation.NonNull;

import com.android.networkmanager.Capwap.message.DataPacket;
import com.android.networkmanager.Capwap.message.DisconnectReportPacket;
import com.android.networkmanager.Capwap.message.DisconnectResponsePacket;
import com.android.networkmanager.Capwap.message.KeepalivePacket;
import com.android.networkmanager.Capwap.message.basemessage.BasePacket;
import com.android.networkmanager.Capwap.message.basemessage.MessageElement;
import com.android.networkmanager.Capwap.message.msgelem.WTPIpAddress;
import com.android.networkmanager.Capwap.message.msgelem.WTPName;
import com.android.networkmanager.Capwap.message.utils.Util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.List;

public class AcDataThread extends Thread{

    private static String TAG="AcDataThread";

    private ByteBuffer rxBuf;
    private ByteBuffer txBuf;
    private Socket mSocket;
    private InputStream in;
    private OutputStream out;
    AcDataThread(Socket socket) {
        mSocket = socket;
        rxBuf = ByteBuffer.allocate(1024);
        try {
            in = mSocket.getInputStream();
            out = mSocket.getOutputStream();
        } catch (IOException e) {
            logError("cant get socket in and out", e);
        }
    }

    String getTAG() {
        return TAG+"("+mSocket.getInetAddress().toString()+")";
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            handleInput();
        }
    }

    private void onReceive(int type, BasePacket packet) {
        switch (type) {
            case DISCONNECT_REPORT_MESSAGE :
                DisconnectReportPacket disconnectReportPacket = (DisconnectReportPacket)packet;
                short seqSum = disconnectReportPacket.getmControlMessage().getSeqSum();
                List<MessageElement> messageElements = disconnectReportPacket.getmMsgElems();
                byte[] name = new byte[0];
                byte[] address = new byte[0];
                for (MessageElement me : messageElements) {
                    switch (me.getType()) {
                        case ELMT_TYPE_WTP_NAME :
                            WTPName wtpName = (WTPName) me;
                            name = wtpName.getName();
                            break;
                        case ELMT_TYPE_WTP_IP_ADDR :
                            WTPIpAddress wtpIpAddress = (WTPIpAddress) me;
                            address = wtpIpAddress.getAddress();
                            break;
                    }
                }
                transmitDisconnectResponsePacket(seqSum, address);
        }
    }

    private void transmitDisconnectResponsePacket(short seqSum, byte[] address) {
        DisconnectResponsePacket disvcoverResponsePacket = DisconnectResponsePacket.buildDisconnectResponsePacket(seqSum);
        txBuf = disvcoverResponsePacket.encode();
        try {
            out.write(txBuf.array());
        } catch (IOException e) {
            logError("Failed to send message: ", e);
        }
        interrupt();
    }


    private int readPacket() {
        if (in == null) {
            return 0;
        }
        rxBuf.clear();
        try {
            int available = in.available();
            if (available > 0) {
                byte[] buffer = new byte[available];
                int size = in.read(buffer);
                if (size > 0) {
                    byte[] bytes = Util.tag.getBytes();
                    if(size <= bytes.length) {
                        return 0;
                    }
                    for(int i = 0; i<bytes.length; i++) {
                        if(bytes[i] != buffer[i]) {
                            return 0;
                        }
                    }
                    rxBuf.put(buffer);
                } else {
                    return 0;
                }
            }
        } catch (IOException e) {
            logError("Failed to receive message: ", e);
            return 0;
        }
        return rxBuf.position();
    }
    private void handlePacket() {

        switch (Util.PacketType(rxBuf)) {
            case 0 :
                switch (Util.DateType(rxBuf)) {
                    case DATA_MESSAGE :
                        DataPacket dataPacket = DataPacket.decode(rxBuf);
                        onReceive(DATA_MESSAGE, dataPacket);
                        break;
                    case KEEPALIVE_MESSAGE :
                        KeepalivePacket keepalivePacket = KeepalivePacket.decode(rxBuf);
                        onReceive(KEEPALIVE_MESSAGE, keepalivePacket);
                        break;
                    default:
                        log("Unknow DATA packet!");
                }
            case 1 :
                if (Util.ControlMessageType(rxBuf) == DISCONNECT_REPORT_MESSAGE) {
                    DisconnectReportPacket disconnectReportPacket = DisconnectReportPacket.decode(rxBuf);
                    onReceive(DISCONNECT_REPORT_MESSAGE, disconnectReportPacket);
                } else {
                    log("Unknow Control packet!");
                }
            default:
                log("Unknow packet!");
        }
    }

    private void handleInput() {
        final int bytesRead;

        bytesRead = readPacket();
        if(bytesRead == 0) {
            return;
        }

        handlePacket();
    }

    private void logError(@NonNull String msg, @NonNull Exception e) {
        Log.i(getTAG(), msg+e.getMessage());
    }

    private void log(@NonNull String msg) {
        Log.i(getTAG(), msg);
    }


}
