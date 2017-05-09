package com.raileanu.drawingtool;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SeekBar;

import com.raileanu.drawingtool.database.ColorDbHelper;
import com.raileanu.drawingtool.interfaces.OnDatabaseChanged;

import java.util.ArrayList;

public class ManageColorsActivity extends AppCompatActivity implements View.OnClickListener,
                                    SeekBar.OnSeekBarChangeListener, OnDatabaseChanged, AdapterView.OnItemClickListener
{

    private ArrayList<Integer> mColorList;
    private ColorDbHelper mDbHelper;
    private int mSelectedColor;
    private ColorArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_colors);

        // set the buttons
        findViewById(R.id.backButton).setOnClickListener(this);
        findViewById(R.id.addColorButton).setOnClickListener(this);
        findViewById(R.id.confirmAddButton).setOnClickListener(this);
        findViewById(R.id.cancelAddButton).setOnClickListener(this);
        findViewById(R.id.clearAllButton).setOnClickListener(this);

        // set the seekbars
        ((SeekBar) findViewById(R.id.redSeekbar) ).setOnSeekBarChangeListener(this);
        ((SeekBar) findViewById(R.id.greenSeekbar) ).setOnSeekBarChangeListener(this);
        ((SeekBar) findViewById(R.id.blueSeekbar) ).setOnSeekBarChangeListener(this);
        mSelectedColor = Color.rgb(((SeekBar) findViewById(R.id.redSeekbar) ).getProgress(),
                                    ((SeekBar) findViewById(R.id.greenSeekbar) ).getProgress(),
                                    ((SeekBar) findViewById(R.id.blueSeekbar) ).getProgress());


        // create the database helper, pass the context
        mDbHelper = new ColorDbHelper(this);

        mColorList = new ArrayList<>();
        // set up the ColorArrayAdapter
        arrayAdapter = new ColorArrayAdapter(this, R.layout.manage_color_list_item , mColorList);
        arrayAdapter.setOnDatabaseChanged(this);

        ListView colorListView = (ListView) findViewById(R.id.colorListView);
        colorListView.setAdapter(arrayAdapter);

        getFromDB();

    }

    //
    //
    //
    public void getFromDB()
    {

        try
        {
            ArrayList<Integer> newList = mDbHelper.readOrderedByPosition();

            mColorList.clear();
            mColorList.addAll(newList);

            System.out.println("[debug] color list size = " + mColorList.size());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        arrayAdapter.notifyDataSetChanged();
        System.out.println("[debug] notified!!  " );
    }


    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.addColorButton:
                findViewById(R.id.colorPickLayout).setVisibility(View.VISIBLE);
                findViewById(R.id.manageColorsButtonsLayout).setVisibility(View.GONE);
                break;

            case R.id.confirmAddButton:

                int color = mSelectedColor;

                // insert in database
                Log.println(Log.DEBUG, "confirmAdd : color = ", String.valueOf(color) );
                mDbHelper.insert(String.valueOf(color) );

                getFromDB();

                findViewById(R.id.colorPickLayout).setVisibility(View.GONE);
                findViewById(R.id.manageColorsButtonsLayout).setVisibility(View.VISIBLE);
                break;

            case R.id.cancelAddButton:

                findViewById(R.id.colorPickLayout).setVisibility(View.GONE);
                findViewById(R.id.manageColorsButtonsLayout).setVisibility(View.VISIBLE);
                break;

            case R.id.clearAllButton:

                mDbHelper.deleteAll();
                getFromDB();

                break;

            case R.id.backButton:
                finish();
                break;

            case R.id.colorListView:
                getFromDB();
                break;
        }


    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
    {
        int red = ((SeekBar) findViewById(R.id.redSeekbar) ).getProgress();
        int green = ((SeekBar) findViewById(R.id.greenSeekbar) ).getProgress();
        int blue = ((SeekBar) findViewById(R.id.blueSeekbar) ).getProgress();


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
        mSelectedColor = Color.rgb(red, green, blue);


        View previewView = findViewById(R.id.previewView);
        previewView.setBackgroundColor( mSelectedColor );
    }


    @Override
    public void onStartTrackingTouch(SeekBar seekBar)
    {

    }


    @Override
    public void onStopTrackingTouch(SeekBar seekBar)
    {

    }


    @Override
    public void refresh()
    {
        getFromDB();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        Log.println(Log.DEBUG, "onItemClick" ," onItemClick!!!");
    }
}
