package com.example.flixter.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Video {

    String id;

    public Video() {}

    public Video(String youtubeId) {
        id = youtubeId;
    }

    public String getId() {
        return id;
    }
}
