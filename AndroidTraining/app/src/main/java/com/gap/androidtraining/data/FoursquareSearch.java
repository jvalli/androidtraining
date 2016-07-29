package com.gap.androidtraining.data;

import java.io.Serializable;
import java.util.List;

public class FoursquareSearch implements Serializable {

    private FoursquareSearchResponse response;

    public FoursquareSearchResponse getResponse() {
        return response;
    }

    public class FoursquareSearchResponse implements Serializable {
        private List<Venue> venues;

        public List<Venue> getVenues() {
            return venues;
        }
    }
}
