package com.example.netflix.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.netflix.Model.Comment;
import com.example.netflix.R;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private Context context;
    private List<Comment> comments;

    public CommentAdapter(Context context, List<Comment> comments) {
        this.context = context;
        this.comments = comments;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CommentAdapter.CommentViewHolder(LayoutInflater.from(context).inflate(R.layout.row_comment,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        holder.userName.setText(comments.get(position).getUserName());
        holder.userComment.setText(comments.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder
    {
        TextView userName,userComment;
        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            userComment=itemView.findViewById(R.id.userComment_CommentBox);
            userName=itemView.findViewById(R.id.userNameCommentBox);
        }
    }
}
