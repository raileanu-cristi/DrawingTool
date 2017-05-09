package com.raileanu.drawingtool;

/**
 * Created by Cristi on 11.04.2017.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.io.Serializable;
import java.util.ArrayList;

public class TouchEventView extends View
{
    static int MOVE_TO = 0;
    static int LINE_TO = 1;

    private ArrayList<PaintingObject> mPaintingList;
    private ArrayList<PathStorage> mPathStorageList;
    Context context;

    public TouchEventView(Context context, AttributeSet attrs)
    {

        super(context, attrs);


        this.context = context;

        reset();
    }

    public ArrayList<PathStorage> getPaintingData()
    {
        return mPathStorageList;
    }

    public void setPaintingData(ArrayList arrayList)
    {
        mPathStorageList = (ArrayList<PathStorage>) arrayList;
        mPaintingList = new ArrayList<>();

        for (int i=0; i < mPathStorageList.size(); i++ )
        {
            PathStorage pathStorage = mPathStorageList.get(i);

            Path newPath = new Path();

            for (int j=0; j< pathStorage.getPoints().size(); j++ )
            {
                PathStorage.Point point = pathStorage.getPoints().get(j);

                if (point.getType() == MOVE_TO)
                    newPath.moveTo(point.getX(), point.getY() );
                else
                    newPath.lineTo(point.getX(), point.getY() );
            }

            PaintingObject newPaintingObj = new PaintingObject();
            newPaintingObj.setPath(newPath);
            newPaintingObj.setColor(pathStorage.mColor);

            mPaintingList.add(newPaintingObj);
        }
    }

    public void setColor(int rgb)
    {
        PaintingObject paintingObject = new PaintingObject();

        paintingObject.setColor(rgb);
        mPaintingList.add(paintingObject);

        PathStorage pathStorage = new PathStorage(rgb);
        mPathStorageList.add(pathStorage);
    }

    public void reset()
    {
        mPaintingList = new ArrayList<>();

        mPaintingList.add(new PaintingObject());

        mPathStorageList = new ArrayList<>();
        mPathStorageList.add(new PathStorage(0));
    }

//    private class GestureListener extends GestureDetector.SimpleOnGestureListener
//    {
//        @Override
//        public boolean onDown(MotionEvent e) {
//            return true;
//        }
//        // event when double tap occurs
//        @Override
//        public boolean onDoubleTap(MotionEvent e)
//        {
//            mOnActivityAction.showRGBADialog();
//            return true;
//        }
//
//    }


    @Override
    protected void onDraw(Canvas canvas)
    {
        for (PaintingObject paintingObject : mPaintingList)
            canvas.drawPath(paintingObject.getPath(), paintingObject.getPaint());
    }


    @Override
    public boolean onTouchEvent(MotionEvent event)
    {

        float eventX = event.getX();
        float eventY = event.getY();
        Path path = mPaintingList.get(mPaintingList.size() -1 ).getPath();
        PathStorage pathStorage = mPathStorageList.get( mPathStorageList.size() - 1 );

        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(eventX, eventY);
                pathStorage.addPoint(eventX, eventY, MOVE_TO);
                break;
            case MotionEvent.ACTION_MOVE:

                path.lineTo(eventX, eventY);
                pathStorage.addPoint(eventX, eventY, LINE_TO);
                break;
            case MotionEvent.ACTION_UP:

                break;
            default:
                break;
        }

        // Schedules a repaint.
        invalidate();
        return true;
    }


    private class PaintingObject
    {
        private Paint mPaint;
        private Path mPath;

        public PaintingObject()
        {
            mPaint = getInitialPaint();
            mPath = new Path();
        }

        public void setColor(int rgb)
        {
            mPaint.setColor(rgb);
        }

        public Path getPath()
        {
            return mPath;
        }

        public Paint getPaint()
        {
            return mPaint;
        }

        public void setPath(Path path)
        {
            mPath = path;
        }


        private Paint getInitialPaint()
        {
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStrokeWidth(6f);
            paint.setColor(Color.BLACK);

            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);

            return paint;
        }
    }
}