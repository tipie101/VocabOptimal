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
import com.google.android.material.tabs.TabLayout;

import java.util.Date;

public class VocabCardFragment extends Fragment {

    private DBHelper dbHelper;
    private VocabTrainer trainer;
    private VocabEntry currentEntry;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vocab_card, container, false);
        dbHelper = DBHelper.getInstance(getActivity().getApplicationContext());
        trainer = new VocabTrainer();
        trainer.setVocab(dbHelper.loadVocabulary(trainer.getMaxRepetitions()));
        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        currentEntry = trainer.pickEntry();
        System.out.println(currentEntry.getTranslation());
        TextView textView = (TextView) getActivity().findViewById(R.id.translation);
        textView.setText(currentEntry.getTranslation());

        Button button = getActivity().findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView answerField = (TextView) getActivity().findViewById(R.id.foreignWordView);
                if(answerField.getText().toString().trim().equals(currentEntry.getForeignWord())) {
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
                TextView vocabField = (TextView) getActivity().findViewById(R.id.translation);
                vocabField.setText(currentEntry.getTranslation());
            }
        });
        TabLayout tabs = view.findViewById(R.id.tabs);
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                NavHostFragment.findNavController(VocabCardFragment.this)
                        .navigate(R.id.action_CardFragment_to_InputMenu);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

    }

}
