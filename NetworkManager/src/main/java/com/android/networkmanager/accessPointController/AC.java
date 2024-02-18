package com.android.networkmanager.accessPointController;

import com.android.networkmanager.accessPointController.utils.AcConnectListener;
import com.android.networkmanager.accessPointController.utils.AcDiscoverListener;

public class AC {

    public static String TAG = "NetworkManager::ACManager";
    public AcDiscoverListener discoverListener;
    private AcConnectListener connectListener;

    public AC(){
        discoverListener = new AcDiscoverListener("111.111.111.111", "NT3-AC");
        connectListener = new AcConnectListener("111.111.111.111", "NT3-AC");

    }

    public void start(){
        discoverListener.start();
        connectListener.start();
    }

    public void stop(){
        discoverListener.stop();
        connectListener.stop();
    }

}
