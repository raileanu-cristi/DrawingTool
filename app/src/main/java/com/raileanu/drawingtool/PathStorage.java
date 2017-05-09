package com.raileanu.drawingtool;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Cristian on 01.05.2017.
 */

public class PathStorage implements Serializable
{
    int mColor;
    ArrayList<Point> mPoints;

    public PathStorage(int color)
    {
        mColor = color;
        mPoints = new ArrayList<>();
    }

    public void addPoint(float x, float y, int type)
    {
        mPoints.add(new Point(x,y,type));
    }

    public ArrayList<Point> getPoints()
    {
        return mPoints;
    }

    public class Point implements Serializable
    {
        float mX;
        float mY;
        int mType;

        public Point(float x, float y, int type)
        {
            mX = x;
            mY = y;
            mType = type;
        }

        public float getX()
        {
            return mX;
        }

        public float getY()
        {
            return mY;
        }

        public int getType()
        {
            return mType;
        }
    }
}
