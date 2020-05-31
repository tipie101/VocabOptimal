package com.timkatimkah.vocaboptimal;

import java.util.Date;

class VocabEntry {
    private int count;
    private String foreignWord;
    private String translation;
    private long lastTry;

    VocabEntry(int count, String foreignWord, String translation, long lastTry) {
        this.count = count;
        this.foreignWord = foreignWord;
        this.translation = translation;
        this.lastTry = lastTry;
    }

    VocabEntry(int count, String foreignWord, String translation) {
        this.count = count;
        this.foreignWord = foreignWord;
        this.translation = translation;
        this.lastTry = new Date().getTime();
    }

    int getCount() {
        return count;
    }

    void setCount(int count) {
        this.count = count;
    }

    String getForeignWord() {
        return foreignWord;
    }

    void setForeignWord(String foreignWord) {
        this.foreignWord = foreignWord;
    }

    String getTranslation() {
        return translation;
    }

    void setTranslation(String translation) {
        this.translation = translation;
    }

    long getLastTry() {
        return lastTry;
    }

    void setLastTry(long lastTry) {
        this.lastTry = lastTry;
    }
}
