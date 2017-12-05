package edu.illinois.finalproject.schemas;

import com.google.gson.annotations.SerializedName;

public class AddressResult {
    @SerializedName("formatted_address")
    private String formattedAddress;

    public String getFormattedAddress() {
        return formattedAddress;
    }
}
