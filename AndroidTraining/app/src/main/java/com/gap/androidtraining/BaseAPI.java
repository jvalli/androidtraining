package com.gap.androidtraining;

import java.io.Serializable;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Query;

/**
 * Created by jero on 7/27/16.
 */
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

    public class FoursquareSearch {

        private response response;

        public FoursquareSearch.response getResponse() {
            return response;
        }

        public class response {
            private List<Venue> venues;

            public List<FoursquareSearch.response.Venue> getVenues() {
                return venues;
            }

            public class Venue implements Serializable {
                public final String id;
                public final String name;
                public final String verified;
                public final String url;

                public Venue(String id, String name, String verified, String url) {
                    this.id = id;
                    this.name = name;
                    this.verified = verified;
                    this.url = url;
                }
            }
        }
    }

    public interface VenueInterface {
        @GET("venues/search")
        @Headers({"Content-Type: application/json;charset=UTF-8"})
        Call<List<FoursquareSearch.response.Venue>> searchVenues(@Query("client_id") String clientId, @Query("client_secret") String clientSecret, @Query("ll") String latitudeLongitude, @Query("v") String date, @Query("query") String query);
    }

    public VenueInterface getVenueInterface() {
        return venueInterface;
    }
}
