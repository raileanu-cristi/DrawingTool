package com.raileanu.drawingtool;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.raileanu.drawingtool.interfaces.OnColorPick;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cristian on 01.05.2017.
 */

public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.ViewHolder> implements View.OnClickListener
{
    private int mResource;
    private Context mContext;
    private OnColorPick onColorPick;
    private List<Integer> horizontalList;

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public Button colorButton;

        public ViewHolder(View view) {
            super(view);
            colorButton = (Button) view.findViewById(R.id.colorButton);

        }

    }


    public HorizontalAdapter(List<Integer> horizontalList)
    {
        this.horizontalList = horizontalList;
    }


    public void setOnColorPick(OnColorPick onColorPick)
    {
        this.onColorPick = onColorPick;
    }


    //
    //
    //
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.color_button_list_item, parent, false);

        return new ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position)
    {
        holder.colorButton.setBackgroundColor(horizontalList.get(position));
        holder.colorButton.setTag(position);
        holder.colorButton.setOnClickListener(this);

    }


    @Override
    public int getItemCount()
    {
        return horizontalList.size();
    }


    @Override
    public void onClick(View view)
    {
        int position = (int) view.getTag();
        Integer color = horizontalList.get(position);

        if (view.getId() == R.id.colorButton)
        {
            onColorPick.getRGBColor(color);
        }
    }
}
