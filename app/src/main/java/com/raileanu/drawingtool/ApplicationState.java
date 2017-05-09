package com.raileanu.drawingtool;

/**
 * Created by Cristian on 01.05.2017.
 */

public class ApplicationState
{
    private static final ApplicationState ourInstance = new ApplicationState();

    static int SAVE_STATE = 1;
    static int LOAD_STATE = 2;
    static int NOTHING_STATE = 0;
    static int EXPORT_STATE = 3;

    private int mState;

    public static ApplicationState getInstance()
    {
        return ourInstance;
    }


    private ApplicationState()
    {
        mState = ApplicationState.NOTHING_STATE;
    }

    public void setState(int state)
    {
        mState = state;
    }

    public int getState()
    {
        int state = mState;
        mState = ApplicationState.NOTHING_STATE;
        return state;
    }
}
