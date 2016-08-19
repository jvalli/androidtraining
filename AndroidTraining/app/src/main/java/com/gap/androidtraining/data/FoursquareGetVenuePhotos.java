package com.gap.androidtraining.data;

import java.io.Serializable;
import java.util.List;

public class FoursquareGetVenuePhotos {

    private FoursquareGetVenuePhotosResponse response;

    public FoursquareGetVenuePhotosResponse getResponse() {
        return response;
    }

    public class FoursquareGetVenuePhotosResponse implements Serializable {
        private FoursquareGetVenuePhotosItemsResponse photos;

        public FoursquareGetVenuePhotosItemsResponse getPhotos() {
            return photos;
        }
    }

    public class FoursquareGetVenuePhotosItemsResponse implements Serializable {
        private List<VenuePhoto> items;

        public List<VenuePhoto> getItems() {
            return items;
        }
    }
}
