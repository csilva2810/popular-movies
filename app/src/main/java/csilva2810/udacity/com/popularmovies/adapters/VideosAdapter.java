package csilva2810.udacity.com.popularmovies.adapters;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import csilva2810.udacity.com.popularmovies.R;
import csilva2810.udacity.com.popularmovies.databinding.ItemVideosBinding;
import csilva2810.udacity.com.popularmovies.models.Video;

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.ViewHolder> {

    private static final String LOG_TAG = VideosAdapter.class.getSimpleName();

    private Context mContext;
    private List<Video> mVideosList;

    public VideosAdapter(Context context, List<Video> videosList) {
        mContext = context;
        mVideosList = videosList;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ItemVideosBinding binding;

        public ViewHolder(ItemVideosBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(final int position) {
            final Video video = mVideosList.get(position);

            binding.setVideo(video);

            binding.videoCardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent youtubeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + video.getKey()));
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(video.getYoutubeUrl()));
                    try {
                        mContext.startActivity(youtubeIntent);
                    } catch (ActivityNotFoundException ex) {
                        mContext.startActivity(browserIntent);
                    }
                }
            });

            binding.videoShareIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_TEXT, video.getYoutubeUrl());
                    shareIntent.setType("text/plain");
                    mContext.startActivity(shareIntent);
                }
            });

            binding.executePendingBindings();
        }

    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ItemVideosBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_videos_list, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (position == 0) {
            holder.itemView.setPadding(16, 0, 0, 0);
        }

        if (mVideosList != null) {
            holder.bind(position);
        }

    }

    @Override
    public int getItemCount() {
        try {
            return mVideosList.size();
        } catch (NullPointerException e) {
            Log.d(LOG_TAG, e.getMessage());
        }
        return 0;
    }

}
