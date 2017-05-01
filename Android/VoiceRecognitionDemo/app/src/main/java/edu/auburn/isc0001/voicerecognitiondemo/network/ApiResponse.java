package edu.auburn.isc0001.voicerecognitiondemo.network;

import com.google.gson.annotations.SerializedName;

public class ApiResponse {
    @SerializedName("displayText")
    private String displayText;

    public String getDisplayText() {
        return displayText;
    }
}
