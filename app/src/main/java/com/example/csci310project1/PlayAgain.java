package com.example.csci310project1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PlayAgain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.end_screen);

        Intent intent = getIntent();
        boolean won = intent.getBooleanExtra("Won", false);
        String message;
        if(won) {
            message = "You won.\nGood Job!";
        } else {
            message = "You lost.\nSorry";
        }

        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(message);
    }

    public void backToMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
