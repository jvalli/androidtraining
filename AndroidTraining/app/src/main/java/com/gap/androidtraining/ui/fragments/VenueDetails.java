package com.gap.androidtraining.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gap.androidtraining.R;
import com.gap.androidtraining.api.BaseAPI;
import com.gap.androidtraining.data.FoursquareGetVenue;
import com.gap.androidtraining.data.Venue;
import com.gap.androidtraining.ui.BaseFragment;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


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
            try {
                getVenue();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return view;
    }

    public void getVenue() throws IOException {
        showProgress(true);
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);
        String date = simpleDate.format(new Date());
        BaseAPI.VenueInterface venueInterface = BaseAPI.getInstance().getVenueInterface();
        final Call<FoursquareGetVenue> call = venueInterface.getVenue(mVenue.id, BaseAPI.FOURSQUARE_CLIENT_ID, BaseAPI.FOURSQUARE_CLIENT_SECRET, date);
        call.enqueue(new Callback<FoursquareGetVenue>() {
            @Override
            public void onResponse(Call<FoursquareGetVenue> call, Response<FoursquareGetVenue> response) {

                FoursquareGetVenue foursquareGetVenue = response.body();
                mVenue = foursquareGetVenue.getResponse().getVenue();
                showProgress(false);
            }

            @Override
            public void onFailure(Call<FoursquareGetVenue> call, Throwable t) {
                Log.d("error", "Error: " + call.request().body());
                showProgress(false);
            }
        });
    }
}
