package com.android.networkmanager.Item;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.android.networkmanager.R;

import java.util.ArrayList;
import java.util.List;

public class ClientItemAdapter extends BaseAdapter {

    private static String TAG = "ClientItemAdapter";
    private List<ClientItem> mDatas;
    private Context mContext;
    private String interfaceName;
    private String bssid;
    private ArrayList<String> bssidList;

    public ClientItemAdapter(List<ClientItem> mDatas, Context mContext, String interfaceName, String bssid, ArrayList<String> bssidList) {
        this.mDatas = mDatas;
        this.mContext = mContext;
        this.interfaceName = interfaceName;
        this.bssid = bssid;
        this.bssidList = bssidList;
    }
    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ClientItemAdapter.ViewHolder vh;
        if (convertView == null){
            vh = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.client_item, null);
            vh.mac_address = convertView.findViewById(R.id.mac_address);
            vh.ap_instance_identifier = convertView.findViewById(R.id.ap_instance_identifier);
            vh.roaming = convertView.findViewById(R.id.roaming);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder)convertView.getTag();
        }
        String bssidText = mDatas.get(position).mMacAddress;
        String apInstanceIdentifierText = mDatas.get(position).mApInstanceIdentifier;
        vh.mac_address.setText(bssidText);
        vh.ap_instance_identifier.setText(apInstanceIdentifierText);
        vh.roaming.setTag(position);
        vh.roaming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(String bssidCandidate : bssidList) {
                    if(!bssidCandidate.equals(bssid)) {
                        String hostapdCmd = "BSS_TM_REQ " + bssidText + " disassoc_imminent=1 pref=1 valid_int=10 neighbor=" + bssidCandidate + ",0x000003FF,0,0,0";
                        Log.i(TAG, "roaming " + bssidText +  " to " + bssidCandidate);
                        Intent intent = new Intent("com.example.applicantion.Roaming");
                        intent.putExtra("cmd", hostapdCmd);
                        intent.putExtra("ifname", interfaceName);
                        mContext.sendBroadcast(intent);
                    }
                }
            }
        });
        return convertView;
    }

    public static final class ViewHolder
    {

        public TextView mac_address;

        public TextView ap_instance_identifier;

        public Button roaming;
    }
}
