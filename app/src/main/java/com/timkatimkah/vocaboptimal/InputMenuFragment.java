package com.timkatimkah.vocaboptimal;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

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

        TabLayout tabs = view.findViewById(R.id.tabs);
        tabs.getTabAt(1).select();
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                NavHostFragment.findNavController(InputMenuFragment.this)
                        .navigate(R.id.action_input_menu_to_card);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
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

        // Request Permission to read external file
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
            && getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                getActivity().requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},  MainActivity.PERMISSION_REQUEST_STORAGE);
        }

        view.findViewById(R.id.save_vocab_from_txt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performFileSearch();
            }
        });
    }

    private void performFileSearch() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        // TODO: declare own file extension
        intent.setType("text/*");
        getActivity().startActivityForResult(intent, MainActivity.READ_REQUEST_CODE);
    }




}
