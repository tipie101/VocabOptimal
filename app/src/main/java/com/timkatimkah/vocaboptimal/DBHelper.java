package com.timkatimkah.vocaboptimal;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Date;

public class DBHelper extends SQLiteOpenHelper {

    // TODO: consider singleton-pattern: https://guides.codepath.com/android/local-databases-with-sqliteopenhelper
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


    DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
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
        long currentMillis = new Date().getTime();
        SQLiteDatabase db = this.getWritableDatabase();
        String insert = "INSERT INTO " + TABLE_VOCAB + " (" +
                FOREIGN_WORD + ", " +
                TRANSLATION + ", " +
                COUNT + ", " +
                LAST_TRY
                + ") VALUES('" +
                vocab + "', '" +
                translation + "', " +
                "0, " + currentMillis + ")";
        db.execSQL(insert);
        db.close();
    }

    void updateEntry(String translation, int count, long lastTry) {
        SQLiteDatabase db = this.getWritableDatabase();
        String insert = "UPDATE " + TABLE_VOCAB + " SET " +
                COUNT + " = " + count + ", " +
                LAST_TRY + " = " + lastTry
                + " WHERE " + TRANSLATION + " = '" + translation + "'";
        db.execSQL(insert);
        db.close();
    }
}


