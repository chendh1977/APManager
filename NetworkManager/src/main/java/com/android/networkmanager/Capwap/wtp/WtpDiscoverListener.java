package com.android.networkmanager.Capwap.wtp;

import static com.android.networkmanager.Capwap.message.utils.Constant.CONNECT_ALLOW_MESSAGE;
import static com.android.networkmanager.Capwap.message.utils.Constant.DISCOVER_RESPONSE_MESSAGE;
import static com.android.networkmanager.Capwap.message.utils.Constant.DataPort;
import static com.android.networkmanager.Capwap.message.utils.Constant.DiscoverPort;
import static com.android.networkmanager.Capwap.message.utils.Constant.ELMT_TYPE_AC_NAME;
import static com.android.networkmanager.Capwap.message.utils.Constant.ELMT_TYPE_CONTROL_IPV4_ADDR;

import android.util.Log;

import androidx.annotation.NonNull;

import com.android.networkmanager.Capwap.message.ConnectAllowPacket;
import com.android.networkmanager.Capwap.message.ConnectRequestPacket;
import com.android.networkmanager.Capwap.message.DiscoverRequestPacket;
import com.android.networkmanager.Capwap.message.DiscoverResponsePacket;
import com.android.networkmanager.Capwap.message.basemessage.BasePacket;
import com.android.networkmanager.Capwap.message.basemessage.MessageElement;
import com.android.networkmanager.Capwap.message.msgelem.ACName;
import com.android.networkmanager.Capwap.message.msgelem.ACIpAddress;
import com.android.networkmanager.Capwap.message.utils.Util;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class WtpDiscoverListener {
    private static String TAG="WtpDiscoverListener";

    private WtpStateMachine mWtpStateMachine;
    private ByteBuffer mRxBuf;

    private ByteBuffer mTxBuf;

    private DatagramSocket mDiscoverSocket;

    private Socket mListenSocket;
    private Thread mListenThread;
    private final String mWtpAddress;
    private final String mWtpName;
    private final String mWtpMacAddress;
    private String mAcName = null;
    private String mAcAddress = null;
    private Timer mTimmer;
    private short mSeqSum = 0;

    public void start() {
        createAndRegisterSocket();
        startDiscover();
    }

    public void stop() {
        unregisterAndDestroySocket();
        stopDiscover();
    }

    public WtpDiscoverListener(String wtpAddress, String wtpName, String wtpMacAddress, WtpStateMachine stateMachine) {
        mWtpAddress = wtpAddress;
        mWtpName = wtpName;
        mWtpMacAddress = wtpMacAddress;
        mWtpStateMachine = stateMachine;
        mRxBuf = ByteBuffer.allocate(1024);
    }

    public void startDiscover() {
        mTimmer = new Timer();

        TimerTask tramRequ = new TimerTask() {
            @Override
            public void run() {
                transmitDisvcoverRequestPacket();
            }

        };

        mTimmer.schedule(tramRequ, 0, 5000);// 5秒一次
    }

    public void stopDiscover() {
        if(mSeqSum != 0) {
            mTimmer.cancel();
            mSeqSum = 0;
        }
    }

    private boolean isRunning(){
        return mDiscoverSocket != null;
    }


    private void createSocket() {
        try {
            InetAddress inetAddress = InetAddress.getByName(mWtpAddress);
            mDiscoverSocket = new DatagramSocket(DiscoverPort, inetAddress);
        } catch (UnknownHostException e) {
            logError("cant find local host: ", e);
        } catch (SocketException e) {
            logError("cant create a UDP socket: ", e);
        }
    }

    public boolean connect() {
        mListenSocket = new Socket();
        InetAddress inetAddress;
        try {
            inetAddress = InetAddress.getByName(mAcAddress);
            InetSocketAddress socketAddress = new InetSocketAddress(inetAddress, DataPort);
            log("try to connect to : ");
            log("name : " + mAcName + "  addres : " + mAcName);
            mListenSocket.connect(socketAddress);
        } catch (UnknownHostException e) {
            logError("cant resolv ac adress: ", e);
            return false;
        } catch (IOException e) {
            logError("cant connect to ac: ", e);
            return false;
        }
        return true;
    }


    private void onReceive(int type,  BasePacket packet) {
        if(type == DISCOVER_RESPONSE_MESSAGE) {
            DiscoverResponsePacket discoverResponsePacket = (DiscoverResponsePacket)packet;
            short seqSum = discoverResponsePacket.getmControlMessage().getSeqSum();
            if(seqSum != mSeqSum) {
                return;
            }
            List<MessageElement> messageElements = discoverResponsePacket.getmMsgElems();
            byte[] name = new byte[0];
            byte[] address = new byte[0];
            for (MessageElement me : messageElements) {
                switch (me.getType()) {
                    case ELMT_TYPE_AC_NAME :
                        ACName acName = (ACName) me;
                        name = acName.getName();
                        break;
                    case ELMT_TYPE_CONTROL_IPV4_ADDR:
                        ACIpAddress ACIpAddress = (ACIpAddress) me;
                        address = ACIpAddress.getAddress();
                        break;
                }
            }
            transmitConnectRequestPacket(seqSum, address);
        } else if(type == CONNECT_ALLOW_MESSAGE) {
            ConnectAllowPacket connectAllowPacket = (ConnectAllowPacket) packet;
            short seqSum = connectAllowPacket.getmControlMessage().getSeqSum();
            if(seqSum != mSeqSum) {
                return;
            }
            List<MessageElement> messageElements = connectAllowPacket.getmMsgElems();
            byte[] name = new byte[0];
            byte[] address = new byte[0];
            for (MessageElement me : messageElements) {
                switch (me.getType()) {
                    case ELMT_TYPE_AC_NAME :
                        ACName acName = (ACName) me;
                        name = acName.getName();
                        break;
                    case ELMT_TYPE_CONTROL_IPV4_ADDR:
                        ACIpAddress ACIpAddress = (ACIpAddress) me;
                        address = ACIpAddress.getAddress();
                        break;
                }
            }
            mAcName = new String(name);
            mAcAddress = new String(address);
            mWtpStateMachine.sendMessage(WtpStateMachine.CMD_CONNECT);
        }


    }

    private void transmitDisvcoverRequestPacket() {
        DiscoverRequestPacket discoverRequestPacket = DiscoverRequestPacket.buildDisvcoverRequestPacket(mWtpName.getBytes(), mWtpAddress.getBytes());
        mSeqSum = discoverRequestPacket.getmControlMessage().getSeqSum();
        mTxBuf = discoverRequestPacket.encode();
        try {
            mDiscoverSocket.setBroadcast(true);
            InetSocketAddress inetSocketAddress = new InetSocketAddress("255.255.255.255", DiscoverPort);
            DatagramPacket datagramPacket = new DatagramPacket(mTxBuf.array(), 0, mTxBuf.position(), inetSocketAddress);
            assert mDiscoverSocket != null;
            mDiscoverSocket.send(datagramPacket);
        } catch (SocketException e) {
            logError("cant send Broadcast : ", e);
            return;
        } catch (IOException e) {
            logError("Failed to send message: ", e);
            return;
        }
        log("send a DiscoverRequestPacket to 255.255.255.255");
    }

    private void transmitConnectRequestPacket(short seqSum, byte[] address) {
        ConnectRequestPacket connectRequestPacket = ConnectRequestPacket.buildConnectSuccessPacket(seqSum, mWtpName.getBytes(), mWtpAddress.getBytes(), mWtpMacAddress.getBytes());
        mTxBuf = connectRequestPacket.encode();
        try {
            mDiscoverSocket.setBroadcast(false);
            InetSocketAddress inetSocketAddress = new InetSocketAddress(new String(address), DiscoverPort);
            DatagramPacket datagramPacket = new DatagramPacket(mTxBuf.array(), 0, mTxBuf.position(), inetSocketAddress);
            assert mDiscoverSocket != null;
            mDiscoverSocket.send(datagramPacket);
        } catch (SocketException e) {
            logError("cant send unicast : ", e);
            return;
        } catch (IOException e) {
            logError("Failed to send message: ", e);
            return;
        }
        log("send a ConnectRequestPacket to " + new String(address));
    }

    private int readPacket() {
        if(mDiscoverSocket == null) {
            return 0;
        }
        mRxBuf.clear();
        byte[] buf = new byte[1024];  //定义byte数组  用来接收发送来的数据
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        try {
            mDiscoverSocket.receive(packet);
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

        if (Util.PacketType(mRxBuf) == 1 && Util.ControlMessageType(mRxBuf) == DISCOVER_RESPONSE_MESSAGE) {
            DiscoverResponsePacket discoverResponsePacket = DiscoverResponsePacket.decode(mRxBuf);
            log("receive a DiscoverResponsePacket");
            onReceive(DISCOVER_RESPONSE_MESSAGE, discoverResponsePacket);
        } else if (Util.PacketType(mRxBuf) == 1 && Util.ControlMessageType(mRxBuf) == CONNECT_ALLOW_MESSAGE) {
            ConnectAllowPacket connectAllowPacket = ConnectAllowPacket.decode(mRxBuf);
            log("receive a ConnectAllowPacket");
            onReceive(CONNECT_ALLOW_MESSAGE, connectAllowPacket);
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

    public void createAndRegisterSocket() {
        if (mDiscoverSocket != null) {
            mWtpStateMachine.sendMessage(WtpStateMachine.CMD_FAILED);
            return;
        }

        createSocket();

        if(mDiscoverSocket == null) {
            mWtpStateMachine.sendMessage(WtpStateMachine.CMD_FAILED);
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
        mWtpStateMachine.sendMessage(WtpStateMachine.CMD_DISCOVER);
    }

    public void unregisterAndDestroySocket () {
        if (mDiscoverSocket == null) {
            return;
        }
        if (mListenSocket != null) {
            try {
                mListenSocket.close();
            } catch (IOException e) {
                logError("cant close a tcp socket : ", e);
            }
        }
        mListenSocket = null;
        mDiscoverSocket.close();
        mDiscoverSocket = null;
    }

    private void logError(@NonNull String msg, @NonNull Exception e) {
        Log.i(TAG, msg+e.getMessage());
    }

    private void log(@NonNull String msg) {
        Log.i(TAG, msg);
    }
}
