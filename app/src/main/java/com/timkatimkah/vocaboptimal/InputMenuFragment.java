package com.timkatimkah.vocaboptimal;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.snackbar.Snackbar;

public class InputMenuFragment extends Fragment {

    private DBHelper dbHelper;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dbHelper = new DBHelper(getActivity().getApplicationContext());
        View view = inflater.inflate(R.layout.input_menu, container, false);
        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.return_card_frag).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(InputMenuFragment.this)
                        .navigate(R.id.action_input_menu_to_card);
            }
        });

        view.findViewById(R.id.save_vocab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // save to DB
                TextView textViewVocab = (TextView) getActivity().findViewById(R.id.newVocab);
                TextView textViewTranslation = (TextView) getActivity().findViewById(R.id.newVocabTranslation);
                String vocab = textViewVocab.getText().toString().trim();
                String translation = textViewTranslation.getText().toString().trim();

                if (!dbHelper.hasTranslation(translation)) {
                dbHelper.saveNewVocab(vocab, translation);
                textViewTranslation.setText("");
                textViewVocab.setText("");
                } else {
                    Snackbar.make(view, "VOCAB ALREADY EXISTS!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });


    }


}
