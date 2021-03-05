package com.codepath.fkhan.flixapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.fkhan.flixapp.DetailActivity;
import com.codepath.fkhan.flixapp.R;
import com.codepath.fkhan.flixapp.models.Movie;

import org.parceler.Parcels;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    Context context;
    List<Movie> movies;

    public MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    //usually involves inflating a layout from XML & returning the holder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("MovieAdapter", "onCreateViewHolder");
        View movieView = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(movieView);
    }

    //involved populating data into the item though holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("MovieAdapter", "onBindViewHolder" + position);
        //get the movie at the position
        Movie movie = movies.get(position);
        //bind the movie data into view holder
        holder.bind(movie);
    }

    //returns the total count of items in the list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //view holder is representation of item_movie recycler row
        RelativeLayout container;
        TextView tvTitle;
        TextView tvOverview;
        ImageView ivPoster;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            container = itemView.findViewById(R.id.container);
        }

        public void bind(Movie movie) {
            tvTitle.setText(movie.getTitle());
            tvOverview.setText(movie.getOverview());
            Glide.with(context).load(movie.getPosterPath()).into(ivPoster);

            /**
             * created DetailActivity which came with xml file to which we rendered as empty screen
             * then we set RelativeLayout to container ID to call it so we can add a toast (commented)
             * then we used intents to set up switching screens (calling context and detailActivity switched
             * from movie display to empty screen (As expected)
             */
            //1. Register click listeners on whole row by calling Relative Layout by its ID
            container.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   //2. Navigate to a new activity on tap
                   //Toast.makeText(context, movie.getTitle(), Toast.LENGTH_SHORT).show(); //check to see if it works

                   Intent i = new Intent(context, DetailActivity.class); //create intent to switch screen

                   //trying to place movie into putExtra by putting it into Parcelable value (bc method can't take movie object)
                   //we use Parcelable (go to AndroidStudio ref. where they have the code to allow u to use Parceler library
                   //essentially, you place the implementation code into buildgradle file
                   i.putExtra("movie", Parcels.wrap(movie));
                   //add @Parcel to Movie class and add empty constructor as shown in AU (android U) page + empty constructor
                   //now you can go to DetailActivity to retrieve the whole movie object just by using an intent

                   context.startActivity(i);

               }
           });

        }

    }
}
