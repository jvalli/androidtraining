package com.gap.androidtraining.data;

import java.io.Serializable;
import java.util.List;

public class FoursquareSearch {

    private response response;

    public response getResponse() {
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
