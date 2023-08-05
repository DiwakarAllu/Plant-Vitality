package com.example.plantvitality;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class YourCropsActivity extends AppCompatActivity {
    CardView plantDiseases,fertizers,tips,plantCare,pros,chatbot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_crops);

        plantCare=findViewById(R.id.plantCare);
        plantDiseases=findViewById(R.id.plantDiseases);
        fertizers=findViewById(R.id.fertilizers);
        tips=findViewById(R.id.fertilizers);
        pros=findViewById(R.id.pro);
        chatbot=findViewById(R.id.chatbot);

        chatbot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}