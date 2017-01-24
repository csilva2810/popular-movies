package csilva2810.udacity.com.popularmovies.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.List;

import csilva2810.udacity.com.popularmovies.MovieDetailsActivity;
import csilva2810.udacity.com.popularmovies.R;
import csilva2810.udacity.com.popularmovies.adapters.VideosAdapter;
import csilva2810.udacity.com.popularmovies.listeners.OnFragmentInteractionListener;
import csilva2810.udacity.com.popularmovies.models.Video;
import csilva2810.udacity.com.popularmovies.services.VideoTask;
import csilva2810.udacity.com.popularmovies.utils.AsyncTaskDelegate;

public class VideosFragment extends Fragment implements AsyncTaskDelegate {

    private long mMovieId;
    private OnFragmentInteractionListener mListener;

    private RecyclerView mVideosRecyclerView;
    private ProgressBar mProgressBar;

    public VideosFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_videos, container, false);

        Bundle args = getArguments();
        if (args != null) {

            mMovieId = args.getLong(MovieDetailsActivity.ARG_MOVIEID);

            mVideosRecyclerView = (RecyclerView) view.findViewById(R.id.videos_recyclerview);
            mVideosRecyclerView.setHasFixedSize(true);
            mVideosRecyclerView.setLayoutManager(
                    new LinearLayoutManager(getActivity(),
                            LinearLayoutManager.HORIZONTAL, false)
            );

            mProgressBar = (ProgressBar) view.findViewById(R.id.spinner_progress);

            new VideoTask(VideosFragment.this).execute(String.valueOf(mMovieId));

        }

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onProcessFinish(Object output, String taskType) {
        if (output != null) {
            List<Video> videos = (List<Video>) output;
            mVideosRecyclerView.setAdapter(new VideosAdapter(getActivity(), videos));
            mProgressBar.setVisibility(View.GONE);
        }
    }
}
