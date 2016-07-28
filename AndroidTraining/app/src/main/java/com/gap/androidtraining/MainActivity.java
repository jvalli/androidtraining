package com.gap.androidtraining;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
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

import com.gap.androidtraining.BaseAPI.FoursquareSearch;
import com.gap.androidtraining.BaseAPI.VenueInterface;

public class MainActivity extends AppCompatActivity implements ConnectionCallbacks, OnConnectionFailedListener {

    private static final int PERMISSION_ACCESS_COARSE_LOCATION = 122;

    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;
    private String mLocation = "40.7,-74";//null;
    private VenueAdapter mVenueAdapter;

    @BindView(R.id.edit_text_search)
    EditText mEditTextSearch;
    @BindView(R.id.list_view_venues)
    ListView mListViewVenues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle(getString(R.string.loading));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(true);

        mGoogleApiClient = new GoogleApiClient.Builder(this, this, this).addApi(LocationServices.API).build();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_ACCESS_COARSE_LOCATION);
        }
    }

    @OnClick(R.id.button_search)
    public void onClickSearch(View view) {
        String text = mEditTextSearch.getText().toString();
        String textToast = String.format("%s '%s'", getString(R.string.searching), text);
        Toast.makeText(getApplicationContext(), textToast, Toast.LENGTH_SHORT).show();
        if (mLocation != null && mLocation.length() > 0) {
            try {
                searchVenues(mLocation, text);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mEditTextSearch.clearFocus();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ACCESS_COARSE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mGoogleApiClient.connect();
                } else {
                    Toast.makeText(this, getString(R.string.location_permission_error), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    public void onConnected(Bundle bundle) {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            mLocation = String.format(Locale.ENGLISH, "%f,%f", lastLocation.getLatitude(), lastLocation.getLongitude());
            String textToast = String.format(Locale.ENGLISH, "%s: (%s)", getString(R.string.location), mLocation);
            Toast.makeText(this, textToast, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, getString(R.string.google_api_client_suspended), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, getString(R.string.google_api_client_failed), Toast.LENGTH_SHORT).show();
    }

    public void searchVenues(String latitudeLongitude, String query) throws IOException {
        mProgressDialog.show();
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);
        String date = simpleDate.format(new Date());
        VenueInterface venueInterface = BaseAPI.getInstance().getVenueInterface();
        final Call<FoursquareSearch> call = venueInterface.searchVenues(BaseAPI.FOURSQUARE_CLIENT_ID, BaseAPI.FOURSQUARE_CLIENT_SECRET, latitudeLongitude, date, query);
        call.enqueue(new Callback<FoursquareSearch>() {
            @Override
            public void onResponse(Call<FoursquareSearch> call, Response<FoursquareSearch> response) {

                mProgressDialog.hide();
                FoursquareSearch foursquareSearch = response.body();
                mVenueAdapter = new VenueAdapter(MainActivity.this, foursquareSearch.getResponse().getVenues());
                if (mListViewVenues != null) {
                    mListViewVenues.setAdapter(mVenueAdapter);
                }
            }

            @Override
            public void onFailure(Call<FoursquareSearch> call, Throwable t) {
                Log.d("error", "Error: " + call.request().body());
                mProgressDialog.hide();
            }
        });
    }
}
