package com.raileanu.drawingtool;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import android.support.v7.widget.RecyclerView;

import com.raileanu.drawingtool.database.ColorDbHelper;
import com.raileanu.drawingtool.interfaces.OnColorPick;
import com.raileanu.drawingtool.interfaces.OnReset;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements OnColorPick,
        View.OnClickListener
{
//    http://www.vogella.com/tutorials/AndroidTouch/article.html


    // data fields
    static String quickSaveFileName = "my_last_painting";

    ColorDialogFragment colorDialogFragment;
    TouchEventView drawingView;
    HorizontalAdapter mArrayAdapter;
    ArrayList<Integer> mColorList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);

        if (getIntent().getBooleanExtra("EXIT", false))
        {
            System.exit(0);
        }

//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN );
        setContentView(R.layout.activity_main );

        drawingView = (TouchEventView) findViewById(R.id.drawingview);

        colorDialogFragment = ColorDialogFragment.newInstance();
        colorDialogFragment.setmOnColorPick(this);



        // set color list and array adapter
        mColorList = new ArrayList<>();
        // set up the ColorArrayAdapter
        mArrayAdapter = new HorizontalAdapter(mColorList);
        mArrayAdapter.setOnColorPick(this);

        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);

        RecyclerView colorListRecyclerView = (RecyclerView) findViewById(R.id.colorButtonListView);
        colorListRecyclerView.setLayoutManager(horizontalLayoutManager);
        colorListRecyclerView.setAdapter(mArrayAdapter);
        
        getFromDB();

        (findViewById(R.id.menuButton)).setOnClickListener(this);
        (findViewById(R.id.pickColorButton)).setOnClickListener(this);
        (findViewById(R.id.eraserButton)).setOnClickListener(this);

    }

    private void getFromDB()
    {
        // create the database helper, pass the context
        ColorDbHelper dbHelper = new ColorDbHelper(this);
        try
        {
            ArrayList<Integer> newList = dbHelper.readOrderedByPosition();

            mColorList.clear();
            mColorList.addAll(newList);

            System.out.println("[debug] color list size = " + mColorList.size());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        mArrayAdapter.notifyDataSetChanged();
        System.out.println("[debug] notified!!  " );
    }

    //
    //
    //


    @Override
    protected void onResume()
    {
        super.onResume();

        int applicationState = ApplicationState.getInstance().getState();

        if ( applicationState == ApplicationState.SAVE_STATE)
        {
            save();
        }
        if (applicationState == ApplicationState.LOAD_STATE)
        {
            load();
        }
        if (applicationState == ApplicationState.EXPORT_STATE)
        {
            export();
        }


        getFromDB();
    }

    public void save()
    {
        Log.println(Log.DEBUG, "save... ", "salvarea datelor..");

        ArrayList<PathStorage> list = drawingView.getPaintingData();
        Log.println(Log.DEBUG, "save... ", "list size = " + list.size());

        File file = new File(this.getFilesDir(), quickSaveFileName);

        try
        {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(list );

            objectOutputStream.close();
            fileOutputStream.close();

            Toast.makeText(getApplicationContext(),
                    "Data saved successfully! ",
                    Toast.LENGTH_LONG)
                    .show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void load()
    {
        Log.println(Log.DEBUG, "load... ", "incarcarea datelor..");

        ArrayList<PathStorage> list;
        File file = new File(this.getFilesDir(), quickSaveFileName);

        try
        {
            FileInputStream fileInputStream = new FileInputStream(file);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            Object inputObject =  objectInputStream.readObject();
//            System.out.println("object IS " + inputObject.toString() );

            list = (ArrayList<PathStorage>) inputObject;

            if (list != null)
            {
                drawingView.setPaintingData(list);
                System.out.println("did read something! ");
            }
            else
                System.out.println("a citit null ! ");

            objectInputStream.close();
            fileInputStream.close();

            Toast.makeText(getApplicationContext(),
                    "Data Loaded successfully! ",
                    Toast.LENGTH_LONG)
                    .show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    private Bitmap getBitmapFromView(View view)
    {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);

        return returnedBitmap;
    }

    //
    //  export
    //
    public void export()
    {
        Bitmap bmp = getBitmapFromView( findViewById(R.id.drawingview) );

        String filename = "ecran_de_desenare.png";
        String folderName = "drawing_app";


        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        if (!dir.mkdirs())
        {
            Log.println(Log.ERROR, "export... ", "directory not created! ");
        }
        File file = new File(dir, filename);
        if (file.exists ())
            file.delete ();
//        file.setReadable(true);
        Log.println(Log.DEBUG, "exporting... ", "exportarea datelor in " + dir);
        FileOutputStream out = null;

        try {
            out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            out.flush();
            // PNG is a lossless format, the compression factor (100) is ignored

            Toast.makeText(getApplicationContext(),
                    "Image Exported successfully! ",
                    Toast.LENGTH_LONG)
                    .show();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Log.println(Log.DEBUG, "exporting... ", "exportat img [" + bmp.getHeight() + " " + bmp.getWidth() + "]");
    }

    public void clearScreen()
    {
        drawingView.reset();
    }


    void showDialog()
    {
        colorDialogFragment.show(getFragmentManager(), "ColorDialogFragment");
    }



    @Override
    public void getRGBColor(int color)
    {
        TouchEventView touchEventView = (TouchEventView) findViewById(R.id.drawingview);
        touchEventView.setColor(color);
    }




    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.menuButton:
                Intent filterObjectivesIntent = new Intent(this, MainMenuActivity.class);
                Bundle bundle = new Bundle();

                filterObjectivesIntent.putExtra("main", bundle);
                startActivity(filterObjectivesIntent);
                break;

            case R.id.pickColorButton:
                showDialog();
                break;
            case R.id.eraserButton:
                getRGBColor(Color.WHITE);
                break;
        }
    }



}
