package com.example.netflix.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.netflix.MainScreens.MovieDetails;
import com.example.netflix.Model.CategoryItemList;
import com.example.netflix.R;

import java.util.ArrayList;
import java.util.List;

public class ItemRecyclerAdapter extends RecyclerView.Adapter<ItemRecyclerAdapter.ItemViewHolder > {
    Context context;
    List<CategoryItemList> categoryItemList;
    List<CategoryItemList> moviesListAll;

    public ItemRecyclerAdapter(Context context, List<CategoryItemList> categoryItemList) {
        this.context = context;
        this.categoryItemList = categoryItemList;
        this.moviesListAll = new ArrayList<>(categoryItemList);
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(context).inflate(R.layout.category_row_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Glide.with(context).load(categoryItemList.get(position).getImageUrl()).into(holder.itemImage);
        holder.itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, MovieDetails.class);
                intent.putExtra("MovieId",categoryItemList.get(position).getId());
                intent.putExtra("MovieName",categoryItemList.get(position).getMovieName());
                intent.putExtra("MovieImageURL",categoryItemList.get(position).getImageUrl());
                intent.putExtra("MovieFile",categoryItemList.get(position).getFileUrl());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryItemList.size();
    }

    public  static final class ItemViewHolder extends RecyclerView.ViewHolder
    {
        ImageView itemImage;
        public ItemViewHolder(@NonNull View itemView)
        {
            super(itemView);
            itemImage=itemView.findViewById(R.id.itemimage);
        }
    }
}
