package com.newsglobal.android.newsapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.newsglobal.android.newsapp.Models.Articles;
import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GlobalAdapter extends RecyclerView.Adapter<GlobalAdapter.GlobalViewHolder> {

    private Context context;
    private List<Articles> articles;

    GlobalAdapter(Context context, List<Articles> articles) {
        this.context = context;
        this.articles = articles;
    }

    @NonNull
    @Override
    public GlobalAdapter.GlobalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_items, parent, false);

        return new GlobalViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final GlobalAdapter.GlobalViewHolder holder, int position) {
        final Articles a = articles.get(position);

        String imageUrl = a.getUrlToImage();

        Picasso.with(context).load(imageUrl).into(holder.imageView);

        holder.tvTitle.setText(a.getTitle());
        holder.tvSource.setText(a.getSource().getName());
        holder.tvDate.setText("\u2022"+dateTime(a.getPublishedAt()));

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Detailed.class);
                intent.putExtra("title",a.getTitle());
                intent.putExtra("source",a.getSource().getName());
                intent.putExtra("time",dateTime(a.getPublishedAt()));
                intent.putExtra("desc",a.getDescription());
                intent.putExtra("imageUrl",a.getUrlToImage());
                intent.putExtra("url",a.getUrl());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    static class GlobalViewHolder extends RecyclerView.ViewHolder{

        TextView tvTitle, tvSource, tvDate;
        KenBurnsView imageView;
        CardView cardView;

        GlobalViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvSource = itemView.findViewById(R.id.tvSource);
            tvDate = itemView.findViewById(R.id.tvDate);

            imageView = itemView.findViewById(R.id.image);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
    private String getCountry()
    {
        Locale locale = Locale.getDefault();
        String country = locale.getCountry();
        return country.toLowerCase();
    }

    private String dateTime(String t)
    {
        PrettyTime prettyTime = new PrettyTime(new Locale(getCountry()));
        String time = null;

        try
        {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:", Locale.ENGLISH);
            Date date = simpleDateFormat.parse(t);
            time = prettyTime.format(date);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        return time;
    }
}
