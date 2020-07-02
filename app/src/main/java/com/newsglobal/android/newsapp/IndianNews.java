package com.newsglobal.android.newsapp;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.newsglobal.android.newsapp.Models.Articles;
import com.newsglobal.android.newsapp.Models.Headlines;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IndianNews extends AppCompatActivity {

    private SearchView searchView;
    private String etQuery = "";

    ViewPager2 viewPager2;
    GlobalAdapter globalAdapter;
    List<Articles> articles = new ArrayList<>();

    SwipeRefreshLayout swipeRefreshLayout;

    final String API_KEY = "29370840b462496c8ecc7600a18cb1e6";

    final String country = "in";

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indian_news);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //show the activity in full screen

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        viewPager2 = findViewById(R.id.globalView);

        swipeRefreshLayout = findViewById(R.id.swipeRefresh);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                    retrieveJson("", country, API_KEY);
            }
        });
            retrieveJson("", country, API_KEY);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.95f + r * 0.05f);
            }
        });

        viewPager2.registerOnPageChangeCallback( new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled( int position, float v, int i1 ) {
            }

            @Override
            public void onPageSelected( int position ) {
            }

            @Override
            public void onPageScrollStateChanged( int state ) {
                enableDisableSwipeRefresh( state == ViewPager.SCROLL_STATE_IDLE );
            }
        } );

        viewPager2.setPageTransformer(compositePageTransformer);

        getIntent();
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);

        if(Intent.ACTION_SEARCH.equals(intent.getAction()))
        {
            String query = intent.getStringExtra(SearchManager.QUERY);
            searchView.setQuery(String.valueOf(query), false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.global_bar, menu);

        MenuItem menuItem = menu.findItem(R.id.global_bar);
        searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Type here to search.....");

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        assert searchManager != null;
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                etQuery = searchView.getQuery().toString();

                if (!etQuery.equals(""))
                {
                    swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            retrieveJson(etQuery, country, API_KEY);
                        }
                    });

                    retrieveJson(etQuery, country, API_KEY);
                }

                else
                {
                    swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            retrieveJson("", country, API_KEY);
                        }
                    });

                    retrieveJson("", country, API_KEY);
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }

    public void retrieveJson(String query, String country, String apiKey)
    {
        swipeRefreshLayout.setRefreshing(true);

        Call<Headlines> call;

        if(!query.equals(""))
        {
            call = ApiClient.getInstance().getApi().getSpecificData(query, apiKey);
        }
        else
        {
            call = ApiClient.getInstance().getApi().getHeadLines(country, apiKey);
        }

        call.enqueue(new Callback<Headlines>() {
            @Override
            public void onResponse(@NonNull Call<Headlines> call, @NonNull Response<Headlines> response) {
                assert response.body() != null;
                if (response.isSuccessful() && response.body().getArticles() != null)
                {
                    swipeRefreshLayout.setRefreshing(false);
                    articles.clear();
                    articles = response.body().getArticles();
                    globalAdapter = new GlobalAdapter(IndianNews.this, articles);

                    viewPager2.setAdapter(globalAdapter);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Headlines> call, @NonNull Throwable t) {

                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(IndianNews.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public String getCountry()
    {
        Locale locale = Locale.getDefault();
        String country = locale.getCountry();
        return country.toLowerCase();
    }

    private void enableDisableSwipeRefresh(boolean enable) {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setEnabled(enable);
        }
    }
}