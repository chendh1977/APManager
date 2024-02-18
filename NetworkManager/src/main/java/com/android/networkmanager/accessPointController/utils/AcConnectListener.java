package com.android.networkmanager.accessPointController.utils;

import static com.android.networkmanager.Capwap.message.utils.Constant.DataPort;

import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class AcConnectListener {

    private static String TAG="AcConnectListener";

    private ServerSocket mSocket;
    private Thread listenThread;
    private final String acAddress;
    private final String acName;
    private Map<InetAddress, AcDataThread> threadMap = new HashMap<>();

    public AcConnectListener(String acAddress, String acName) {
        this.acAddress = acAddress;
        this.acName = acName;
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
            InetAddress inetAddress = InetAddress.getByName(acAddress);
            mSocket = new ServerSocket(DataPort, 100, inetAddress);
        } catch (IOException e) {
            logError("cant create a TCP socket : ", e);
        }
    }


    private void createAndRegisterSocket() {
        if (mSocket != null) {
            return;
        }

        createSocket();

        if (mSocket == null) return;
        listenThread = new Thread() {
            @Override
            public void run() {
                while (isRunning()) {
                    handleListen();
                }
            }
        };
        listenThread.start();
    }

    private void handleListen() {
        Socket socket = null;//接受连接
        try {
            assert mSocket != null;
            socket = mSocket.accept();
        } catch (IOException e) {
            logError("cant accept a link : ", e);
            return;
        }
        InetAddress inetAddress = socket.getInetAddress();
        //创建线程处理连接
        AcDataThread socketThread = new AcDataThread(socket);
        threadMap.put(inetAddress, socketThread);
        socketThread.start();
    }


    private void unregisterAndDestroySocket () {
        if (mSocket == null) {
            return;
        }

        for(AcDataThread dataThread : threadMap.values()) {
            dataThread.interrupt();
        }

        try {
            mSocket.close();
        } catch (IOException e) {
            logError("cant close listen socket : ", e);
        }
        mSocket = null;
    }

    private void logError(@NonNull String msg, @NonNull Exception e) {
        Log.i(TAG, msg+e.getMessage());
    }
}
