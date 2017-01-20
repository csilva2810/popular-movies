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
import csilva2810.udacity.com.popularmovies.adapters.ReviewsAdapter;
import csilva2810.udacity.com.popularmovies.constants.MoviesApi;
import csilva2810.udacity.com.popularmovies.listeners.OnFragmentInteractionListener;
import csilva2810.udacity.com.popularmovies.models.Review;
import csilva2810.udacity.com.popularmovies.services.ReviewTask;
import csilva2810.udacity.com.popularmovies.utils.AsyncTaskDelegate;

public class ReviewsFragment extends Fragment implements AsyncTaskDelegate {

    private long mMovieId;
    private OnFragmentInteractionListener mListener;

    private RecyclerView mReviewsRecyclerView;
    private ProgressBar mProgressBar;

    public ReviewsFragment() {
        // Required empty public constructor
    }

    public static ReviewsFragment newInstance(String param1, String param2) {
        ReviewsFragment fragment = new ReviewsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reviews, container, false);

        Bundle args = getArguments();
        if (args != null) {

            mMovieId = args.getLong(MovieDetailsActivity.ARG_MOVIEID);

            mReviewsRecyclerView = (RecyclerView) view.findViewById(R.id.reviews_recyclerview);
            mReviewsRecyclerView.setHasFixedSize(true);
            mReviewsRecyclerView.setLayoutManager(
                    new LinearLayoutManager(getActivity(),
                            LinearLayoutManager.VERTICAL, false)
            );

            mProgressBar = (ProgressBar) view.findViewById(R.id.spinner_progress);

            new ReviewTask(ReviewsFragment.this).execute(String.valueOf(mMovieId));

        }

        return view;
    }

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
        List<Review> reviews = (List<Review>) output;
        mReviewsRecyclerView.setAdapter(new ReviewsAdapter(getActivity(), reviews));
        mProgressBar.setVisibility(View.GONE);
    }

}
