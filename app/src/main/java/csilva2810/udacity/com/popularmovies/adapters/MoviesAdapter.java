package csilva2810.udacity.com.popularmovies.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import csilva2810.udacity.com.popularmovies.models.Movie;
import csilva2810.udacity.com.popularmovies.MovieDetailsActivity;
import csilva2810.udacity.com.popularmovies.R;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    private List<Movie> mMovies;
    private Context mContext;
    private static final String LOG_TAG = MoviesAdapter.class.getSimpleName();

    public MoviesAdapter(Context context, List<Movie> movies) {
        this.mContext = context;
        this.mMovies = movies;
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        ImageView coverImageView;
        TextView titleTextView;

        public MovieViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.movie_card_view);
            coverImageView = (ImageView) itemView.findViewById(R.id.movie_cover);
            titleTextView = (TextView) itemView.findViewById(R.id.movie_title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), MovieDetailsActivity.class);
                    Movie movie = mMovies.get(getAdapterPosition());
                    intent.putExtra(Movie.EXTRA_MOVIE, movie);
                    view.getContext().startActivity(intent);
                }
            });
        }
    }

    @Override
    public MoviesAdapter.MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movies_grid, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MoviesAdapter.MovieViewHolder holder, int position) {

        Movie movie = mMovies.get(position);
        holder.titleTextView.setText(movie.getTitle());
        // "https://source.unsplash.com/category/nature/800x600"

        Picasso.with(holder.itemView.getContext())
            .load(movie.getPosterImage())
            .placeholder(R.drawable.placeholder_image)
            .into(holder.coverImageView);

    }

    @Override
    public int getItemCount() {
        try {
            return mMovies.size();
        } catch (NullPointerException e) {
            Log.d(LOG_TAG, "Get item count: " + e.getMessage());
        }
        return 0;
    }

}