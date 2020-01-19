package com.projectcs2103t.chinesechess;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView;


public class MyActivity extends ActionBarActivity {
    public UI ui;
    public TextView debugInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        ui = null;
        debugInfo = null;
        newGame();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void newGame() {
        ViewGroup mainViewGroup = (ViewGroup) findViewById(R.id.MainView);
        //View button = (View) findViewById(R.id.NewGameBtn);
        //button.setVisibility(View.INVISIBLE);

        if(ui!=null){
            mainViewGroup.removeView(ui);
            ui=null;
        }
        if(debugInfo!=null){
            mainViewGroup.removeView(debugInfo);
            debugInfo=null;
        }

        ui = new UI(getApplicationContext());
        ui.setId(R.id.uiId);
        ui.setGameOverListener(new UI.GameOverListener() {
            @Override
            public void gameOver(String message) {
                showDialogue(message);
            }
        });
        ui.setDebugMessageListener(new UI.DebugMessageListener(){
            @Override
            public void handleDebugMessage(String message){
                setDebugMessage(message);
            }
        });
        debugInfo = new TextView(this);
        debugInfo.setId(R.id.debugTextId);
        debugInfo.setText("Debug Info:");

        RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, getWindowManager().getDefaultDisplay().getWidth()/8*9);
        RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params2.addRule(RelativeLayout.BELOW, ui.getId());
        mainViewGroup.addView(ui,params1);
        mainViewGroup.addView(debugInfo,params2);
    }
    public void setDebugMessage(String message){
        debugInfo.setText(message);
        debugInfo.invalidate();
    }
    public void showDialogue(String message){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setMessage(message);
        alertBuilder.setCancelable(false);
        alertBuilder.setPositiveButton("New Game",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        newGame();
                    }
                });
        alertBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = alertBuilder.create();
        alert.show();
    }
}
