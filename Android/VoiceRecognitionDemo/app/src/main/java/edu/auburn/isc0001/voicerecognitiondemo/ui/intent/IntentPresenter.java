package edu.auburn.isc0001.voicerecognitiondemo.ui.intent;

import android.content.Intent;
import android.speech.RecognizerIntent;

import java.util.ArrayList;

import edu.auburn.isc0001.voicerecognitiondemo.network.API;
import edu.auburn.isc0001.voicerecognitiondemo.network.ApiResponse;
import edu.auburn.isc0001.voicerecognitiondemo.network.RetrofitHelper;
import nucleus5.presenter.Presenter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import timber.log.Timber;

public class IntentPresenter extends Presenter<IntentActivity> {

    private Retrofit retrofit = RetrofitHelper.getInstance();

    private Call<ApiResponse> call;

    void notifyActivityResults(Intent data) {
        ArrayList<String> matches = data
                .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
        if (matches.size() > 0) {
            Timber.d(matches.get(0));
            runApiCall();
        }
    }

    private void runApiCall() {
        if (call != null && !call.isCanceled()) {
            call.cancel();
        }

        IntentActivity activity = getView();
        if (activity != null) {
            activity.displayLoading();
        }

        call = retrofit.create(API.class).getApiResponse();
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                IntentActivity activity = getView();
                if (activity != null) {
                    activity.displayResults(response.body().getDisplayText());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

            }
        });
    }
}
