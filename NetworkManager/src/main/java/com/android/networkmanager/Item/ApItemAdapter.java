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


public class ApItemAdapter extends BaseAdapter {

    private static String TAG = "ApItemAdapter";
    private List<ApItem> mDatas;
    private Context mContext;

    public ApItemAdapter(List<ApItem> mDatas, Context mContext) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.ap_item, null);
            vh.ssid = convertView.findViewById(R.id.ssid);
            vh.bssid = convertView.findViewById(R.id.bssid);
            vh.band = convertView.findViewById(R.id.band);
            vh.bandwidth = convertView.findViewById(R.id.band_width);
            vh.channelNum = convertView.findViewById(R.id.channel_num);
            vh.frequency = convertView.findViewById(R.id.frequency);
            vh.apInstanceIdentifie = convertView.findViewById(R.id.ap_instance_identifier);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder)convertView.getTag();
        }
        String ssidText = mDatas.get(position).mSsid.equals("") ? "HiddenSsid" : mDatas.get(position).mSsid;
        String bssidText = mDatas.get(position).mBssid;
        String bandText = mDatas.get(position).mBand == UNSPECIFIED ? "unknow" : Constant.Bands[mDatas.get(position).mBand];
        String bandWidthText = mDatas.get(position).mBandwidth == UNSPECIFIED ? "unknow" : Constant.ChannelWidths[mDatas.get(position).mBandwidth];
        String channelNumText = "CH." + mDatas.get(position).mChannelNum;
        String frequencyText = mDatas.get(position).mFrequency + "Mhz";
        String apInstanceIdentifieText = mDatas.get(position).mApInstanceIdentifier;
        vh.ssid.setText(ssidText);
        vh.bssid.setText(bssidText);
        vh.band.setText(bandText);
        vh.bandwidth.setText(bandWidthText);
        vh.channelNum.setText(channelNumText);
        vh.frequency.setText(frequencyText);
        vh.apInstanceIdentifie.setText(apInstanceIdentifieText);
        return convertView;
    }

    public static final class ViewHolder
    {

        public TextView ssid;

        public TextView bssid;

        public TextView band;


        public TextView bandwidth;

        public TextView channelNum;

        public TextView frequency;

        public TextView apInstanceIdentifie;
    }
}
