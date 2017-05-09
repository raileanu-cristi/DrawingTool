package com.raileanu.drawingtool;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.raileanu.drawingtool.database.ColorDbHelper;
import com.raileanu.drawingtool.interfaces.OnDatabaseChanged;

import java.util.ArrayList;

/**
 * Created by Cristian on 30.04.2017.
 */

public class ColorArrayAdapter extends ArrayAdapter<Integer> implements View.OnClickListener
{
    private int mResource;
    private Context mContext;
    private OnDatabaseChanged onDatabaseChanged;



    public static class ViewHolder
    {
        View colorView;
        Button upButton;
        Button deleteButton;
    }

    public ColorArrayAdapter(Context context, int resource, ArrayList<Integer> objects)
    {
        super(context, resource, objects);

        Log.println(Log.DEBUG, "ColorArrayAdapter: ", "constructor" );
        this.mResource = resource;
        this.mContext = context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view = convertView;

        Log.println(Log.DEBUG, "getView : position = ", String.valueOf(position) );

        if (view == null)
        {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            view = inflater.inflate(mResource, null);

            ViewHolder viewHolder = new ViewHolder();

            viewHolder.colorView = view.findViewById(R.id.colorListItemView);
            viewHolder.upButton = (Button) view.findViewById(R.id.upButton);
            viewHolder.deleteButton = (Button) view.findViewById(R.id.deleteButton);

            view.setTag(viewHolder);
        }

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        Integer color = getItem(position);
        Log.println(Log.DEBUG, "getView : color = ", String.valueOf(color) );
        viewHolder.colorView.setBackgroundColor( color );
        viewHolder.colorView.setFocusable(false);

        viewHolder.upButton.setOnClickListener(this);
        viewHolder.upButton.setTag(position);
        viewHolder.deleteButton.setOnClickListener(this);
        viewHolder.deleteButton.setTag(position);

        return view;
    }

    //
    //
    //
    public void setOnDatabaseChanged(OnDatabaseChanged context )
    {
        onDatabaseChanged = context;
    }


    @Override
    public void onClick(View view)
    {
        System.out.println("on click () !!! ");
        int position = (int) view.getTag();

        if (view.getId() == R.id.upButton)
        {
            ColorDbHelper dbHelper = new ColorDbHelper( getContext() );

            int databasePosition = position + 1;
            if (databasePosition > 1)
                dbHelper.updatePosition(String.valueOf(databasePosition), String.valueOf(databasePosition - 1) );
        }
        if (view.getId() == R.id.deleteButton)
        {
            System.out.println("delete button pressed! ");
            ColorDbHelper dbHelper = new ColorDbHelper( getContext() );

            int databasePosition = position + 1;
            dbHelper.delete(String.valueOf(databasePosition) );
        }

        onDatabaseChanged.refresh();
    }
}
