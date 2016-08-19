package com.gap.androidtraining.data;

import java.io.Serializable;
import java.util.Locale;

public class VenuePhoto implements Serializable {
    public final String id;
    public final int createdAt;
    public final String prefix;
    public final String suffix;
    public final int width;
    public final int height;

    public VenuePhoto(String id, int createdAt, String prefix, String suffix, int width, int height) {
        this.id = id;
        this.createdAt = createdAt;
        this.prefix = prefix;
        this.suffix = suffix;
        this.width = width;
        this.height = height;
    }

    public String getFullPhotoUrl() {
        return String.format(Locale.ENGLISH, "%s%dx%d%s", prefix, width, height, suffix);
    }
}
