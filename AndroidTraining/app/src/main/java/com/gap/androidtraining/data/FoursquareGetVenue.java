package com.gap.androidtraining.data;

import java.io.Serializable;

public class FoursquareGetVenue {

    private FoursquareGetVenueResponse response;

    public FoursquareGetVenueResponse getResponse() {
        return response;
    }

    public class FoursquareGetVenueResponse implements Serializable {
        private Venue venue;

        public Venue getVenue() {
            return venue;
        }
    }
}
