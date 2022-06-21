package com.example.netflix.MainScreens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.netflix.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MovieDetails extends AppCompatActivity {
    ImageView movieImage;
    TextView movieName;
    Button play;
    String name,image,fileUrl,movieId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        getSupportActionBar().hide();
        movieImage=findViewById(R.id.imageDetails);
        movieName=findViewById(R.id.movieName);
        play=findViewById(R.id.playButton);
        movieId=getIntent().getStringExtra("MovieId");
        name=getIntent().getStringExtra("MovieName");
        image=getIntent().getStringExtra("MovieImageURL");
        fileUrl=getIntent().getStringExtra("MovieFile");
        Glide.with(this).load(image).into(movieImage);
        movieName.setText(name);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MovieDetails.this,VideoPlayer.class);
                i.putExtra("FileURL",fileUrl);
                startActivity(i);
            }
        });
    }
}