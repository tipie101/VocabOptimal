package com.timkatimkah.vocaboptimal;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.snackbar.Snackbar;

import java.util.Date;

public class VocabCardFragment extends Fragment {

    private DBHelper dbHelper;
    private VocabTrainer trainer;
    private VocabEntry currentEntry;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vocab_card, container, false);
        dbHelper = new DBHelper(getActivity().getApplicationContext());
        trainer = new VocabTrainer();
        trainer.setVocab(dbHelper.loadVocabulary(trainer.getMaxRepetitions()));
        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        currentEntry = trainer.pickEntry();
        System.out.println(currentEntry.getForeignWord());
        TextView textView = (TextView) getActivity().findViewById(R.id.foreignWordView);
        textView.setText(currentEntry.getForeignWord());

        Button button = getActivity().findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView answerField = (TextView) getActivity().findViewById(R.id.translation);
                if(answerField.getText().toString().trim().equals(currentEntry.getTranslation())) {
                    currentEntry.setCount(currentEntry.getCount() + 1);
                    Snackbar.make(view, "CORRECT ANSWER!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    currentEntry.setCount(0);
                    Snackbar.make(view, "WRONG ANSWER!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                currentEntry.setLastTry(new Date().getTime());
                dbHelper.updateEntry(currentEntry.getTranslation(), currentEntry.getCount(), currentEntry.getLastTry());
                answerField.setText("");
                currentEntry = trainer.pickEntry();
                TextView vocabField = (TextView) getActivity().findViewById(R.id.foreignWordView);
                vocabField.setText(currentEntry.getForeignWord());
            }
        });

        view.findViewById(R.id.return_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(VocabCardFragment.this)
                        .navigate(R.id.action_CardFragment_to_InputMenu);
            }
        });
    }

}
