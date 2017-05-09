package com.raileanu.drawingtool;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.raileanu.drawingtool.interfaces.OnReset;

public class MainMenuActivity extends AppCompatActivity implements View.OnClickListener
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        findViewById(R.id.resumeButton ).setOnClickListener(this);
        findViewById(R.id.newButton ).setOnClickListener(this);
        findViewById(R.id.manageColorsButton ).setOnClickListener(this);
        findViewById(R.id.saveButton ).setOnClickListener(this);
        findViewById(R.id.loadButton ).setOnClickListener(this);
        findViewById(R.id.exportButton ).setOnClickListener(this);
        findViewById(R.id.importButton ).setOnClickListener(this);
        findViewById(R.id.exitButton ).setOnClickListener(this);
    }


    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.resumeButton:
                this.finish();

                break;
            case R.id.newButton:
                Intent clearScreenIntent = new Intent(getApplicationContext(), MainActivity.class);
                clearScreenIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(clearScreenIntent);

                break;
            case R.id.manageColorsButton:
                Intent manageColorsIntent = new Intent(this, ManageColorsActivity.class);
                startActivity(manageColorsIntent);

                break;
            case R.id.saveButton:

                ApplicationState.getInstance().setState(ApplicationState.SAVE_STATE);
                finish();
                break;
            case R.id.loadButton:

                ApplicationState.getInstance().setState(ApplicationState.LOAD_STATE);
                finish();
                break;
            case R.id.exportButton:

                ApplicationState.getInstance().setState(ApplicationState.EXPORT_STATE);
                finish();
                break;
            case R.id.importButton:

                Intent importIntent = new Intent(this, ImportPalletesActivity.class);
                startActivity(importIntent);
                break;
            case R.id.exitButton:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("EXIT", true);
                startActivity(intent);
                break;
        }
    }
}
