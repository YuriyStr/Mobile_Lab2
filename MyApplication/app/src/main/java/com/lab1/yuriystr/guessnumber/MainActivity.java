package com.lab1.yuriystr.guessnumber;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.*;

import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    TextView tvInfo;
    TextView tvSquare;
    EditText etInput;
    Button bControl;
    RadioGroup rgLang;
    int randomNumber;
    boolean isFinished;
    String languageCode;
    Random rnd;
    HashMap<ButtonKey, String> possibleButtonTexts;
    HashMap<LabelKey, String> possibleLabelTexts;
    LabelKey currentLabelTextKey;
    ButtonKey currentButtonTextKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeControls();
        updatePossibleTexts();
        rnd = new Random();
        startNewGame();
    }

    private void initializeControls(){
        tvInfo = (TextView)findViewById(R.id.infoField);
        tvSquare = (TextView)findViewById(R.id.squareTextView);
        etInput = (EditText)findViewById(R.id.inputField);
        bControl = (Button)findViewById(R.id.submitButton);
        rgLang = (RadioGroup)findViewById(R.id.languageRadioGroup);
        languageCode = "en";
        initializeRadioButtonStates();
    }

    private void initializeRadioButtonStates(){
        rgLang.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioEnglish:
                        languageCode = "en";
                        break;
                    case R.id.radioRussian:
                        languageCode = "ru";
                        break;
                }
                updateResources();
            }
        });
    }

    public void onClick(View v){
        if (isFinished) {
            startNewGame();
        }
        else {
            play();
        }
    }

    private void startNewGame() {
        randomNumber = rnd.nextInt(100) + 1;
        isFinished = false;
        currentButtonTextKey = ButtonKey.INPUT_VALUE;
        currentLabelTextKey = LabelKey.TRY_TO_GUESS;
        updateUI();
    }

    private void play() {
        if (etInput.getText().toString().isEmpty()) {
            currentLabelTextKey = LabelKey.ERROR;
            updateUI();
            return;
        }
        int number = Integer.parseInt(etInput.getText().toString());
        if (number < 1 || number > 100) {
            currentLabelTextKey = LabelKey.ERROR;
            updateUI();
            return;
        }
        if (number > randomNumber) {
            currentLabelTextKey = LabelKey.AHEAD;
            updateUI();
        }
        else if (number < randomNumber) {
            currentLabelTextKey = LabelKey.BEHIND;
            updateUI();
        }
        else {
            endGame();
        }
    }

    private void endGame() {
        currentLabelTextKey = LabelKey.HIT;
        currentButtonTextKey = ButtonKey.PLAY_MORE;
        updateUI();
        isFinished = true;
    }

    private void updateResources() {
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale(languageCode.toLowerCase()));
        res.updateConfiguration(conf, dm);
        updatePossibleTexts();
        updateUI();
    }

    private void updateUI(){
        bControl.setText(possibleButtonTexts.get(currentButtonTextKey));
        tvInfo.setText(possibleLabelTexts.get(currentLabelTextKey));
        if (currentLabelTextKey != LabelKey.ERROR && currentLabelTextKey != LabelKey.TRY_TO_GUESS) {
            int inputValue = Integer.parseInt(etInput.getText().toString());
            tvSquare.setText(inputValue + "^2 = " +  (inputValue * inputValue));
        }
    }

    private void updatePossibleLabelTexts(){
        possibleLabelTexts = new HashMap<LabelKey, String>();
        possibleLabelTexts.put(LabelKey.BEHIND, getResources().getString(R.string.behind));
        possibleLabelTexts.put(LabelKey.AHEAD, getResources().getString(R.string.ahead));
        possibleLabelTexts.put(LabelKey.HIT, getResources().getString(R.string.hit));
        possibleLabelTexts.put(LabelKey.ERROR, getResources().getString(R.string.error));
        possibleLabelTexts.put(LabelKey.TRY_TO_GUESS, getResources().getString(R.string.try_to_guess));
    }

    private void updatePossibleButtonTexts(){
        possibleButtonTexts = new HashMap<ButtonKey, String>();
        possibleButtonTexts.put(ButtonKey.PLAY_MORE, getResources().getString(R.string.play_more));
        possibleButtonTexts.put(ButtonKey.INPUT_VALUE, getResources().getString(R.string.input_value));
    }

    private void updatePossibleTexts() {
        updatePossibleButtonTexts();
        updatePossibleLabelTexts();
    }
}
