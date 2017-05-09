package com.raileanu.drawingtool.database;

import android.provider.BaseColumns;

/**
 * Created by Cristian on 29.04.2017.
 */

public class ColorDBContract
{
    public static final String DB_NAME = "com.raileanu.drawingtool.database";
    public static final int DB_VERSION = 1;

    public class ColorEntry implements BaseColumns
    {
        public static final String TABLE = "colors";

        public static final String COL_COLOR_ID = "id";
        public static final String COL_COLOR_VALUE = "value";
        public static final String COL_COLOR_POSITION = "position";
    }
}
