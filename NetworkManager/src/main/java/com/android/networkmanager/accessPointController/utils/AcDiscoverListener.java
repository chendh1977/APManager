package com.android.networkmanager.accessPointController.utils;

import static com.android.networkmanager.Capwap.message.utils.Constant.CONNECT_REQUEST_MESSAGE;
import static com.android.networkmanager.Capwap.message.utils.Constant.DISCOVER_REQUEST_MESSAGE;
import static com.android.networkmanager.Capwap.message.utils.Constant.DiscoverPort;
import static com.android.networkmanager.Capwap.message.utils.Constant.ELMT_TYPE_WTP_IP_ADDR;
import static com.android.networkmanager.Capwap.message.utils.Constant.ELMT_TYPE_WTP_MAC_ADDR;
import static com.android.networkmanager.Capwap.message.utils.Constant.ELMT_TYPE_WTP_NAME;

import android.util.Log;

import androidx.annotation.NonNull;

import com.android.networkmanager.Capwap.message.ConnectAllowPacket;
import com.android.networkmanager.Capwap.message.ConnectRequestPacket;
import com.android.networkmanager.Capwap.message.DiscoverRequestPacket;
import com.android.networkmanager.Capwap.message.DiscoverResponsePacket;
import com.android.networkmanager.Capwap.message.basemessage.BasePacket;
import com.android.networkmanager.Capwap.message.basemessage.MessageElement;
import com.android.networkmanager.Capwap.message.msgelem.WTPIpAddress;
import com.android.networkmanager.Capwap.message.msgelem.WTPMacAddress;
import com.android.networkmanager.Capwap.message.msgelem.WTPName;
import com.android.networkmanager.Capwap.message.utils.Util;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.List;

public class AcDiscoverListener {

    private static String TAG="AcDiscoverListener";


    private ByteBuffer mRxBuf;

    private ByteBuffer mTxBuf;

    private DatagramSocket mSocket;
    private Thread mListenThread;
    private final String mAcAddress;
    private final String mAcName;

    public AcDiscoverListener(String mAcAddress, String mAcName) {
        this.mAcAddress = mAcAddress;
        this.mAcName = mAcName;
        mRxBuf = ByteBuffer.allocate(1024);
    }

    public void start() {
        createAndRegisterSocket();
    }

    public void stop() {
        unregisterAndDestroySocket();
    }

    public boolean isRunning(){
        return mSocket != null;
    }


    private void createSocket() {
        try {
            InetAddress inetAddress = InetAddress.getByName(mAcAddress);
            mSocket = new DatagramSocket(DiscoverPort, inetAddress);
        } catch (UnknownHostException e) {
            logError("cant find local host: ", e);
        } catch (SocketException e) {
            logError("cant create a UDP socket: ", e);
        }
    }

    private void createAndRegisterSocket() {
        if (mSocket != null) {
            return;
        }

        createSocket();

        if(mSocket == null) {
            return;
        }
        mListenThread = new Thread() {
            @Override
            public void run() {
                while (isRunning()) {
                    handleInput();
                }
            }
        };
        mListenThread.start();
    }

    private void unregisterAndDestroySocket () {
        if (mSocket == null) {
            return;
        }

        mSocket.close();
        mSocket = null;
    }

    private void processDiscoverRequestPacket(DiscoverRequestPacket discoverRequestPacket) {
        short seqSum = discoverRequestPacket.getmControlMessage().getSeqSum();
        List<MessageElement> messageElements = discoverRequestPacket.getmMsgElems();
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
        transmitDisvcoverResponsePacket(seqSum, address);
    }

    private void processConnectRequestPacket(ConnectRequestPacket connectRequestPacket) {
        short seqSum = connectRequestPacket.getmControlMessage().getSeqSum();
        List<MessageElement> messageElements = connectRequestPacket.getmMsgElems();
        byte[] name = new byte[0];
        byte[] address = new byte[0];
        byte[] mmAcAddress = new byte[0];
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
                case  ELMT_TYPE_WTP_MAC_ADDR:
                    WTPMacAddress wtpMmAcAddress = (WTPMacAddress) me;
                    mmAcAddress = wtpMmAcAddress.getAddress();
            }
        }
        transmitConnectAllowPacket(seqSum, address);
    }

    private void transmitDisvcoverResponsePacket(short seqSum, byte[] address) {
        DiscoverResponsePacket discoverResponsePacket = DiscoverResponsePacket.buildDisvcoverResponsePacket(seqSum, mAcName.getBytes(), mAcAddress.getBytes());
        mTxBuf = discoverResponsePacket.encode();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(new String(address), DiscoverPort);
        DatagramPacket datagramPacket = new DatagramPacket(mTxBuf.array(), 0, mTxBuf.position(), inetSocketAddress);
        try {
            assert mSocket != null;
            mSocket.send(datagramPacket);
        } catch (IOException e) {
            logError("Failed to send message: ", e);
            return;
        }
        log("send a DiscoverResponsePacket to " + new String(address));
    }

    private void transmitConnectAllowPacket(short seqSum, byte[] address) {
        ConnectAllowPacket connectAllowPacket = ConnectAllowPacket.buildDisvcoverResponsePacket(seqSum, mAcName.getBytes(), mAcAddress.getBytes());
        mTxBuf = connectAllowPacket.encode();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(new String(address), DiscoverPort);
        DatagramPacket datagramPacket = new DatagramPacket(mTxBuf.array(), 0, mTxBuf.position(), inetSocketAddress);
        try {
            assert mSocket != null;
            mSocket.send(datagramPacket);
        } catch (IOException e) {
            logError("Failed to send message: ", e);
            return;
        }
        log("send a ConnectAllowPacket to " + new String(address));
    }

    private int readPacket() {
        if(mSocket == null) {
            return 0;
        }
        mRxBuf.clear();
        byte[] buf = new byte[1024];  //定义byte数组  用来接收发送来的数据
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        try {
            mSocket.receive(packet);
        } catch (IOException e) {
            logError("Failed to receive message: ", e);
            return 0;
        }
        byte[] bytes = Util.tag.getBytes();
        if(packet.getLength() <= bytes.length) {
            return 0;
        }
        for(int i = 0; i<bytes.length; i++) {
            if(bytes[i] != buf[i]) {
                return 0;
            }
        }
        mRxBuf.put(buf);

        return mRxBuf.position();
    }

    private void handlePacket() {

        if (Util.PacketType(mRxBuf) == 1 && Util.ControlMessageType(mRxBuf) == DISCOVER_REQUEST_MESSAGE) {
            DiscoverRequestPacket discoverRequestPacket = DiscoverRequestPacket.decode(mRxBuf);
            log("receive a DiscoverRequestPacket");
            processDiscoverRequestPacket(discoverRequestPacket);
        } else if(Util.PacketType(mRxBuf) == 1 && Util.ControlMessageType(mRxBuf) == CONNECT_REQUEST_MESSAGE) {
            ConnectRequestPacket connectRequestPacket = ConnectRequestPacket.decode(mRxBuf);
            log("receive a ConnectRequestPacket");
            processConnectRequestPacket(connectRequestPacket);
        }else {
            log("Not a Discover Message!");
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
        Log.i(TAG, msg+e.getMessage());
    }

    private void log(@NonNull String msg) {
        Log.i(TAG, msg);
    }
}
