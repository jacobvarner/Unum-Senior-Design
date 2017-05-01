package edu.auburn.isc0001.voicerecognitiondemo.ui.intent;

import android.content.Intent;
import android.os.Build;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;

import java.util.ArrayList;
import java.util.Locale;

import edu.auburn.isc0001.voicerecognitiondemo.network.API;
import edu.auburn.isc0001.voicerecognitiondemo.network.ApiResponse;
import edu.auburn.isc0001.voicerecognitiondemo.network.RetrofitHelper;
import nucleus5.presenter.Presenter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import timber.log.Timber;

public class IntentPresenter extends Presenter<IntentActivity>
        implements TextToSpeech.OnInitListener {

    private Retrofit retrofit = RetrofitHelper.getInstance();
    private Call<ApiResponse> call;

    private TextToSpeech textToSpeech;

    @Override
    protected void onTakeView(IntentActivity intentActivity) {
        if (textToSpeech == null) {
            textToSpeech = new TextToSpeech(intentActivity.getApplicationContext(), this);
        }
    }

    @Override
    public void onInit(int status) {
        if (status != TextToSpeech.ERROR) {
            textToSpeech.setLanguage(Locale.US);
        }
    }

    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }

        if (call != null && !call.isCanceled()) {
            call.cancel();
        }
    }

    void notifyActivityResults(Intent data) {
        ArrayList<String> matches = data
                .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
        if (matches.size() > 0) {
            Timber.d(matches.get(0));
            runApiCall();
        }
    }

    void stopTalking() {
        if (textToSpeech != null) {
            textToSpeech.stop();
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
                    String responseText = response.body().getDisplayText();
                    activity.displayResults(responseText);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        textToSpeech.speak(responseText, TextToSpeech.QUEUE_FLUSH, null, null);
                    } else {
                        textToSpeech.speak(responseText, TextToSpeech.QUEUE_FLUSH, null);
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                IntentActivity activity = getView();
                if (activity != null) {
                    activity.displayError(t.getMessage());
                }
            }
        });
    }
}
