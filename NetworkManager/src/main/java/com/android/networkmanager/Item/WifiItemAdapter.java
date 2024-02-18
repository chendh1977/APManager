package com.android.networkmanager.Item;

import static com.android.networkmanager.Utils.Constant.UNSPECIFIED;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.networkmanager.R;
import com.android.networkmanager.Utils.Constant;

import java.util.List;


public class WifiItemAdapter extends BaseAdapter {

    private static String TAG = "WifiItemAdapter";
    private List<WifiItem> mDatas;
    private Context mContext;

    public WifiItemAdapter(List<WifiItem> mDatas, Context mContext) {
        this.mDatas = mDatas;
        this.mContext = mContext;
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
        ViewHolder vh;
        if (convertView == null){
            vh = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.wifi_item, null);
            vh.ssid = convertView.findViewById(R.id.ssid);
            vh.bssid = convertView.findViewById(R.id.bssid);
            vh.rssiValue = convertView.findViewById(R.id.rssi_value);
            vh.band = convertView.findViewById(R.id.band);
            vh.bandwidth = convertView.findViewById(R.id.band_width);
            vh.channelNum = convertView.findViewById(R.id.channel_num);
            vh.frequency = convertView.findViewById(R.id.frequency);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder)convertView.getTag();
        }
        String ssidText = mDatas.get(position).mSsid.equals("") ? "HiddenSsid" : mDatas.get(position).mSsid;
        String bssidText = mDatas.get(position).mBssid;
        String rssiValueText = mDatas.get(position).mRssiValue + "dBm";
        String bandText = mDatas.get(position).mBand == UNSPECIFIED ? "unknow" : Constant.Bands[mDatas.get(position).mBand];
        String bandWidthText = mDatas.get(position).mBandwidth == UNSPECIFIED ? "unknow" : Constant.ChannelWidths[mDatas.get(position).mBandwidth];
        String channelNumText = "CH." + mDatas.get(position).mChannelNum;
        String frequencyText = mDatas.get(position).mFrequency + "Mhz";
        vh.ssid.setText(ssidText);
        vh.bssid.setText(bssidText);
        vh.rssiValue.setText(rssiValueText);
        vh.band.setText(bandText);
        vh.bandwidth.setText(bandWidthText);
        vh.channelNum.setText(channelNumText);
        vh.frequency.setText(frequencyText);
        return convertView;
    }

    public static final class ViewHolder
    {

        public TextView ssid;

        public TextView bssid;

        public TextView rssiValue;

        public TextView band;

        public TextView bandwidth;

        public TextView channelNum;

        public TextView frequency;
    }
}
