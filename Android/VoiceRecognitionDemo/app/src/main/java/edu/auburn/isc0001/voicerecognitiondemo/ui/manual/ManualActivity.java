package edu.auburn.isc0001.voicerecognitiondemo.ui.manual;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
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

@RequiresPresenter(ManualPresenter.class)
public class ManualActivity extends NucleusAppCompatActivity<ManualPresenter> {
    private static final String VIEW_STATE = "view_state";
    private static final int STATE_NONE = 0;
    private static final int STATE_LOADING = 1;
    private static final int STATE_TEXT = 2;

    private int state = STATE_NONE;

    private View progressBar;
    private TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.search_statusbar));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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
                getPresenter().startListening();
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

    void displayPartialResults(String text) {
        textView.setText(text);
        state = STATE_TEXT;
        updateView();
    }

    void displayResults(String text) {
        textView.setText(text);
        state = STATE_TEXT;
        updateView();
    }

    void displayError(String text) {
        textView.setText(text);
        state = STATE_TEXT;
        updateView();
    }
}
