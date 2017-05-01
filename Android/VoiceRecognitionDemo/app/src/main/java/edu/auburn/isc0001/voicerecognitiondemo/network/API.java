package edu.auburn.isc0001.voicerecognitiondemo.network;

import retrofit2.Call;
import retrofit2.http.GET;

public interface API {

    @GET("./?Google")
    Call<ApiResponse> getApiResponse();
}
