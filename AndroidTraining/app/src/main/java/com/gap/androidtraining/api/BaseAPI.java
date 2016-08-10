package com.gap.androidtraining.api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Path;
import retrofit2.http.Query;

import com.gap.androidtraining.data.FoursquareGetVenue;
import  com.gap.androidtraining.data.FoursquareSearch;

public class BaseAPI {

    public static final String API_URL = "https://api.foursquare.com/v2/";
    public static final String FOURSQUARE_CLIENT_ID = "U12SZ2MFV0ATNLPVXEY43VJFOOUML0OST0XINT4K4WNXJEX4";
    public static final String FOURSQUARE_CLIENT_SECRET = "5IYQ0LRW5VOT2R1VWD5A00QWCIPELT1BPMJM5XORQ3WKUXSI";

    private static BaseAPI instance = new BaseAPI();
    private Retrofit retrofit;
    private VenueInterface venueInterface;

    public static BaseAPI getInstance() {
        return instance;
    }

    private BaseAPI() {

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(loggingInterceptor);
        OkHttpClient client = builder.build();

        retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        venueInterface = retrofit.create(VenueInterface.class);
    }

    public interface VenueInterface {
        @GET("venues/search")
        @Headers({"Content-Type: application/json;charset=UTF-8"})
        Call<FoursquareSearch> searchVenues(@Query("client_id") String clientId, @Query("client_secret") String clientSecret, @Query("ll") String latitudeLongitude, @Query("v") String date, @Query("query") String query);

        @GET("venues/{venue_id}")
        @Headers({"Content-Type: application/json;charset=UTF-8"})
        Call<FoursquareGetVenue> getVenue(@Path("venue_id") String venueId, @Query("client_id") String clientId, @Query("client_secret") String clientSecret, @Query("v") String date);
    }

    public VenueInterface getVenueInterface() {
        return venueInterface;
    }
}
