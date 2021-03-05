package com.codepath.fkhan.flixapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.codepath.fkhan.flixapp.models.Movie;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

import okhttp3.Headers;

public class DetailActivity extends YouTubeBaseActivity {

    //create a variable for youtube API key from GoogleAPI library
    private static final String YOUTUBE_API_KEY = "AIzaSyBvulnNqyUm4p4MHVBBn7bQ6CYmiJiCeCQ";
    //add correct url to play movie trailers
    public static final String VIDEOS_URL = "https://api.themoviedb.org/3/movie/%d/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

    //declare icon names from Activity Detail xml and initialize them under onCreate method
    TextView tvTitle;
    TextView tvOverview;
    RatingBar ratingBar;
    YouTubePlayerView youTubePlayerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //initialize the fields by calling findViewHolder and calling it by R.Id._
        tvTitle = findViewById(R.id.tvTitle);
        tvOverview = findViewById(R.id.tvOverview);
        ratingBar = findViewById(R.id.ratingBar);
        youTubePlayerView = findViewById(R.id.player);


        //Instead of calling intent to just change title, we want Intent to take in movie object so it's easier.
        // Go back to MovieAdapter to change i.putExtra

        //post-implementation of Parcel in MovieAdapter and now call (must unwrap) Parcel and intent w movie object
        Movie movie = Parcels.unwrap(getIntent().getParcelableExtra("movie"));
        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());
        //define rating in Movie class by declaring field, initializing it to corresponding API name, and set up a getter
        //had to downcast rating to float bc setRating method takes in float only
        ratingBar.setRating((float) movie.getRating());


        AsyncHttpClient client = new AsyncHttpClient(); //create client object to use Async
        client.get(String.format(VIDEOS_URL, movie.getMovieId()), new JsonHttpResponseHandler() { //getMovieId calls for corresponding movie id
                    @Override
                    public void onSuccess(int i, Headers headers, JSON json) {
                        try {
                            JSONArray results = json.jsonObject.getJSONArray("results");
                            if (results.length() == 0){
                                return;
                            }
                            String youtubeKey = results.getJSONObject(0).getString("key"); //get first object (one movie out of entire movie list)
                            Log.d("DetailActivity", youtubeKey); //parsing out youtubeKey
                            initializeYoutube(youtubeKey);
                        } catch (JSONException e) {
                            Log.e("DetailActivity", "Failed to parse JSON", e);
                        }
                    }
                    @Override
                    public void onFailure(int i, Headers headers, String s, Throwable throwable) {

                    }
                });

    }

    private void initializeYoutube(String youtubeKey) {
        //stream youtube videos onto our app.
        //initialize the field above with a method
        youTubePlayerView.initialize(YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                //log for debugging
                Log.d("DetailActivity", "onInitializationSuccess");
                // cue video, play video, etc.
                youTubePlayer.cueVideo(youtubeKey);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Log.d("DetailActivity", "onInitializationFailure");
            }
        });
    }
}