package com.raileanu.drawingtool.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Cristian on 29.04.2017.
 */

public class ColorDbHelper extends SQLiteOpenHelper
{

    public ColorDbHelper(Context context)
    {
        super(context, ColorDBContract.DB_NAME, null, ColorDBContract.DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String createTable = "CREATE TABLE IF NOT EXISTS " + ColorDBContract.ColorEntry.TABLE +
                " (" + ColorDBContract.ColorEntry.COL_COLOR_VALUE + " INT(8), "
                + ColorDBContract.ColorEntry.COL_COLOR_POSITION + " INT(3))";


        System.out.println("Create table " + createTable);
        db.execSQL(createTable);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + ColorDBContract.ColorEntry.TABLE);
        onCreate(db);
    }

    //
    //          insert
    //
    public void insert(String value)
    {
        int newPosition = nrRows() + 1;

        ContentValues contentValues = new ContentValues();
        contentValues.put(ColorDBContract.ColorEntry.COL_COLOR_VALUE, value );
        contentValues.put(ColorDBContract.ColorEntry.COL_COLOR_POSITION, newPosition );

        SQLiteDatabase db = this.getWritableDatabase();

        db.insertWithOnConflict(ColorDBContract.ColorEntry.TABLE,
                null,
                contentValues,
                SQLiteDatabase.CONFLICT_REPLACE);

        db.close();
    }

    //
    //      nrRows
    //
    public int nrRows()
    {
        int nrEntries = 0;
        String queryString = "SELECT * FROM " + ColorDBContract.ColorEntry.TABLE;

        SQLiteDatabase db = this.getWritableDatabase();

        try
        {
            Cursor cursor = db.rawQuery(queryString, null);

            int index = cursor.getColumnIndex("nr");

            nrEntries = cursor.getCount();

            System.out.println("[debug] nrEntries = " + nrEntries);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            db.close();
        }

        return nrEntries;
    }

    public void delete(String position)
    {
        Log.println(Log.DEBUG, "delete", "deleting position " + position);
        int oldPosition = nrRows();

        String deleteScript = "delete from " + ColorDBContract.ColorEntry.TABLE +
                              " where "+ ColorDBContract.ColorEntry.COL_COLOR_POSITION + " = " + position;
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(deleteScript);


        updatePosition(String.valueOf(oldPosition) , position );
    }

    public void deleteAll()
    {
        String deleteScript = "delete from " + ColorDBContract.ColorEntry.TABLE;
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(deleteScript);
    }

    public void updatePosition(String oldPosition, String newPosition)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        System.out.println("Update method position " + oldPosition);


        try
        {
            String columnName = ColorDBContract.ColorEntry.COL_COLOR_POSITION;

            String extraPosition= "0";

            // move from newPosition to 0
            ContentValues contentValues = new ContentValues();
            contentValues.put(columnName, extraPosition);

            String whereClause = ColorDBContract.ColorEntry.COL_COLOR_POSITION + " = ?";
            String[] whereArgs = new String[]{ String.valueOf(newPosition) };
            db.update(ColorDBContract.ColorEntry.TABLE, contentValues, whereClause, whereArgs  );

            // move from oldPosition to newPosition
            ContentValues updateValues = new ContentValues();
            updateValues.put(columnName, newPosition);
            String[] whereArgs2 = new String[]{ String.valueOf(oldPosition) };
            db.update(ColorDBContract.ColorEntry.TABLE, updateValues, whereClause, whereArgs2  );

            // move from 0 to oldPosition
            ContentValues values = new ContentValues();
            values.put(columnName, oldPosition);                  // where to move

            String[] whereArgs3 = new String[]{ String.valueOf(extraPosition) }; // where to find
            db.update(ColorDBContract.ColorEntry.TABLE, values, whereClause, whereArgs3  );
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            db.close();
        }
    }

    public ArrayList<Integer> readOrderedByPosition()
    {
        ArrayList<Integer> colorList = new ArrayList<>();

        try
        {

            SQLiteDatabase db = this.getWritableDatabase();

            String queryString = "SELECT * FROM " + ColorDBContract.ColorEntry.TABLE +
                                 " ORDER BY "+ColorDBContract.ColorEntry.COL_COLOR_POSITION;

            Cursor cursor = db.rawQuery(queryString, null);

            int colorValueIndex = cursor.getColumnIndex(ColorDBContract.ColorEntry.COL_COLOR_VALUE);


            if (cursor != null && cursor.moveToFirst())
            {
                do
                {
                    Integer color = cursor.getInt(colorValueIndex);

                    System.out.println("READ colorValue " + color);
                    colorList.add(color);


                } while (cursor.moveToNext());
            }

            cursor.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return colorList;
    }

}
