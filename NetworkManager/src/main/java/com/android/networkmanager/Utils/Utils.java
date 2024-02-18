package com.android.networkmanager.Utils;


public class Utils {

    private static boolean is24GHz(int freqMhz) {
        return freqMhz >= Constant.BAND_24_GHZ_START_FREQ_MHZ && freqMhz <= Constant.BAND_24_GHZ_END_FREQ_MHZ;
    }

    private static boolean is5GHz(int freqMhz) {
        return freqMhz >=  Constant.BAND_5_GHZ_START_FREQ_MHZ && freqMhz <= Constant.BAND_5_GHZ_END_FREQ_MHZ;
    }

    private static boolean is6GHz(int freqMhz) {
        if (freqMhz == Constant.BAND_6_GHZ_OP_CLASS_136_CH_2_FREQ_MHZ) {
            return true;
        }
        return (freqMhz >= Constant.BAND_6_GHZ_START_FREQ_MHZ && freqMhz <= Constant.BAND_6_GHZ_END_FREQ_MHZ);
    }

    private static boolean is6GHzPsc(int freqMhz) {
        if (!is6GHz(freqMhz)) {
            return false;
        }
        return (freqMhz - Constant.BAND_6_GHZ_PSC_START_MHZ) % Constant.BAND_6_GHZ_PSC_STEP_SIZE_MHZ == 0;
    }

    private static boolean is60GHz(int freqMhz) {
        return freqMhz >= Constant.BAND_60_GHZ_START_FREQ_MHZ && freqMhz <= Constant.BAND_60_GHZ_END_FREQ_MHZ;
    }

    public static int getBand(int frequency) {
        if (is24GHz(frequency)) {
            return Constant.WIFI_BAND_24_GHZ;
        } else if (is5GHz(frequency)) {
            return Constant.WIFI_BAND_5_GHZ;
        } else if (is6GHz(frequency)) {
            return Constant.WIFI_BAND_6_GHZ;
        } else if (is60GHz(frequency)) {
            return Constant.WIFI_BAND_60_GHZ;
        }
        return Constant.UNSPECIFIED;
    }

    public static int convertFrequencyMhzToChannelIfSupported(int freqMhz) {
        if (freqMhz == 2484) {
            return 14;
        } else if (is24GHz(freqMhz)) {
            return (freqMhz - Constant.BAND_24_GHZ_START_FREQ_MHZ) / 5 + Constant.BAND_24_GHZ_FIRST_CH_NUM;
        } else if (is5GHz(freqMhz)) {
            return ((freqMhz - Constant.BAND_5_GHZ_START_FREQ_MHZ) / 5) + Constant.BAND_5_GHZ_FIRST_CH_NUM;
        } else if (is6GHz(freqMhz)) {
            if (freqMhz == Constant.BAND_6_GHZ_OP_CLASS_136_CH_2_FREQ_MHZ) {
                return 2;
            }
            return ((freqMhz - Constant.BAND_6_GHZ_START_FREQ_MHZ) / 5) + Constant.BAND_6_GHZ_FIRST_CH_NUM;
        } else if (is60GHz(freqMhz)) {
            return ((freqMhz - Constant.BAND_60_GHZ_START_FREQ_MHZ) / 2160) + Constant.BAND_60_GHZ_FIRST_CH_NUM;
        }

        return Constant.UNSPECIFIED;
    }

}
