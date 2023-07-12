package com.example.mytranslator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class SecondActivity extends AppCompatActivity{
    private LinearLayout savedWordsLayout;
    private List<String> savedWords;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);

        savedWordsLayout=findViewById(R.id.savedWordsLayout);
        Button backButton=findViewById(R.id.backButton);

        savedWords=new ArrayList<>();

        FirebaseApp.initializeApp(this);

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("words");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                Word word=dataSnapshot.getValue(Word.class);
                String savedWord=word.getWord();
                savedWords.add(savedWord);
                displaySavedWords();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
    private void displaySavedWords(){
        savedWordsLayout.removeAllViews();
        for(String word:savedWords){
            TextView textView=new TextView(this);
            textView.setText(word);
            savedWordsLayout.addView(textView);

        }
    }
}