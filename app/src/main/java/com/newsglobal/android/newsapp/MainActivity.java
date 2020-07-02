package com.newsglobal.android.newsapp;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    private InterstitialAd mInterstitialAd;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, "ca-app-pub-3707598583515158~6759262812");

        prepareAd();

        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(mInterstitialAd.isLoaded())
                        {
                            mInterstitialAd.show();
                        }
                        else
                        {
                            Log.d("TAG","Interstitial not loaded");
                        }

                        prepareAd();
                    }
                });
            }
        },600,600, TimeUnit.SECONDS);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomBar);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        loadFragment(new Home(this));

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //show the activity in full screen

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        Fragment fragment = null;

        switch (item.getItemId())
        {
            case R.id.home:
                fragment = new Home(this);
                break;

            case R.id.share:
                String shareText = "https://play.google.com/store/apps/details?id=com.newsglobal.android.newsapp";
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "GD NEWS");
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
                startActivity(Intent.createChooser(shareIntent, "Share app via"));
                break;

            case R.id.rate:
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                }
                catch (ActivityNotFoundException e)
                {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
                }
                break;
        }

        return loadFragment(fragment);
    }

    private boolean loadFragment(Fragment fragment)
    {
        if (fragment!=null)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            return true;
        }

        return false;
    }

    public void prepareAd()
    {
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3707598583515158/1966101133");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
    }

    @Override
    public void onBackPressed()
    {
        if(mInterstitialAd.isLoaded())
        {
            mInterstitialAd.show();

            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                    finish();
                }
            });
        }

        else
        {
            super.onBackPressed();
        }
    }
}
