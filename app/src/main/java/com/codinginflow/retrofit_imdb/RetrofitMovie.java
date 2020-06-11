package com.codinginflow.retrofit_imdb;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface RetrofitMovie {
    @GET
    Call<Movie> getDetails(@Url String url);

    @GET
    Call<Post> getPost(@Url String url);
}
