package csilva2810.udacity.com.popularmovies.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
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

import csilva2810.udacity.com.popularmovies.R;
import csilva2810.udacity.com.popularmovies.databinding.ItemMoviesBinding;
import csilva2810.udacity.com.popularmovies.models.Movie;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {

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
            Log.d(LOG_TAG, "Activity must implement OnMovieClickListener");
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

    class ViewHolder extends RecyclerView.ViewHolder implements OnMovieClickListener {

        private ItemMoviesBinding binding;

        ViewHolder(ItemMoviesBinding binding) {

            super(binding.getRoot());
            this.binding = binding;

        }

        void bind(final int position) {

            final Movie movie = mMovies.get(position);

            binding.setMovie(movie);
            binding.executePendingBindings();

            binding.movieCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onMovieClick(movie, position, binding.movieCover);
                }
            });

        }

        @Override
        public void onMovieClick(Object object, int position, View view) {
            mListener.onMovieClick(object, position, view);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ItemMoviesBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_movies_grid, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if (mMovies == null) return 0;
        return mMovies.size();
    }

}