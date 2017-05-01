package edu.auburn.isc0001.voicerecognitiondemo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import edu.auburn.isc0001.voicerecognitiondemo.R;
import edu.auburn.isc0001.voicerecognitiondemo.ui.intent.IntentActivity;
import edu.auburn.isc0001.voicerecognitiondemo.ui.manual.ManualActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.intent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), IntentActivity.class));
            }
        });

        findViewById(R.id.manual).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ManualActivity.class));
            }
        });
    }
}
