package com.timkatimkah.vocaboptimal;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    private VocabTrainer trainer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(getApplicationContext());
        trainer = new VocabTrainer();
        trainer.setVocab(dbHelper.loadVocabulary());

        TextView textView = (TextView) findViewById(R.id.foreignWordView);
        textView.setText(trainer.pickEntry().getForeignWord());

    }

}

