package csilva2810.udacity.com.popularmovies.adapters;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import csilva2810.udacity.com.popularmovies.R;
import csilva2810.udacity.com.popularmovies.databinding.ItemReviewsBinding;
import csilva2810.udacity.com.popularmovies.models.Review;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {

    private static String LOG_TAG = ReviewsAdapter.class.getSimpleName();

    private Context mContext;
    private List<Review> mReviewsList;

    public ReviewsAdapter(Context context, List<Review> reviews) {
        this.mContext = context;
        this.mReviewsList = reviews;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ItemReviewsBinding binding;

        public ViewHolder(ItemReviewsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(final int position) {
            final Review review = mReviewsList.get(position);

            binding.setReview(review);

            binding.reviewReadMoreButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri intentUri = Uri.parse( review.getUrl() );
                    mContext.startActivity(new Intent(Intent.ACTION_VIEW, intentUri));
                }
            });

            binding.executePendingBindings();

        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        ItemReviewsBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_reviews_list, parent, false);
        return new ViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (mReviewsList != null) {
            holder.bind(position);
        }

    }

    @Override
    public int getItemCount() {
        try {
            return mReviewsList.size();
        } catch (NullPointerException e) {
            Log.d(LOG_TAG, e.getMessage());
        }
        return 0;
    }

}
