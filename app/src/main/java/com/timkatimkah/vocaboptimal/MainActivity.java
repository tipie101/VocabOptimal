package com.timkatimkah.vocaboptimal;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

// TODO: https://www.youtube.com/watch?v=zYVEMCiDcmY&t=490s

public class MainActivity extends AppCompatActivity {

    private DBHelper dbHelper;

    final static int PERMISSION_REQUEST_STORAGE = 1000;
    final static int READ_REQUEST_CODE = 42;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show();
                // finish();
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = DBHelper.getInstance(getApplicationContext());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        System.out.println("onActivityResult-----------------------------------------------------");
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MainActivity.READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                HashMap<String,String> fileContent = readVocabsFromTextFile(uri);
                dbHelper.saveNewVocabs(fileContent);
            }
        }
    }

    private HashMap<String, String> readVocabsFromTextFile(Uri uri){
        // translation is key, foreign word is value
        HashMap<String,String> vocabs = new HashMap();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(getContentResolver().openInputStream(uri)));
            String line;

            while ((line = reader.readLine()) != null) {
                String[] vocabComponents = line.split("\t");
                // TODO: Input checks
                vocabs.put(vocabComponents[1].trim(), vocabComponents[0].trim());
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return vocabs;
    }

}

