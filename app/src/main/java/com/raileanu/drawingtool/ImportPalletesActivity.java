package com.raileanu.drawingtool;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.raileanu.drawingtool.database.ColorDbHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ImportPalletesActivity extends AppCompatActivity implements View.OnClickListener
{

    private String TAG = MainActivity.class.getSimpleName();

    private ProgressDialog mProgressDialog;
    private ListView mListView;
    private static String url = "https://api.myjson.com/bins/8cb2d"; // URL to get palletes JSON
    ArrayList<Integer> mAddedColorList;
    ArrayList<JSONObject> mPalleteJSONList;
    PalleteArrayAdapter mArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_palletes);

        ( findViewById(R.id.confirmImportButton) ).setOnClickListener(this);
        ( findViewById(R.id.cancelImportButton) ).setOnClickListener(this);

        mAddedColorList = new ArrayList<>();

        mPalleteJSONList = new ArrayList<>();
        mListView = (ListView) findViewById(R.id.palletesListView);
        new GetPalletes().execute();
    }


    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.confirmImportButton:

                ColorDbHelper dbHelper = new ColorDbHelper(this);

                for ( int i=0; i< mPalleteJSONList.size(); i++ )
                    if ( mArrayAdapter.isChecked(i) )
                    {
                        JSONObject jsonObject = mPalleteJSONList.get(i);

                        try
                        {
                            JSONArray colorsJSON = jsonObject.getJSONArray("elements");

                            for (int j=0; j< colorsJSON.length(); j++ )
                            {
                                JSONObject colorJSON = colorsJSON.getJSONObject(j);

                                int red = colorJSON.getInt("red");
                                int green = colorJSON.getInt("green");
                                int blue = colorJSON.getInt("blue");

                                dbHelper.insert(String.valueOf(Color.rgb(red, green, blue) ) );
                            }
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }

                this.finish();
                break;

            case R.id.cancelImportButton:
                this.finish();
                break;
        }
    }


    private class GetPalletes extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected void onPreExecute()
        {

            super.onPreExecute();
            // Showing progress dialog
            mProgressDialog = new ProgressDialog(ImportPalletesActivity.this);
            mProgressDialog.setMessage("Please wait...");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();

        }


        @Override
        protected Void doInBackground(Void... arg0)
        {

            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null)
            {
                try
                {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray palletes = jsonObj.getJSONArray("palletes");

                    // looping through All palletes
                    for (int i = 0; i < palletes.length(); i++)
                    {
                        JSONObject pallete = palletes.getJSONObject(i);

                        mPalleteJSONList.add(pallete);

                    }
                }
                catch (final JSONException e)
                {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {

                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else
            {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {

                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }


        @Override
        protected void onPostExecute(Void result)
        {

            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();

            /**
             * Updating parsed JSON data into ListView
             * */
            mArrayAdapter = new PalleteArrayAdapter(ImportPalletesActivity.this, R.layout.import_palletes_list_item , mPalleteJSONList);
//

            mListView.setAdapter(mArrayAdapter);
        }

    }


}
