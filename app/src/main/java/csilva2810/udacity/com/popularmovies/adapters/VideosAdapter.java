package csilva2810.udacity.com.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import csilva2810.udacity.com.popularmovies.R;
import csilva2810.udacity.com.popularmovies.models.Video;

/**
 * Created by carlinhos on 1/15/17.
 */

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.VideosViewHolder> {

    private static final String LOG_TAG = VideosAdapter.class.getSimpleName();

    private Context mContext;
    private List<Video> mVideosList;

    public VideosAdapter(Context context, List<Video> videosList) {
        mContext = context;
        mVideosList = videosList;
    }

    class VideosViewHolder extends RecyclerView.ViewHolder {

        TextView videoName;

        VideosViewHolder(View itemView) {
            super(itemView);

            videoName = (TextView) itemView.findViewById(R.id.video_name_textview);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mContext, videoName.getText(), Toast.LENGTH_SHORT).show();
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

        Video video = mVideosList.get(position);
        holder.videoName.setText(video.getName());

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
