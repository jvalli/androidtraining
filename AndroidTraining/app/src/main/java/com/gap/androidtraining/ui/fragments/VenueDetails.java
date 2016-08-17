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
                // TODO: parse images from venue response
                List<String> testData = new ArrayList<String>();
                testData.add("https://upload.wikimedia.org/wikipedia/commons/c/c0/Gingerbread_House_Essex_CT.jpg");
                testData.add("https://upload.wikimedia.org/wikipedia/commons/f/f8/Ellen_H._Swallow_Richards_House_Boston_MA_01.jpg");
                testData.add("http://resources.phrasemix.com/img/full/suburban-houses.jpg");
                testData.add("http://hookedonhouses.net/wp-content/uploads/2009/01/Father-of-the-Bride-Lookalike-house.jpg");
                testData.add("https://upload.wikimedia.org/wikipedia/commons/c/c9/Ranch_style_home_in_Salinas,_California.JPG");
                testData.add("https://s-media-cache-ak0.pinimg.com/236x/90/4a/0e/904a0e6ab72e18756e5b186da3d5147b.jpg");
                testData.add("https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcSBqYSOdVZ04yElTOrVhvS6k9QAju846kpJp6RKm-9pdDevrnOc");
                testData.add("http://www.comohotels.com/parrotcay/sites/default/files/styles/440x138/public/images/nonlinking/parrotcay_two_bedroom_beach_house_exterior.jpg?itok=PwZrfdDN");
                testData.add("https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcTEavCht10H_zx13RujOoRo2_y6t5JBa0Yg57VXZwCSBA2wvn8P");
                mImageAdapter = new ImageAdapter(VenueDetails.this.getActivity(), testData);
                if (mGridViewGallery != null) {
                    mGridViewGallery.setAdapter(mImageAdapter);
                }
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
