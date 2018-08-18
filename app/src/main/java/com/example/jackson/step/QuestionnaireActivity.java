package com.example.jackson.step;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

// @ From https://github.com/skooltch84/MultipleChoiceQuiz/tree/master/app/src/main/java/com/skooltchdev/multiplechoicequiz

public class QuestionnaireActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private QuestionLibrary mQuestionLibrary = new QuestionLibrary();

    private TextView mScoreView;
    private TextView mQuestionView;
    private Button mButtonChoice1;
    private Button nextButton;
    private Button quitButton;
    private Spinner spinner;
    private static final String[]paths = {"item 1", "item 2", "item 3"};
    private String questionName;
    private String mAnswer;
    private int mStage = 0;
    private int mQuestionNumber = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);

        mScoreView = (TextView)findViewById(R.id.score);
        mQuestionView = (TextView)findViewById(R.id.question);
        nextButton = (Button)findViewById(R.id.next);
        quitButton = (Button)findViewById(R.id.quit);
        spinner = (Spinner) findViewById(R.id.spinner);

        spinner.setOnItemSelectedListener(this);
        updateQuestion();
        //End of Button Listener for Button1

        //Start of Button Listener for Button2
        nextButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //My logic for Button goes in here

                if (mQuestionNumber == 5){
                    SharedPreferences mPreferences = getSharedPreferences("CurrentUser",
                            MODE_PRIVATE);
                    SharedPreferences.Editor editor = mPreferences.edit();
                    editor.putBoolean("questionaire", true);
                    editor.commit();
                    Intent intent = new Intent(QuestionnaireActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                }
                else{

                    mStage = mStage + 1;
                    updateStage(mStage);
                    updateQuestion();
                }

            }
        });

        //End of Button Listener for Button2


        //Start of Button Listener for Button3
        quitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                    System.exit(0);
                    finish();

            }
        });

        //End of Button Listener for Button3





    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }


    private void updateQuestion(){
        if (mQuestionNumber!=1) {
            mAnswer = spinner.getSelectedItem().toString();
            Toast.makeText(QuestionnaireActivity.this, mAnswer, Toast.LENGTH_SHORT).show();
            mQuestionView.setText(mQuestionLibrary.getQuestion(mQuestionNumber));
        }

        if(mQuestionNumber==1){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.question_1, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        }
        else if(mQuestionNumber==2){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.question_2, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        }
        else if(mQuestionNumber==3){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.question_3, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        }
        else if(mQuestionNumber==4){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.question_4, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        }
        else if(mQuestionNumber==5){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.question_5, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        }


        mQuestionNumber++;
    }


    private void updateStage(int point) {
        mScoreView.setText("" + mStage);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
