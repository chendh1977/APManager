package com.android.networkmanager.Capwap.wtp;

import android.os.Looper;
import android.os.Message;

import com.android.networkmanager.statemachine.State;
import com.android.networkmanager.statemachine.StateMachine;

public class WtpStateMachine extends StateMachine {

    private static String TAG = "WtpStateMachine";

    public static final int CMD_DISCOVER = 0;
    public static final int CMD_FAILED = 1;
    public static final int CMD_CONNECT = 2;
    public static final int CMD_RUNING = 2;
    private final IdleState mIdleState = new IdleState();
    private final DiscoverState mDiscoverState = new DiscoverState();
    private final RuningState mRuningState = new RuningState();
    private final WtpDiscoverListener mWtpDiscoverListener;

    public WtpStateMachine(Looper looper, String wtpAddress, String wtpName, String wtpMacAddress) {
        super(TAG, looper);

        mWtpDiscoverListener = new WtpDiscoverListener(wtpAddress, wtpName, wtpMacAddress, this);

        addState(mIdleState);
        addState(mDiscoverState);
        addState(mRuningState);

        setInitialState(mIdleState);
        start();
    }

    private class IdleState extends State {
        @Override
        public void enter() {
            mWtpDiscoverListener.createAndRegisterSocket();
        }

        @Override
        public void exit() {
            mWtpDiscoverListener.unregisterAndDestroySocket();
        }

        @Override
        public boolean processMessage(Message msg) {
            switch (msg.what) {
                case CMD_DISCOVER:
                    transitionTo(mDiscoverState);
                    break;
                case CMD_FAILED:
                    break;
                default:
                    break;
            }
            return HANDLED;
        }
    }

    private class DiscoverState extends State {
        @Override
        public void enter() {
            mWtpDiscoverListener.startDiscover();
        }

        @Override
        public void exit() {
            mWtpDiscoverListener.stopDiscover();
        }

        @Override
        public boolean processMessage(Message msg) {
            switch (msg.what) {
                case CMD_CONNECT:
                    if(mWtpDiscoverListener.connect()) {
                        transitionTo(mRuningState);
                    }
                case CMD_FAILED:
                    break;
                default:
                    break;
            }
            return HANDLED;
        }
    }

    private class RuningState extends State {
        @Override
        public void enter() {

        }

        @Override
        public void exit() {

        }

        @Override
        public boolean processMessage(Message msg) {
            return HANDLED;
        }
    }
}
