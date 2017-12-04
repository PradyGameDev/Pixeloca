package edu.illinois.finalproject.schemas;

import java.util.ArrayList;

public class ReverseGeocoderResponse {
    private ArrayList<AddressResult> results;

    public ArrayList<AddressResult> getResults() {
        return results;
    }
}
