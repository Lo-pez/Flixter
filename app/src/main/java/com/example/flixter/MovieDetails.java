package com.example.flixter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixter.databinding.ActivityMovieDetailsBinding;
import com.example.flixter.models.Movie;
import com.example.flixter.models.Video;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class MovieDetails extends AppCompatActivity {

    Movie movie;

    // the view objects
    TextView tvTitle;
    TextView tvOverview;
    ImageView tvPreview;
    ImageView tvLogo;
    RatingBar rbVoteAverage;

    public String NOW_PLAYING_URL;

    private ActivityMovieDetailsBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMovieDetailsBinding.inflate(getLayoutInflater());

        View view = binding.getRoot();
        setContentView(view);

        // resolve the view objects
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvOverview = (TextView) findViewById(R.id.tvOverview);
        tvPreview = (ImageView) findViewById(R.id.tvPreview);
        tvLogo = (ImageView) findViewById(R.id.tvLogo);
        rbVoteAverage = (RatingBar) findViewById(R.id.rbVoteAverage);

        // unwrap the movie passed in via intent, using its simple name as a key
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getTitle()));

        // set the title and overview
        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());

        // vote average is 0..10, convert to 0..5 by dividing by 2
        float voteAverage = movie.getVoteAverage().floatValue();
        rbVoteAverage.setRating(voteAverage / 2.0f);
        String imageUrl;
        int placeholderUrl;
        // if phone in landscape, set URL backdrop
        // else poster image
        placeholderUrl = R.drawable.flicks_backdrop_placeholder;
        imageUrl = movie.getBackdropPath();
        Glide.with(this)
                .load(imageUrl)
                .placeholder(placeholderUrl)
                .into(tvPreview);
        Glide.with(this)
                .load(R.drawable.yt3)
                .into(tvLogo);
    }

    public void intent(View view) {
        NOW_PLAYING_URL = String.format("https://api.themoviedb.org/3/movie/now_playing?api_key=%s" , getString(R.string.now_playing_api_key));

        final String YOUTUBE_URL = "https://api.themoviedb.org/3/movie/" + movie.getId() +"/videos?api_key=cfb00a4c2592c9b1add073e120f6f4c5&language=en-US";
        List<String> video = new ArrayList<>();
        String TAG = "intent";

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(YOUTUBE_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray results = jsonObject.getJSONArray("results");
                    JSONObject resultsJSONObject = results.getJSONObject(0);
                    video.add(resultsJSONObject.get("key").toString());


                } catch (JSONException e) {
                    Log.e(TAG, "Hit JSON exception", e);
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG, "onFailure");
            }
        });

    }
}