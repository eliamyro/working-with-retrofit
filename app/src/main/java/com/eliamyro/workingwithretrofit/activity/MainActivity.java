package com.eliamyro.workingwithretrofit.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.eliamyro.workingwithretrofit.BuildConfig;
import com.eliamyro.workingwithretrofit.R;
import com.eliamyro.workingwithretrofit.adapter.MoviesAdapter;
import com.eliamyro.workingwithretrofit.model.Movie;
import com.eliamyro.workingwithretrofit.model.MovieResponse;
import com.eliamyro.workingwithretrofit.rest.ApiClient;
import com.eliamyro.workingwithretrofit.rest.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String API_KEY = BuildConfig.TMDB_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (API_KEY.isEmpty()){
            Toast.makeText(this, "Please obtain your API KEY first from themoviedb.org", Toast.LENGTH_SHORT).show();
            return;
        }

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.movies_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

                ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<MovieResponse> call = apiService.getTopRatedMovies(API_KEY);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
               if (response.body()!=null){
                   List<Movie> movies = response.body().getResult();
                   Log.d(TAG, "Number of movies received: " + movies.size());

                   recyclerView.setAdapter(new MoviesAdapter(movies, R.layout.list_item_view, getApplicationContext()));
               }

            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });


    }
}
