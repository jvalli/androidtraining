package com.gap.androidtraining.ui.fragments;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.gap.androidtraining.R;
import com.gap.androidtraining.api.BaseAPI;
import com.gap.androidtraining.data.FoursquareSearch;
import com.gap.androidtraining.data.Venue;
import com.gap.androidtraining.ui.BaseFragment;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class VenueSearch extends BaseFragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ListView.OnItemClickListener {

    private static final int PERMISSION_ACCESS_COARSE_LOCATION = 122;

    private GoogleApiClient mGoogleApiClient;
    private String mLocation = "40.7,-74";//null;
    private VenueAdapter mVenueAdapter;

    @BindView(R.id.edit_text_search)
    EditText mEditTextSearch;
    @BindView(R.id.list_view_venues)
    ListView mListViewVenues;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGoogleApiClient = new GoogleApiClient.Builder(this.getContext(), this, this).addApi(LocationServices.API).build();

        if (ContextCompat.checkSelfPermission(this.getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.getActivity(), new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_ACCESS_COARSE_LOCATION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_venue_search, container, false);
        ButterKnife.bind(this, view);

        if (ContextCompat.checkSelfPermission(this.getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mGoogleApiClient.connect();
        }

        mListViewVenues.setOnItemClickListener(this);

        return view;
    }

    @OnClick(R.id.button_search)
    public void onClickSearch(View view) {
        String text = mEditTextSearch.getText().toString();
        String textToast = String.format("%s '%s'", getString(R.string.searching), text);
        Toast.makeText(this.getContext(), textToast, Toast.LENGTH_SHORT).show();
        if (mLocation != null && mLocation.length() > 0) {
            try {
                searchVenues(mLocation, text);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mEditTextSearch.clearFocus();
        hideKeyboard(mEditTextSearch);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ACCESS_COARSE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mGoogleApiClient.connect();
                } else {
                    Toast.makeText(this.getContext(), getString(R.string.location_permission_error), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
        super.onDetach();
    }

    @Override
    public void onConnected(Bundle bundle) {

        if (ContextCompat.checkSelfPermission(this.getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            mLocation = String.format(Locale.ENGLISH, "%f,%f", lastLocation.getLatitude(), lastLocation.getLongitude());
            String textToast = String.format(Locale.ENGLISH, "%s: (%s)", getString(R.string.location), mLocation);
            Toast.makeText(this.getContext(), textToast, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this.getContext(), getString(R.string.google_api_client_suspended), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this.getContext(), getString(R.string.google_api_client_failed), Toast.LENGTH_SHORT).show();
    }

    public void searchVenues(String latitudeLongitude, String query) throws IOException {
        showProgress(true);
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);
        String date = simpleDate.format(new Date());
        BaseAPI.VenueInterface venueInterface = BaseAPI.getInstance().getVenueInterface();
        final Call<FoursquareSearch> call = venueInterface.searchVenues(BaseAPI.FOURSQUARE_CLIENT_ID, BaseAPI.FOURSQUARE_CLIENT_SECRET, latitudeLongitude, date, query);
        call.enqueue(new Callback<FoursquareSearch>() {
            @Override
            public void onResponse(Call<FoursquareSearch> call, Response<FoursquareSearch> response) {

                FoursquareSearch foursquareSearch = response.body();
                mVenueAdapter = new VenueAdapter(VenueSearch.this.getActivity(), foursquareSearch.getResponse().getVenues());
                if (mListViewVenues != null) {
                    mListViewVenues.setAdapter(mVenueAdapter);
                }
                showProgress(false);
            }

            @Override
            public void onFailure(Call<FoursquareSearch> call, Throwable t) {
                Log.d("error", "Error: " + call.request().body());
                showProgress(false);
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

        Venue venue = (Venue) mListViewVenues.getItemAtPosition(position);
        VenueDetails venueDetails = VenueDetails.newInstance(venue);
        replace(venueDetails);
    }
}
