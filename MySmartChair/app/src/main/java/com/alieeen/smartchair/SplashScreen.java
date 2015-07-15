package com.alieeen.smartchair;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.alieeen.smartchair.util.SharedPrefs;


public class SplashScreen extends AppCompatActivity implements Runnable{

    private int sleep_time = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        Handler h = new Handler();
        h.postDelayed(this, sleep_time);



    }

    private void setUpActionBar() {
        ActionBar ab = getSupportActionBar();
        ab.hide();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_splash_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void run() {

        boolean isLoggedIn = SharedPrefs.isLoggedIn(this);

        //TODO testing only
        isLoggedIn = false;

        Intent i;

        if (isLoggedIn) {
            i = new Intent(this, MainActivity.class);
        } else {
            i = new Intent(this, WalkthroughActivity.class);
        }
        startActivity(i);
        finish();
    }
}
