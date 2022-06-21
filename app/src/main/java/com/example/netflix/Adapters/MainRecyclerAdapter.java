package com.example.netflix.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.netflix.MainScreens.MovieDetails;
import com.example.netflix.Model.AllCategory;
import com.example.netflix.Model.CategoryItemList;
import com.example.netflix.R;

import java.util.List;

public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.MainViewHolder > {
    Context context;
    List<AllCategory> allCategoryList;

    public MainRecyclerAdapter(Context context, List<AllCategory> allCategoryList) {
        this.context = context;
        this.allCategoryList = allCategoryList;
    }

    @NonNull
    @Override
    public MainRecyclerAdapter.MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MainRecyclerAdapter.MainViewHolder(LayoutInflater.from(context).inflate(R.layout.main_recycler_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MainRecyclerAdapter.MainViewHolder holder, int position) {
        holder.itemCategory.setText(allCategoryList.get(position).getCategoryTitle());
        setItemRecycler(holder.itemRecycler, allCategoryList.get(position).getCategoryItemList());

    }

    @Override
    public int getItemCount() {
        return allCategoryList.size();
    }

    public  static final class MainViewHolder extends RecyclerView.ViewHolder
    {
        TextView itemCategory;
        RecyclerView itemRecycler;
        public MainViewHolder(@NonNull View itemView)
        {
            super(itemView);
            itemCategory=itemView.findViewById(R.id.itemCategory);
            itemRecycler=itemView.findViewById(R.id.itemRecycler);
        }
    }
    public void setItemRecycler(RecyclerView recycler,List<CategoryItemList> categoryItemList)
    {
        ItemRecyclerAdapter itemRecyclerAdapter=new ItemRecyclerAdapter(context,categoryItemList);
        recycler.setLayoutManager(new LinearLayoutManager(context,RecyclerView.HORIZONTAL,false));
        recycler.setAdapter(itemRecyclerAdapter);
    }
}
