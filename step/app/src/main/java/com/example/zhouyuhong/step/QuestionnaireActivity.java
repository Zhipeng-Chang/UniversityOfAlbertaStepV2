package com.example.zhouyuhong.step;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhouyuhong.step.database.DatabaseQuestionaire;


public class QuestionnaireActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG="tag";
    private QuestionLibrary mQuestionLibrary = new QuestionLibrary();

    private TextView myStageView;
    private TextView mQuestionView;
    private EditText specifyInput;
    private Button nextButton;
    private Button quitButton;
    private Spinner spinner;

    private String mAnswer;
    private String mAnswerFromSpinner;
    private String mSpecify;
    private int mQuestionNumber = 1;

    DatabaseQuestionaire dbQuestionaire;
    SQLiteDatabase myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);


        dbQuestionaire = new DatabaseQuestionaire(this);
        myDB = dbQuestionaire.getWritableDatabase();
        dbQuestionaire.onCreate(myDB);

        myStageView = (TextView)findViewById(R.id.score);
        mQuestionView = (TextView)findViewById(R.id.question);
        nextButton = (Button)findViewById(R.id.next);
        quitButton = (Button)findViewById(R.id.quit);
        spinner = (Spinner) findViewById(R.id.spinner);
        specifyInput = (EditText)findViewById(R.id.specifyInput);
        specifyInput.setEnabled(false);
        specifyInput.setInputType(InputType.TYPE_NULL);
        specifyInput.setFocusable(false);

        spinner.setOnItemSelectedListener(this);
        updateQuestion();
        nextButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //My logic for Button goes in here



                if (mQuestionNumber == 6){
                    SharedPreferences mPreferences = getSharedPreferences("CurrentUser",
                            MODE_PRIVATE);
                    SharedPreferences.Editor editor = mPreferences.edit();
                    editor.putBoolean("questionaire", true);
                    editor.commit();
                    Intent intent = new Intent(QuestionnaireActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    SaveToDatabase(5,spinner.getSelectedItem().toString());

                }
                else{
                    if (needSpecify()){
                        Toast.makeText(QuestionnaireActivity.this, "Please enter your answer", Toast.LENGTH_SHORT).show();
                        specifyInput.setEnabled(true);
                        specifyInput.setInputType(InputType.TYPE_CLASS_TEXT);
                        specifyInput.setFocusableInTouchMode(true);
                        specifyInput.setFocusable(true);

                    }
                    else {
                        updateQuestion();
                    }
                }

            }
        });

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
            if (mAnswer.contains("(please specify)")){
                mSpecify = specifyInput.getText().toString();
                Toast.makeText(QuestionnaireActivity.this, mSpecify, Toast.LENGTH_SHORT).show();
                SaveToDatabase(mQuestionNumber-1,spinner.getSelectedItem().toString());
                specifyInput.getText().clear();
                mSpecify=null;
            }
            else {
                Toast.makeText(QuestionnaireActivity.this, mAnswerFromSpinner, Toast.LENGTH_SHORT).show();
                SaveToDatabase(mQuestionNumber-1,mAnswerFromSpinner);
            }

        }

        if(mQuestionNumber==1){
            mQuestionView.setText(mQuestionLibrary.getQuestion(mQuestionNumber-1));
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.question_1, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        }
        else if(mQuestionNumber==2){
            mQuestionView.setText(mQuestionLibrary.getQuestion(mQuestionNumber-1));

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.question_2, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        }
        else if(mQuestionNumber==3){
            mQuestionView.setText(mQuestionLibrary.getQuestion(mQuestionNumber-1));

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.question_3, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        }
        else if(mQuestionNumber==4){
            mQuestionView.setText(mQuestionLibrary.getQuestion(mQuestionNumber-1));

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.question_4, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        }
        else if(mQuestionNumber==5){
            mQuestionView.setText(mQuestionLibrary.getQuestion(mQuestionNumber-1));

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.question_5, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        }
        specifyInput.setEnabled(false);
        specifyInput.setInputType(InputType.TYPE_NULL);
        specifyInput.setFocusable(false);
        myStageView.setText("" + mQuestionNumber+"/5");
        mQuestionNumber++;
    }

    public boolean needSpecify(){
        mAnswerFromSpinner = spinner.getSelectedItem().toString();
        mSpecify = specifyInput.getText().toString();

        if (mAnswerFromSpinner.contains("(please specify)" )&& mSpecify.matches("")){
            return true;
        }
        else{
            return false;
        }

    }


    public void SaveToDatabase(int Question, String Answer){
        Log.i(TAG, "Adding question to dababase:" + Question);
        dbQuestionaire.insertData(Question, Answer);

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
