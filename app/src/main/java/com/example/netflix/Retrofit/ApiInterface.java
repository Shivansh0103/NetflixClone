package com.example.netflix.Retrofit;

import static com.example.netflix.Retrofit.RetrofitClient.BASE_URL;

import com.example.netflix.Model.AllCategory;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface ApiInterface {
    @GET(BASE_URL)
    Observable<List<AllCategory>> getAllCategoryMovies();
}
