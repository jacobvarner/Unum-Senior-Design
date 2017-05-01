package edu.auburn.isc0001.voicerecognitiondemo.ui.intent;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import edu.auburn.isc0001.voicerecognitiondemo.R;
import edu.auburn.isc0001.voicerecognitiondemo.util.ToolbarUtils;
import nucleus5.factory.RequiresPresenter;
import nucleus5.view.NucleusAppCompatActivity;

@RequiresPresenter(IntentPresenter.class)
public class IntentActivity extends NucleusAppCompatActivity<IntentPresenter> {
    private static final int REQUEST_CODE = 1234; // arbitrary
    private static final String VIEW_STATE = "view_state";
    private static final int STATE_NONE = 0;
    private static final int STATE_LOADING = 1;
    private static final int STATE_TEXT = 2;

    private int state = STATE_NONE;

    private Toolbar toolbar;
    private View progressBar;
    private TextView textView;

    private boolean wasActivityResult = false;
    private Intent activityResultData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        ToolbarUtils.init(this, toolbar);
        ToolbarUtils.setNavigationIcon(this, toolbar, R.drawable.ic_arrow_back_black_24dp,
                ToolbarUtils.ICON_ALPHA_BLACK);

        progressBar = findViewById(R.id.progressBar);
        textView = (TextView) findViewById(R.id.textView);

        if (savedInstanceState != null) {
            state = savedInstanceState.getInt(VIEW_STATE);
        }

        updateView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_mic, menu);
        ToolbarUtils.setAllMenuItemsAlpha(menu, ToolbarUtils.ICON_ALPHA_BLACK);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.mic:
                runVoiceRecognizer();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(VIEW_STATE, state);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (wasActivityResult) {
            wasActivityResult = false;
            getPresenter().notifyActivityResults(activityResultData);
            activityResultData = null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE) {
                wasActivityResult = true;
                activityResultData = data;
            }
        }
    }

    private void runVoiceRecognizer() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "What is your question?");
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "en-US");
        startActivityForResult(intent, REQUEST_CODE);
    }

    private void updateView() {
        switch (state) {
            default:
            case STATE_NONE:
                textView.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                break;
            case STATE_LOADING:
                textView.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                break;
            case STATE_TEXT:
                textView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                break;
        }
    }

    void displayLoading() {
        state = STATE_LOADING;
        updateView();
    }

    void displayResults(String text) {
        textView.setText(text);
        state = STATE_TEXT;
        updateView();
    }
}
