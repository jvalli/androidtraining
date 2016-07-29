package com.gap.androidtraining.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gap.androidtraining.R;
import com.gap.androidtraining.data.Venue;
import com.gap.androidtraining.ui.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;


public class VenueDetails extends BaseFragment {

    private static final String ARG_VENUE = "venue";

    private Venue mVenue = null;

    @BindView(R.id.text_view_name)
    TextView mTextViewName;

    public static VenueDetails newInstance(Venue venue) {
        VenueDetails fragment = new VenueDetails();
        Bundle args = new Bundle();
        args.putSerializable(ARG_VENUE, venue);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mVenue = (Venue) getArguments().getSerializable(ARG_VENUE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_venue_details, container, false);
        ButterKnife.bind(this, view);

        if (mVenue != null) {
            mTextViewName.setText(mVenue.name);
        }

        return view;
    }
}
