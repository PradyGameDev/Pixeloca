package edu.illinois.finalproject.schemas;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ReverseGeocoderResponse {
    @SerializedName("addressResultArrayList")
    private ArrayList<AddressResult> addressResultArrayList;

    public ArrayList<AddressResult> getAddressResultArrayList() {
        return addressResultArrayList;
    }
}
