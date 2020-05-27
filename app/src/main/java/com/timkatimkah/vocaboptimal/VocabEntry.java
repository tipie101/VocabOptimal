package com.timkatimkah.vocaboptimal;

public class VocabEntry {
    private int count;
    private String foreignWord;
    private String translation;
    // TODO: TimeStamp
    private long lastTry;

    public VocabEntry(int count, String foreignWord, String translation, long lastTry) {
        this.count = count;
        this.foreignWord = foreignWord;
        this.translation = translation;
        this.lastTry = lastTry;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getForeignWord() {
        return foreignWord;
    }

    public void setForeignWord(String foreignWord) {
        this.foreignWord = foreignWord;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public long getLastTry() {
        return lastTry;
    }

    public void setLastTry(int lastTry) {
        this.lastTry = lastTry;
    }
}
