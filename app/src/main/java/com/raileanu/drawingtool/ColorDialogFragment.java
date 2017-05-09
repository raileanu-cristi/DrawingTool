package com.raileanu.drawingtool;

import android.app.DialogFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.raileanu.drawingtool.interfaces.OnColorPick;

/**
 * Created by Cristi on 12.04.2017.
 */

public class ColorDialogFragment extends DialogFragment implements SeekBar.OnSeekBarChangeListener
{
    private View mView;
    private SeekBar mRedSeekBar;
    private SeekBar mGreenSeekBar;
    private SeekBar mBlueSeekBar;
    int mColor;

    private OnColorPick mOnColorPick;

    static ColorDialogFragment newInstance()
    {
        return new ColorDialogFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_colorpicker, container, false);
        mRedSeekBar = (SeekBar) mView.findViewById(R.id.redSeekbar);
        mGreenSeekBar = (SeekBar) mView.findViewById(R.id.greenSeekbar);
        mBlueSeekBar = (SeekBar) mView.findViewById(R.id.blueSeekbar);
        mColor = Color.rgb(mRedSeekBar.getProgress(), mGreenSeekBar.getProgress(), mBlueSeekBar.getProgress());

        addListener();

        return mView;
    }

    private void addListener()
    {

        mRedSeekBar.setOnSeekBarChangeListener(this);
        mGreenSeekBar.setOnSeekBarChangeListener(this);
        mBlueSeekBar.setOnSeekBarChangeListener(this);
    }


    /**
     * Notification that the progress level has changed. Clients can use the fromUser parameter
     * to distinguish user-initiated changes from those that occurred programmatically.
     *
     * @param seekBar  The SeekBar whose progress has changed
     * @param progress The current progress level. This will be in the range 0..max where max
     *                 was set by {@link ProgressBar#setMax(int)}. (The default value for max is 100.)
     * @param fromUser True if the progress change was initiated by the user.
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
    {

        int red = mRedSeekBar.getProgress();
        int green = mGreenSeekBar.getProgress();
        int blue = mBlueSeekBar.getProgress();

        int seekBarId = seekBar.getId();
        switch (seekBarId)
        {
            case (R.id.redSeekbar):
                red = progress;
                break;
            case (R.id.greenSeekbar):
                green = progress;
                break;
            case (R.id.blueSeekbar):
                blue = progress;
                break;
        }

        // set the preview color
        mColor = Color.rgb(red, green, blue);

        View previewView = (View) mView.findViewById(R.id.previewView);
        previewView.setBackgroundColor( mColor );
    }


    /**
     * Notification that the user has started a touch gesture. Clients may want to use this
     * to disable advancing the seekbar.
     *
     * @param seekBar The SeekBar in which the touch gesture began
     */
    @Override
    public void onStartTrackingTouch(SeekBar seekBar)
    {

    }


    /**
     * Notification that the user has finished a touch gesture. Clients may want to use this
     * to re-enable advancing the seekbar.
     *
     * @param seekBar The SeekBar in which the touch gesture began
     */
    @Override
    public void onStopTrackingTouch(SeekBar seekBar)
    {

    }

    public void setmOnColorPick(OnColorPick mOnColorPick)
    {
        this.mOnColorPick = mOnColorPick;
    }


    @Override
    public void onDestroy()
    {
        int color = mColor;
        mOnColorPick.getRGBColor(color);
        super.onDestroy();
    }
}
