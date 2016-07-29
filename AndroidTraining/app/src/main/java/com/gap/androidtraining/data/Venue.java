package com.gap.androidtraining.data;

import java.io.Serializable;

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
