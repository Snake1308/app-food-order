package com.example.appfoododer.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appfoododer.R;
import com.example.appfoododer.model.Restaurant;

import java.util.List;

public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListAdapter.MyViewHolder> {
    private List<Restaurant> restaurantList;
    private RestaurantListClickListener clickListener;

    public RestaurantListAdapter(List<Restaurant> restaurantModelList, RestaurantListClickListener clickListener) {
        this.restaurantList = restaurantModelList;
        this.clickListener = clickListener;
    }

    public void updateData(List<Restaurant> restaurantList) {
        this.restaurantList = restaurantList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RestaurantListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_row, parent, false);
        return  new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantListAdapter.MyViewHolder holder, int position) {
        holder.restaurantName.setText(restaurantList.get(position).getName());
        holder.restaurantAddress.setText("Address: "+restaurantList.get(position).getAddress());
        holder.restaurantHours.setText("Today's hours: " + restaurantList.get(position).getHours().getTodaysHours());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClick(restaurantList.get(position));
            }
        });
        Glide.with(holder.thumbImage)
                .load(restaurantList.get(position).getImage())
                .into(holder.thumbImage);

    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView restaurantName;
        TextView  restaurantAddress;
        TextView  restaurantHours;
        ImageView thumbImage;

        public MyViewHolder(View view) {
            super(view);
            restaurantName = view.findViewById(R.id.restaurantName);
            restaurantAddress = view.findViewById(R.id.restaurantAddress);
            restaurantHours = view.findViewById(R.id.restaurantHours);
            thumbImage = view.findViewById(R.id.thumbImage);

        }
    }

    public interface RestaurantListClickListener {
        public void onItemClick(Restaurant restaurant);
    }
}
