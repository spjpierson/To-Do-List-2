package com.piersonapps.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
private TextView t;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

     //   t = findViewById(R.id.t);
     //   t.setOnClickListener(this::onClick);
    }

    public void onClick(View arg0){
     //   String text = "My text on click";
      //  t.setText(text);
    }
}