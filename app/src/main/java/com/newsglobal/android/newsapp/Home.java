package com.newsglobal.android.newsapp;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.List;

public class Home extends Fragment {

    private Handler sliderHandler = new Handler();
    private ViewPager2 locationsViewPager;

    private Context context;

    Home(Context context)
    {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        MobileAds.initialize(context, "ca-app-pub-3707598583515158~6759262812");

        AdView mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        locationsViewPager = view.findViewById(R.id.locationsViewPager);

        List<HomeItems> homeItems = new ArrayList<>();

        HomeItems global_news = new HomeItems();
        global_news.imageId = R.drawable.global_news;
        global_news.title = "GLOBAL";
        homeItems.add(global_news);

        HomeItems indian_news = new HomeItems();
        indian_news.imageId = R.drawable.india_news;
        indian_news.title = "INDIA";
        homeItems.add(indian_news);

        HomeItems business_news = new HomeItems();
        business_news.imageId = R.drawable.business_news;
        business_news.title = "BUSINESS";
        homeItems.add(business_news);

        HomeItems health_news = new HomeItems();
        health_news.imageId = R.drawable.health_news;
        health_news.title = "HEALTH";
        homeItems.add(health_news);

        HomeItems science_news = new HomeItems();
        science_news.imageId = R.drawable.science_news;
        science_news.title = "SCIENCE";
        homeItems.add(science_news);

        HomeItems technology_news = new HomeItems();
        technology_news.imageId = R.drawable.technology_news;
        technology_news.title = "TECHNOLOGY";
        homeItems.add(technology_news);

        HomeItems entertainment_news = new HomeItems();
        entertainment_news.imageId = R.drawable.entertainment_news;
        entertainment_news.title = "ENTERTAINMENT";
        homeItems.add(entertainment_news);

        HomeItems sports_news = new HomeItems();
        sports_news.imageId = R.drawable.sports_news;
        sports_news.title = "SPORTS";
        homeItems.add(sports_news);

        HomeItemsAdapter adapter = new HomeItemsAdapter(Home.this, homeItems, locationsViewPager);

        locationsViewPager.setAdapter(adapter);

        locationsViewPager.setClipToPadding(false);
        locationsViewPager.setClipChildren(false);
        locationsViewPager.setOffscreenPageLimit(3);
        locationsViewPager.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.95f + r * 0.05f);
            }
        });

        locationsViewPager.setPageTransformer(compositePageTransformer);

        locationsViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 3500); //slide duration 3 seconds
            }
        });

        return view;
    }

    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            locationsViewPager.setCurrentItem(locationsViewPager.getCurrentItem() + 1);
        }
    };

    @Override
    public void onPause()
    {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        sliderHandler.postDelayed(sliderRunnable, 2500);
    }
}