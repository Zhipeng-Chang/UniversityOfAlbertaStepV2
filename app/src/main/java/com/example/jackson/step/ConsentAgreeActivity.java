package com.example.jackson.step;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;

public class ConsentAgreeActivity extends AppCompatActivity {

    private Button AgreeButton;
    private Button DeclineButton;
    private TextView ConsentView;
    private Context mContext;
    private ConsentAgreeActivity activity = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consent);
        mContext = this;
        AgreeButton = (Button) findViewById(R.id.agreeButton);
        DeclineButton = (Button) findViewById(R.id.declineButton);
        ConsentView = (TextView) findViewById(R.id.consentView);

        ConsentView.setMovementMethod(new ScrollingMovementMethod());

        AgreeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SharedPreferences mPreferences = getSharedPreferences("CurrentUser",
                        MODE_PRIVATE);
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putBoolean("agree", true);
                editor.commit();
                Intent intent = new Intent(activity, MainActivity.class);
                startActivity(intent);
                finish();

            }

        });

        DeclineButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(mContext,
                        "In order to use the app, you have to accept the term. Otherwise, please exit the app.",
                        Toast.LENGTH_SHORT)
                        .show();
            }

        });
    }

}
