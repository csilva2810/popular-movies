package csilva2810.udacity.com.popularmovies.adapters;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import csilva2810.udacity.com.popularmovies.R;
import csilva2810.udacity.com.popularmovies.models.Video;

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.VideosViewHolder> {

    private static final String LOG_TAG = VideosAdapter.class.getSimpleName();

    private Context mContext;
    private List<Video> mVideosList;

    public VideosAdapter(Context context, List<Video> videosList) {
        mContext = context;
        mVideosList = videosList;
    }

    class VideosViewHolder extends RecyclerView.ViewHolder {

        String videoKey;
        ImageView videoThumbnail;
        TextView videoName;

        VideosViewHolder(View itemView) {
            super(itemView);

            videoThumbnail = (ImageView) itemView.findViewById(R.id.video_image_thumb);
            videoName = (TextView) itemView.findViewById(R.id.video_name_textview);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent youtubeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + videoKey));
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://www.youtube.com/watch?v=" + videoKey));
                    try {
                        mContext.startActivity(youtubeIntent);
                    } catch (ActivityNotFoundException ex) {
                        mContext.startActivity(browserIntent);
                    }
                }
            });

        }

    }

    public VideosAdapter.VideosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =
                LayoutInflater.from(mContext).inflate(R.layout.videos_list_item, parent, false);
        return new VideosAdapter.VideosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VideosAdapter.VideosViewHolder holder, int position) {

        if (mVideosList != null) {
            Video video = mVideosList.get(position);
            holder.videoName.setText(video.getName());
            holder.videoKey = video.getKey();

            String thumbUrl = "https://img.youtube.com/vi/" + holder.videoKey + "/maxresdefault.jpg";
            Picasso.with(mContext).load(thumbUrl).into(holder.videoThumbnail);
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
