package com.timkatimkah.vocaboptimal;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DBHelper extends SQLiteOpenHelper {

    // singleton-pattern: https://guides.codepath.com/android/local-databases-with-sqliteopenhelper
    private static DBHelper dbHelper;

    // Database Info
    private static final String DATABASE_NAME = "vocabOptimal";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_VOCAB = "vocabulary";

    // Vocabulary Columns
    private static final String FOREIGN_WORD = "foreignWord";
    private static final String TRANSLATION = "translation";
    private static final String COUNT = "count";
    private static final String LAST_TRY = "lastTry";


    private DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    static synchronized DBHelper getInstance(Context context) {
        if (dbHelper == null) {
            dbHelper = new DBHelper(context);
            System.out.println("Initialized db-Helper");
        }
        return dbHelper;
    }

    // Called when the database connection is being configured.
    // Configure database settings for things like foreign key support, write-ahead logging, etc.
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_VOCAB +
                "(" +
                TRANSLATION + " TEXT PRIMARY KEY, " +
                FOREIGN_WORD + " TEXT, " +
                COUNT + " INTEGER, " +
                LAST_TRY + " DATETIME DEFAULT CURRENT_TIMESTAMP)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_VOCAB);
            onCreate(db);
        }
    }

    ArrayList<VocabEntry> loadVocabulary() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<VocabEntry> vocabEntries = new ArrayList<VocabEntry>();
        String readVocab = "SELECT * FROM " + TABLE_VOCAB;
        Cursor cursor = db.rawQuery(readVocab, null);
        // TODO: create a helper function
        if (cursor.moveToFirst()) {
            do {
                vocabEntries.add(
                        new VocabEntry(
                                cursor.getInt(cursor.getColumnIndex(COUNT)),
                                cursor.getString(cursor.getColumnIndex(FOREIGN_WORD)),
                                cursor.getString(cursor.getColumnIndex(TRANSLATION)),
                                cursor.getLong(cursor.getColumnIndex(LAST_TRY))
                        )
                );
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return vocabEntries;
    }

    ArrayList<VocabEntry> loadVocabulary(int maxCount) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<VocabEntry> vocabEntries = new ArrayList<VocabEntry>();
        String readVocab = "SELECT * FROM " + TABLE_VOCAB + " WHERE " + COUNT + " < ?";
        String[] params = {"" + maxCount};
        Cursor cursor = db.rawQuery(readVocab, params);
        // TODO: create a helper function
        if (cursor.moveToFirst()) {
            do {
                vocabEntries.add(
                        new VocabEntry(
                                cursor.getInt(cursor.getColumnIndex(COUNT)),
                                cursor.getString(cursor.getColumnIndex(FOREIGN_WORD)),
                                cursor.getString(cursor.getColumnIndex(TRANSLATION)),
                                cursor.getLong(cursor.getColumnIndex(LAST_TRY))
                        )
                );
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return vocabEntries;
    }

    void saveNewVocab(String vocab, String translation) {
        if (hasTranslation(translation)) {
            System.out.println("Entry with Translation: '" + translation + "' already exists!");
            return;
        }
        long currentMillis = new Date().getTime();
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COUNT, 0);
        values.put(LAST_TRY, currentMillis);
        values.put(TRANSLATION, translation);
        values.put(FOREIGN_WORD, vocab);
        db.insert(TABLE_VOCAB, null, values);
        db.close();
    }

    // Helper-Method
    // Translation is key, Vocab is Value
    void saveNewVocabs(Map<String, String> vocabs) {
        for (Map.Entry me : vocabs.entrySet()) {
            saveNewVocab(me.getValue().toString(), me.getKey().toString());
        }
    }

    void updateEntry(String translation, int count, long lastTry) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COUNT, count);
        values.put(LAST_TRY, lastTry);
        db.update(TABLE_VOCAB, values, TRANSLATION + " = ?", new String[] {translation});
        db.close();
    }

    boolean hasTranslation(String key) {
        SQLiteDatabase db = this.getReadableDatabase();
        String exists = "SELECT EXISTS(SELECT 1 FROM " + TABLE_VOCAB + " WHERE " + TRANSLATION + "= ?);";
        Cursor cursor = db.rawQuery(exists, new String[] {key});
        cursor.moveToFirst();
        boolean found = 1 == cursor.getInt(0);
        cursor.close();
        db.close();
        return found;
    }
}


