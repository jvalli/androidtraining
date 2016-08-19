package com.gap.androidtraining.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.gap.androidtraining.R;
import com.gap.androidtraining.api.BaseAPI;
import com.gap.androidtraining.data.FoursquareGetVenue;
import com.gap.androidtraining.data.FoursquareGetVenuePhotos;
import com.gap.androidtraining.data.Venue;
import com.gap.androidtraining.ui.BaseFragment;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class VenueDetails extends BaseFragment {

    private static final String ARG_VENUE = "venue";

    private Venue mVenue = null;
    private ImageAdapter mImageAdapter;

    @BindView(R.id.text_view_name)
    TextView mTextViewName;

    @BindView(R.id.grid_view_gallery)
    GridView mGridViewGallery;

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
                getVenuePhotos();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return view;
    }

    public void getVenuePhotos() throws IOException {
        showProgress(true);
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);
        String date = simpleDate.format(new Date());
        BaseAPI.VenueInterface venueInterface = BaseAPI.getInstance().getVenueInterface();
        final Call<FoursquareGetVenuePhotos> call = venueInterface.getVenuePhotos(mVenue.id, BaseAPI.FOURSQUARE_CLIENT_ID, BaseAPI.FOURSQUARE_CLIENT_SECRET, date);
        call.enqueue(new Callback<FoursquareGetVenuePhotos>() {
            @Override
            public void onResponse(Call<FoursquareGetVenuePhotos> call, Response<FoursquareGetVenuePhotos> response) {

                FoursquareGetVenuePhotos foursquareGetVenuePhotos = response.body();
                mImageAdapter = new ImageAdapter(VenueDetails.this.getActivity(), foursquareGetVenuePhotos.getResponse().getPhotos().getItems());
                if (mGridViewGallery != null) {
                    mGridViewGallery.setAdapter(mImageAdapter);
                }
                showProgress(false);
            }

            @Override
            public void onFailure(Call<FoursquareGetVenuePhotos> call, Throwable t) {
                Log.d("error", "Error: " + call.request().body());
                showProgress(false);
            }
        });
    }
}
