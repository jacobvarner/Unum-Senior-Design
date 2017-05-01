package edu.auburn.isc0001.voicerecognitiondemo.ui.manual;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import edu.auburn.isc0001.voicerecognitiondemo.network.API;
import edu.auburn.isc0001.voicerecognitiondemo.network.ApiResponse;
import edu.auburn.isc0001.voicerecognitiondemo.network.RetrofitHelper;
import edu.auburn.isc0001.voicerecognitiondemo.ui.intent.IntentActivity;
import nucleus5.presenter.Presenter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import timber.log.Timber;

public class ManualPresenter extends Presenter<ManualActivity>
        implements TextToSpeech.OnInitListener, RecognitionListener {

    private Retrofit retrofit = RetrofitHelper.getInstance();
    private Call<ApiResponse> call;

    private SpeechRecognizer speechRecognizer;
    private Intent recognizerIntent;
    private TextToSpeech textToSpeech;

    @Override
    protected void onCreate(@Nullable Bundle savedState) {
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "en-US");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
    }

    @Override
    protected void onTakeView(ManualActivity manualActivity) {
        if (textToSpeech == null) {
            textToSpeech = new TextToSpeech(manualActivity.getApplicationContext(), this);
        }

        if (speechRecognizer == null) {
            speechRecognizer = SpeechRecognizer
                    .createSpeechRecognizer(manualActivity.getApplicationContext());
            speechRecognizer.setRecognitionListener(this);
        }
    }

    @Override
    public void onInit(int status) {
        if (status != TextToSpeech.ERROR) {
            textToSpeech.setLanguage(Locale.US);
        }
    }

    @Override
    public void onReadyForSpeech(Bundle params) {
        ManualActivity activity = getView();
        if (activity != null) {
            activity.displayResults("Listening...");
        }
    }

    @Override
    public void onBeginningOfSpeech() {
        // do nothing
    }

    @Override
    public void onRmsChanged(float rmsdB) {
        // do nothing
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        // do nothing
    }

    @Override
    public void onEndOfSpeech() {
        // do nothing
    }

    @Override
    public void onError(int error) {
        ManualActivity activity = getView();
        if (activity != null) {
            activity.displayError("");
        }
    }

    @Override
    public void onResults(Bundle results) {
        ManualActivity activity = getView();
        if (activity != null) {
            activity.displayLoading();
            runApiCall();
        }
    }

    @Override
    public void onPartialResults(Bundle partialResults) {
        ManualActivity activity = getView();
        if (activity != null) {
            List<String> partials = partialResults
                    .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            if (partials.size() > 0) {
                activity.displayPartialResults(partials.get(0));
            }
        }
    }

    @Override
    public void onEvent(int eventType, Bundle params) {
        // do nothing
    }

    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }

        if (speechRecognizer != null) {
            speechRecognizer.stopListening();
            speechRecognizer.destroy();
        }

        if (call != null && !call.isCanceled()) {
            call.cancel();
        }
    }

    private void runApiCall() {
        if (call != null && !call.isCanceled()) {
            call.cancel();
        }

        ManualActivity activity = getView();
        if (activity != null) {
            activity.displayLoading();
        }

        call = retrofit.create(API.class).getApiResponse();
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                ManualActivity activity = getView();
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
                ManualActivity activity = getView();
                if (activity != null) {
                    activity.displayError(t.getMessage());
                }
            }
        });
    }

    void startListening() {
        if (textToSpeech != null) {
            textToSpeech.stop();
        }

        if (speechRecognizer != null) {
            speechRecognizer.startListening(recognizerIntent);
        }
    }
}
