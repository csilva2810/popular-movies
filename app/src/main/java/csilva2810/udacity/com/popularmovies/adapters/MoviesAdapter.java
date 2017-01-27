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
import java.util.Set;

import csilva2810.udacity.com.popularmovies.models.Movie;
import csilva2810.udacity.com.popularmovies.MovieDetailsActivity;
import csilva2810.udacity.com.popularmovies.R;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    private List<Movie> mMovies;
    private Context mContext;
    private OnMovieClickListener mListener;
    private static final String LOG_TAG = MoviesAdapter.class.getSimpleName();

    public interface OnMovieClickListener {
        void onMovieClick(Object object, int position, View itemView);
    }

    public MoviesAdapter(Context context, List<Movie> movies, OnMovieClickListener listener) {
        mContext = context;
        mMovies = movies;
        try {
            mListener = listener;
        } catch (ClassCastException e) {
            Log.d(LOG_TAG, "Activity must implement OnMovieClickListener()");
        }
        setFavoriteMovies();
    }

    private void setFavoriteMovies() {
        Set<Long> favorites = Movie.getFavoritesId(mContext);

        if (favorites != null) {
            Log.d(LOG_TAG, "Favorites: " + favorites);
            for (Movie movie : mMovies) {
                movie.setFavorite(favorites.contains(movie.getId()));
            }
        }
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements OnMovieClickListener {

        CardView cv;
        ImageView coverImageView, favoriteIcon;
        TextView titleTextView;

        MovieViewHolder(View itemView) {

            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.movie_card_view);
            coverImageView = (ImageView) itemView.findViewById(R.id.movie_cover);
            favoriteIcon = (ImageView) itemView.findViewById(R.id.favorite_icon);
            titleTextView = (TextView) itemView.findViewById(R.id.movie_title);

        }

        void bind(final int position) {
            final Movie movie = mMovies.get(position);

            if (movie.isFavorite()) {
                favoriteIcon.setVisibility(View.VISIBLE);
            }

            titleTextView.setText(movie.getTitle());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onMovieClick(movie, position, coverImageView);
                }
            });

            Picasso.with(mContext)
                    .load(movie.getPosterImage())
                    .placeholder(R.drawable.placeholder_video)
                    .into(coverImageView);

        }

        @Override
        public void onMovieClick(Object object, int position, View view) {
            mListener.onMovieClick(object, position, view);
        }
    }

    @Override
    public MoviesAdapter.MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_movies_grid, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MoviesAdapter.MovieViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if (mMovies == null) return 0;
        return mMovies.size();
    }

    @Override
    public void onViewRecycled(MovieViewHolder holder) {
        // Unsing this method to set favoriteIcon's Visibility to GONE
        // since this holder will be used to another movie which may not be a favorite
        holder.favoriteIcon.setVisibility(View.GONE);
        super.onViewRecycled(holder);
    }
}