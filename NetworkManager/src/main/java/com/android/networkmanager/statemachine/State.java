package com.android.networkmanager.statemachine;

import android.os.Message;

/**
 *
 *
 * The class for implementing states in a StateMachine
 */
public class State implements IState {

    /**
     * Constructor
     */protected State() {
    }

    /* (non-Javadoc)
     * @see com.android.internal.util.IState#enter()
     */
    @Override
    public void enter() {
    }

    /* (non-Javadoc)
     * @see com.android.internal.util.IState#exit()
     */
    @Override
    public void exit() {
    }

    /* (non-Javadoc)
     * @see com.android.internal.util.IState#processMessage(android.os.Message)
     */
    @Override
    public boolean processMessage(Message msg) {
        return false;
    }


    @Override
    public String getName() {
        String name = getClass().getName();
        int lastDollar = name.lastIndexOf('$');
        return name.substring(lastDollar + 1);
    }
}
