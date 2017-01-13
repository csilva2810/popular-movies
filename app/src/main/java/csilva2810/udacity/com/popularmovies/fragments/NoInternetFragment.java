package csilva2810.udacity.com.popularmovies.fragments;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import csilva2810.udacity.com.popularmovies.MainActivity;
import csilva2810.udacity.com.popularmovies.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoInternetFragment extends Fragment {

    private OnTryAgainClickListener mListener;

    public NoInternetFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_no_internet, container, false);

        Button btnTryAgain = (Button) view.findViewById(R.id.button_try_again);
        btnTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onTryAgainClickListener();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnTryAgainClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " Must Implement OnTryAgainClickListener");
        }
    }

    public interface OnTryAgainClickListener {
        public void onTryAgainClickListener();
    }
}
