package com.example.netflix.MainScreens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.netflix.Adapters.MainRecyclerAdapter;
import com.example.netflix.Model.AllCategory;
import com.example.netflix.R;
import com.example.netflix.Retrofit.RetrofitClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MainScreen extends AppCompatActivity {
    TextView movieText,tvSeriesText;
    MainRecyclerAdapter mainRecyclerAdapter;
    RecyclerView mainRecyclerView;
    List<AllCategory> allCategoryList;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        getSupportActionBar().hide();
        movieText=findViewById(R.id.moviestooltext);
        tvSeriesText=findViewById(R.id.tvseriestooltext);
        mainRecyclerView=findViewById(R.id.mainrecyclerview);
        movieText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainScreen.this,Movies.class));
            }
        });
        tvSeriesText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainScreen.this,TVSeries.class));
            }
        });
        BottomNavigationView bottomNavigationView;
        bottomNavigationView=findViewById(R.id.bottomnavigation);
        mainRecyclerView=findViewById(R.id.mainrecyclerview);
        Menu menu=bottomNavigationView.getMenu();
        MenuItem menuItem=menu.getItem(0);
        menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.homeicon:
                        break;
                    case R.id.searchicon:
                        startActivity(new Intent(MainScreen.this,Search.class));
                        break;
                    case R.id.settingsicon:
                        startActivity(new Intent(MainScreen.this,Settings.class));
                        break;
                }
                return false;
            }
        });
        ConnectivityManager connectivityManager=(ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        if(networkInfo==null||!networkInfo.isConnected()||!networkInfo.isAvailable())
        {
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setTitle("NO INTERNET");
            builder.setMessage("Please turn on internet connection to continue.");
            builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    recreate();
                }
            });
            AlertDialog alertDialog=builder.create();
            alertDialog.show();
            alertDialog.setCanceledOnTouchOutside(false);
        }
        else {
            allCategoryList = new ArrayList<>();
            getAllMovieData();
        }
    }
    public  void setMainRecycler(List<AllCategory> allCategoryList)
    {
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        mainRecyclerView.setLayoutManager(layoutManager);
        mainRecyclerAdapter=new MainRecyclerAdapter(this,allCategoryList);
        mainRecyclerView.setAdapter(mainRecyclerAdapter);
    }

    private void getAllMovieData() {
        CompositeDisposable compositeDisposable=new CompositeDisposable();
        compositeDisposable.add( RetrofitClient.getRetrofitClient().getAllCategoryMovies()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<AllCategory>>(){

                    @Override
                    public void onNext(List<AllCategory> allCategoryList) {
                        setMainRecycler(allCategoryList);

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                }));

    }
}