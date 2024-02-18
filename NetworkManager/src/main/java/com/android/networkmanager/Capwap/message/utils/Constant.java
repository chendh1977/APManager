package com.android.networkmanager.Capwap.message.utils;

public class Constant {
    public static final int DiscoverPort = 1203;
    public static final int DataPort = 1204;
    /* Capwap Message type definition - Start */
    public static final short KEEPALIVE_MESSAGE = 1;
    public static final short DATA_MESSAGE = 2;
    /**
     * Discover
     */
    public static final byte DISCOVER_REQUEST_MESSAGE = 3;
    public static final byte DISCOVER_RESPONSE_MESSAGE = 4;
    public static final byte CONNECT_REQUEST_MESSAGE = 5;
    public static final byte CONNECT_ALLOW_MESSAGE = 5;

    /**
     * Disconnect
     */
    public static final byte DISCONNECT_REPORT_MESSAGE = 6;
    public static final byte DISCONNECT_RESPONSE_MESSAGE = 7;

    /* Capwap Message type definition - End */

    /* Capwap Message Element Type Definition - Start */
    public static final short ELMT_TYPE_AC_NAME = 1;
    public static final short ELMT_TYPE_CONTROL_IPV4_ADDR = 2;
    public static final short ELMT_TYPE_WTP_NAME = 3;
    public static final short ELMT_TYPE_WTP_IP_ADDR = 4;
    public static final short ELMT_TYPE_WTP_MAC_ADDR = 5;
    /* Capwap Message Element Type Definition - End */
}