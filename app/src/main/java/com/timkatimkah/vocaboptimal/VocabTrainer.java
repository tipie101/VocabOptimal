package com.timkatimkah.vocaboptimal;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

class VocabTrainer {
    int getMaxRepetitions() {
        return maxRepetitions;
    }

    private int maxRepetitions;
    private int[] timeouts; // the time to wait in millis
    private int timeoutStep; // Timeout-Step in Minutes
    private ArrayList<VocabEntry> vocab = new ArrayList<>();

    VocabTrainer() {
        this.maxRepetitions = 5;
        this.timeoutStep = 30;
        setDefaultTimeouts(this.maxRepetitions);
    }

    VocabTrainer(int maxRepetitions, int timeoutStep){
        this.maxRepetitions = maxRepetitions;
        this.timeoutStep = timeoutStep;
        setDefaultTimeouts(this.maxRepetitions);
    }

    private void setDefaultTimeouts(int number) {
        this.timeouts =  new int[number];
        for (int i = 0; i < number; i++) {
            timeouts[i] = i * 60 * 1000 * timeoutStep;
        }
    }

    void setVocab(ArrayList<VocabEntry> vocab) {
        this.vocab = vocab;
    }

    VocabEntry pickEntry() {
        Random randomize = new Random();

        ArrayList<VocabEntry> activeVocab = new ArrayList<>();
        for (int i = 0; i < this.vocab.size(); i++) {
            if (isActive(vocab.get(i))) {
                activeVocab.add(vocab.get(i));
            }
        }
        if (activeVocab.size() == 0) {
            return new VocabEntry(0, "...Nothing to See...", "...Nothing to See...");
        }
        // TODO: Catch if activeVocab is empty
        return activeVocab.get(randomize.nextInt(activeVocab.size()));
    }

    private boolean isActive(VocabEntry vocabEntry) {
        return (this.maxRepetitions > vocabEntry.getCount()) && (new Date().getTime() >= vocabEntry.getLastTry() + timeouts[vocabEntry.getCount()]);
    }
}
