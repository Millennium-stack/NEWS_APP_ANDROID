package com.newsglobal.android.newsapp;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.flaviofaria.kenburnsview.KenBurnsView;

import java.util.List;

public class HomeItemsAdapter extends RecyclerView.Adapter<HomeItemsAdapter.HomeItemViewHolder>{

    private List<HomeItems> homeItems;

    private Home context;
    private ViewPager2 viewPager2;

    HomeItemsAdapter(Home context, List<HomeItems> homeItems, ViewPager2 viewPager2)
    {
        this.context = context;
        this.homeItems = homeItems;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public HomeItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HomeItemViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_container_location,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull HomeItemViewHolder holder, final int position) {
        holder.setLocationData(homeItems.get(position));

        if (position == homeItems.size() - 2)
        {
            viewPager2.post(runnable);
        }

        holder.kbvLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;

                switch (homeItems.get(position).getImageId())
                {
                    case R.drawable.global_news:
                        intent = new Intent(context.getActivity(), GlobalNews.class);
                        context.startActivity(intent);
                        break;

                    case R.drawable.india_news:
                        intent = new Intent(context.getActivity(), IndianNews.class);
                        context.startActivity(intent);
                        break;

                    case R.drawable.business_news:
                        intent = new Intent(context.getActivity(), BusinessNews.class);
                        context.startActivity(intent);
                        break;

                    case R.drawable.health_news:
                        intent = new Intent(context.getActivity(), HealthNews.class);
                        context.startActivity(intent);
                        break;

                    case R.drawable.science_news:
                        intent = new Intent(context.getActivity(), ScienceNews.class);
                        context.startActivity(intent);
                        break;

                    case R.drawable.technology_news:
                        intent = new Intent(context.getActivity(), TechnologyNews.class);
                        context.startActivity(intent);
                        break;

                    case R.drawable.entertainment_news:
                        intent = new Intent(context.getActivity(), EntertainmentNews.class);
                        context.startActivity(intent);
                        break;

                    case R.drawable.sports_news:
                        intent = new Intent(context.getActivity(), SportsNews.class);
                        context.startActivity(intent);
                        break;

                    default:
                        break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return homeItems.size();
    }

    static class HomeItemViewHolder extends RecyclerView.ViewHolder
    {
        private KenBurnsView kbvLocation;
        private TextView textTitle;

        HomeItemViewHolder(@NonNull View itemView)
        {
            super(itemView);
            kbvLocation = itemView.findViewById(R.id.kbvLocation);
            textTitle = itemView.findViewById(R.id.textTitle);
        }

        void setLocationData(HomeItems homeItems)
        {
            kbvLocation.setImageResource(homeItems.getImageId());
            textTitle.setText(homeItems.title);
        }
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

            List<HomeItems> itemLoc = homeItems;

            homeItems.addAll(itemLoc);
            notifyDataSetChanged();
        }
    };
}