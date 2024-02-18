package com.android.networkmanager.trafficStatistics;

import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.content.Intent;
import android.net.TrafficStats;
import android.util.Log;

import com.android.networkmanager.Item.ApItem;

import java.util.HashMap;
import java.util.Map;

public class LocalApStatustics {

    private static String TAG = "NetworkManager::LocalApStatustics";
    private final Context mContext;
    private final NetworkStatsManager mNetworkStatsManager;
    private Map<String, ApItem> mLocalApMap = new HashMap<>();
    private Map<String, getTrafficDateThread> mGetTrafficDateThreadMap = new HashMap<>();
    private Thread mainThread;

    private class getTrafficDateThread extends Thread {
        private String ifname;
        public getTrafficDateThread(String ifname) {

            this.ifname = ifname;
        }
        @Override
        public void run() {
            while( !Thread.interrupted() ) {
                long rxBytes = TrafficStats.getRxBytes(ifname);
                long rxPackets = TrafficStats.getRxPackets(ifname);
                long txBytes = TrafficStats.getTxBytes(ifname);
                long txPackets = TrafficStats.getTxPackets(ifname);
                Log.i(TAG, "rxBytes="+rxBytes+",rxPackets="+rxPackets+",txBytes="+txBytes+",txPackets="+txPackets);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }
        }
    }

    public LocalApStatustics(Context context) {
        mContext = context;
        mNetworkStatsManager = mContext.getSystemService(NetworkStatsManager.class);
    }

    public void addAp(ApItem apItem) {
        mLocalApMap.put(apItem.mApInstanceIdentifier, apItem);
        getTrafficDateThread getTrafficDateThread = new getTrafficDateThread(apItem.mApInstanceIdentifier);
        mGetTrafficDateThreadMap.put(apItem.mApInstanceIdentifier, getTrafficDateThread);
        getTrafficDateThread.start();

    }

    public void removeAp(ApItem apItem) {
        mLocalApMap.remove(apItem.mApInstanceIdentifier);
        mGetTrafficDateThreadMap.get(apItem.mApInstanceIdentifier).interrupt();
        mGetTrafficDateThreadMap.remove(apItem.mApInstanceIdentifier);

    }

    public void clearAp() {
        mLocalApMap.clear();
        for(Thread thread : mGetTrafficDateThreadMap.values()) {
            thread.interrupt();
        }
        mGetTrafficDateThreadMap.clear();
    }

    public void start() {
        mainThread = new Thread() {
          @Override
          public void run() {
              while(!Thread.interrupted()) {
                  if(false) {
                      String bssidText = "";
                      String interfaceName = "";
                      String bssidCandidate = "";
                      String hostapdCmd = "BSS_TM_REQ " + bssidText + " disassoc_imminent=1 pref=1 valid_int=10 neighbor=" + bssidCandidate + ",0x000003FF,0,0,0";
                      Log.i(TAG, "roaming " + bssidText +  " to " + bssidCandidate);
                      Intent intent = new Intent("com.example.applicantion.Roaming");
                      intent.putExtra("cmd", hostapdCmd);
                      intent.putExtra("ifname", interfaceName);
                      mContext.sendBroadcast(intent);
                  } else if(false) {
                      Log.i(TAG, "no implement");
                  } else {
                      Log.i(TAG, "no implement");
                  }

              }
          }
        };
    }

    public void stop() {
        mainThread.interrupt();
    }
}
