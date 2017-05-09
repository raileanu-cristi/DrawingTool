package com.raileanu.drawingtool;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Cristian on 02.05.2017.
 */

public class PalleteArrayAdapter extends ArrayAdapter<JSONObject> implements CompoundButton.OnCheckedChangeListener
{
    private int mResource;
    private Context mContext;
    private boolean[] mIsChecked;



    public static class ViewHolder
    {
        Switch addPalleteSwitch;
        TextView palleteNameTextView;
    }

    public PalleteArrayAdapter(Context context, int resource, ArrayList<JSONObject> objects)
    {
        super(context, resource, objects);

        Log.println(Log.DEBUG, "ColorArrayAdapter: ", "constructor" );
        this.mResource = resource;
        this.mContext = context;

        mIsChecked = new boolean[objects.size() ];
    }

    //
    //
    //
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view = convertView;

        Log.println(Log.DEBUG, "getView : position = ", String.valueOf(position) );

        if (view == null)
        {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            view = inflater.inflate(mResource, null);

            PalleteArrayAdapter.ViewHolder viewHolder = new PalleteArrayAdapter.ViewHolder();

            viewHolder.addPalleteSwitch = (Switch) view.findViewById(R.id.addPalleteSwitch);
            viewHolder.palleteNameTextView = (TextView) view.findViewById(R.id.palleteNameTextView);

            view.setTag(viewHolder);
        }

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        JSONObject palleteJSON = getItem(position);

        String palleteName = "error";
        try
        {
            palleteName = palleteJSON.getString("name");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        viewHolder.palleteNameTextView.setText(palleteName);
        viewHolder.addPalleteSwitch.setOnCheckedChangeListener(this);
        viewHolder.addPalleteSwitch.setTag(position);

        return view;
    }


    public boolean isChecked(int position)
    {
        return mIsChecked[position];
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
    {
        int position = (int) buttonView.getTag();

        mIsChecked[position] = isChecked;
    }
}
