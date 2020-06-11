package com.codinginflow.retrofit_imdb;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    EditText edtText;
    Button btnSearch;
    ImageView imageView;
    TextView txtDetails;

    private RetrofitMovie retrofitMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtText = findViewById(R.id.edtText);
        btnSearch = findViewById(R.id.btnSearch);
        imageView = findViewById(R.id.imageView);
        txtDetails = findViewById(R.id.txtDetails);


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchMovie();
            }
        });
    }

    private void searchMovie(){
        String url = "http://www.omdbapi.com/";
        String fullUrl = "https://www.omdbapi.com/?apikey=30546dfa&t=" + edtText.getText();
        Log.d(TAG, "searchMovie: " + fullUrl);
        if(!TextUtils.isEmpty(edtText.getText())){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            retrofitMovie = retrofit.create(RetrofitMovie.class);
            Call<Movie> movieDetails = retrofitMovie.getDetails(fullUrl);

            movieDetails.enqueue(new Callback<Movie>() {
                @Override
                public void onResponse(Call<Movie> call, Response<Movie> response) {
                    if(response.isSuccessful()){
                        Movie details = response.body();
                        Glide.with(MainActivity.this)
                                .load(details.getPoster())
                                .into(imageView);

                        Log.d(TAG, "onResponse: " + details.toString());
                        String view = "";
                        view += "Title: " + details.getTitle() + "\n";
                        view += "Year: " + details.getYear() + "\n";
                        view += "Released Date: " + details.getReleaseDate() + "\n";
                        view += "Genre: " + details.getGenre() + "\n";
                        view += "Director: " + details.getDirector() + "\n";
                        view += "Actors: " + details.getActors() + "\n";
                        view += "imdbRating: " + details.getImdbRating() + "\n";
                        view += "Plot: " + details.getPlot() + "\n";
                        view += "Language: " + details.getLanguage() + "\n";
                        view += "Country: " + details.getCountry() + "\n";
                        txtDetails.setText(view);
                        Log.d(TAG, "onResponse: " + view);

                    }else{
                        txtDetails.setText("Something went wrong" + "\n" +
                                response.code());
                    }
                }

                @Override
                public void onFailure(Call<Movie> call, Throwable t) {
                    txtDetails.setText(t.getMessage());
                }
            });

            makeToast("Searching...");
        }else{
            makeToast("Please type a valid name!");
        }

//        String baseUrl = "https://jsonplaceholder.typicode.com/";
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(baseUrl)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        RetrofitMovie post = retrofit.create(RetrofitMovie.class);
//        Call<Post> item = post.getPost("https://jsonplaceholder.typicode.com/posts/10");
//        item.enqueue(new Callback<Post>() {
//            @Override
//            public void onResponse(Call<Post> call, Response<Post> response) {
//                if(response.isSuccessful()){
//                    Post abc = response.body();
//                    Log.d(TAG, "onResponse: " + abc.toString());
//                    txtDetails.setText(abc.getId() + " " + "\n" +
//                            abc.getBody());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Post> call, Throwable t) {
//
//            }
//        });
    }

    private void makeToast(String s){
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}