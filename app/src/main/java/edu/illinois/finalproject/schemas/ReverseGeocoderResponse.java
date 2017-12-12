package edu.illinois.finalproject.schemas;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ReverseGeocoderResponse {
    @SerializedName("results")
    private ArrayList<AddressResult> results;

    public ArrayList<AddressResult> getResults() {
        return results;
    }
}
