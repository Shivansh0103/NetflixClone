package com.example.netflix.MainScreens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import com.example.netflix.Adapters.MainRecyclerAdapter;
import com.example.netflix.Adapters.SearchRecyclerAdapter;
import com.example.netflix.Model.AllCategory;
import com.example.netflix.R;
import com.example.netflix.Retrofit.RetrofitClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class Search extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    SearchRecyclerAdapter mainRecyclerAdapter;
    RecyclerView mainRecyclerView;
    List<AllCategory> allCategoryList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().setTitle("Search");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.DKGRAY));
        bottomNavigationView=findViewById(R.id.bottomnavigation);
        mainRecyclerView=findViewById(R.id.searchrecyclerview);
        Menu menu=bottomNavigationView.getMenu();
        MenuItem menuItem=menu.getItem(1);
        menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                        case R.id.homeicon:
                            startActivity(new Intent(Search.this,MainScreen.class));
                        break;
                        case R.id.settingsicon:
                            startActivity(new Intent(Search.this,Settings.class));
                        break;
                        case R.id.searchicon:
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
        mainRecyclerAdapter=new SearchRecyclerAdapter(this,allCategoryList);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.search_menu,menu);
        MenuItem searchItem=menu.findItem(R.id.searchview);
        SearchView searchView=(SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                mainRecyclerAdapter.getFilter().filter(s);
                return false;
            }
        });
        return  true;
    }
}