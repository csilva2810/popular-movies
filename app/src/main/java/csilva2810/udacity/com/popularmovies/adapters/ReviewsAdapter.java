package csilva2810.udacity.com.popularmovies.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import csilva2810.udacity.com.popularmovies.R;
import csilva2810.udacity.com.popularmovies.models.Review;

/**
 * Created by carlinhos on 1/16/17.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {

    private static String LOG_TAG = ReviewsAdapter.class.getSimpleName();
    private static final int NO_ITEMS_VIEW = 0;
    private static final int ITEM_VIEW = 1;

    private Context mContext;
    private List<Review> mReviewsList;

    public ReviewsAdapter(Context context, List<Review> reviews) {
        this.mContext = context;
        this.mReviewsList = reviews;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView authorIcon;
        TextView authorName;
        TextView content;
        Button readMoreButton;

        ViewHolder(View itemView) {
            super(itemView);

            authorIcon = (ImageView) itemView.findViewById(R.id.icon_author);
            authorName = (TextView) itemView.findViewById(R.id.review_author_name);
            content = (TextView) itemView.findViewById(R.id.review_content);
            readMoreButton = (Button) itemView.findViewById(R.id.review_read_more_button);

            readMoreButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri intentUri = Uri.parse( (String) view.getTag() );
                    mContext.startActivity(new Intent(Intent.ACTION_VIEW, intentUri));
                }
            });

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(
                LayoutInflater.from(mContext)
                .inflate(R.layout.reviews_list_item, parent, false)
        );

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (mReviewsList != null) {
            Review review = mReviewsList.get(position);

            holder.authorName.setText(review.getAuthorName());
            holder.content.setText(review.getResumedContent());
            holder.readMoreButton.setTag(review.getUrl());
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
