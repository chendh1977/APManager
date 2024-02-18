package com.android.networkmanager.Utils;

public class Constant {

    /**
     * Wi-Fi 2.4 GHz band.
     */
    public static final int WIFI_BAND_24_GHZ = 0;

    /**
     * Wi-Fi 5 GHz band.
     */
    public static final int WIFI_BAND_5_GHZ = 1;

    /**
     * Wi-Fi 6 GHz band.
     */
    public static final int WIFI_BAND_6_GHZ = 3;

    /**
     * Wi-Fi 60 GHz band.
     */
    public static final int WIFI_BAND_60_GHZ = 4;

    public static String[] Bands = {"2.4G", "5G", "6G", "60G"};

    /**
     * The unspecified value.
     */
    public final static int UNSPECIFIED = -1;

    /**
     * 2.4 GHz band first channel number
     */
    public static final int BAND_24_GHZ_FIRST_CH_NUM = 1;

    /**
     * 2.4 GHz band last channel number
     */
    public static final int BAND_24_GHZ_LAST_CH_NUM = 14;

    /**
     * 2.4 GHz band frequency of first channel in MHz
     */
    public static final int BAND_24_GHZ_START_FREQ_MHZ = 2412;

    /**
     * 2.4 GHz band frequency of last channel in MHz
     */
    public static final int BAND_24_GHZ_END_FREQ_MHZ = 2484;

    /**
     * 5 GHz band first channel number
     */
    public static final int BAND_5_GHZ_FIRST_CH_NUM = 32;

    /**
     * 5 GHz band last channel number
     */
    public static final int BAND_5_GHZ_LAST_CH_NUM = 177;

    /**
     * 5 GHz band frequency of first channel in MHz
     */
    public static final int BAND_5_GHZ_START_FREQ_MHZ = 5160;

    /**
     * 5 GHz band frequency of last channel in MHz
     */
    public static final int BAND_5_GHZ_END_FREQ_MHZ = 5885;

    /**
     * 6 GHz band first channel number
     */
    public static final int BAND_6_GHZ_FIRST_CH_NUM = 1;

    /**
     * 6 GHz band last channel number
     */
    public static final int BAND_6_GHZ_LAST_CH_NUM = 233;

    /**
     * 6 GHz band frequency of first channel in MHz
     */
    public static final int BAND_6_GHZ_START_FREQ_MHZ = 5955;

    /**
     * 6 GHz band frequency of last channel in MHz
     */
    public static final int BAND_6_GHZ_END_FREQ_MHZ = 7115;

    /**
     * The center frequency of the first 6Ghz preferred scanning channel, as defined by
     * IEEE802.11ax draft 7.0 section 26.17.2.3.3.
     */
    public static final int BAND_6_GHZ_PSC_START_MHZ = 5975;

    /**
     * The number of MHz to increment in order to get the next 6Ghz preferred scanning channel
     * as defined by IEEE802.11ax draft 7.0 section 26.17.2.3.3.
     */
    public static final int BAND_6_GHZ_PSC_STEP_SIZE_MHZ = 80;

    /**
     * 6 GHz band operating class 136 channel 2 center frequency in MHz
     */
    public static final int BAND_6_GHZ_OP_CLASS_136_CH_2_FREQ_MHZ = 5935;

    /**
     * 60 GHz band first channel number
     */
    public static final int BAND_60_GHZ_FIRST_CH_NUM = 1;

    /**
     * 60 GHz band last channel number
     */
    public static final int BAND_60_GHZ_LAST_CH_NUM = 6;

    /**
     * 60 GHz band frequency of first channel in MHz
     */
    public static final int BAND_60_GHZ_START_FREQ_MHZ = 58320;

    /**
     * 60 GHz band frequency of last channel in MHz
     */
    public static final int BAND_60_GHZ_END_FREQ_MHZ = 70200;

    /**
     * AP Channel bandwidth is 20 MHZ
     */
    public static final int CHANNEL_WIDTH_20MHZ = 0;

    /**
     * AP Channel bandwidth is 40 MHZ
     */
    public static final int CHANNEL_WIDTH_40MHZ = 1;

    /**
     * AP Channel bandwidth is 80 MHZ
     */
    public static final int CHANNEL_WIDTH_80MHZ = 2;

    /**
     * AP Channel bandwidth is 160 MHZ
     */
    public static final int CHANNEL_WIDTH_160MHZ = 3;

    /**
     * AP Channel bandwidth is 160 MHZ, but 80MHZ + 80MHZ
     */
    public static final int CHANNEL_WIDTH_80MHZ_PLUS_MHZ = 4;

    /**
     * AP Channel bandwidth is 320 MHZ
     */
    public static final int CHANNEL_WIDTH_320MHZ = 5;

    public static String[] ChannelWidths = {"20Mhz", "40Mhz", "80Mhz", "160Mhz", "80(p)Mhz", "320Mhz"};
}
