package com.example.mytranslator;

public class Word {
    private String word;

    public Word() {
        // Default constructor required for Firebase
    }

    public Word(String word) {
        this.word = word;
    }

    public String getWord() {
        return word;
    }
}
