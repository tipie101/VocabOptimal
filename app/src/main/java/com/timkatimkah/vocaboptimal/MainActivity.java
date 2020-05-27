package com.timkatimkah.vocaboptimal;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    private VocabTrainer trainer;
    private VocabEntry currentEntry;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(getApplicationContext());
        trainer = new VocabTrainer();
        trainer.setVocab(dbHelper.loadVocabulary(trainer.getMaxRepetitions()));

        currentEntry = trainer.pickEntry();
        TextView textView = (TextView) findViewById(R.id.foreignWordView);
        textView.setText(currentEntry.getForeignWord());

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView answerField = (TextView) findViewById(R.id.translation);
                if(answerField.getText().toString().trim().equals(currentEntry.getTranslation())) {
                    // TODO: Update
                    currentEntry = trainer.pickEntry();
                    TextView vocabField = (TextView) findViewById(R.id.foreignWordView);
                    vocabField.setText(currentEntry.getForeignWord());
                    answerField.setText("");
                }
            }
        });

    }

}

