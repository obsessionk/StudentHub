package com.example.tuitionnotesofthestudent.tutor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.tuitionnotesofthestudent.R;

public class TutorRealDashboard extends AppCompatActivity {

    CardView btn_viewStudents;
    CardView btn_viewAssignment;
    CardView btn_viewNotes;
    CardView btn_uploadNotes;
    CardView btn_uploadAssignment;

    Intent intent;

    String userId, email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_real_dashboard);

        btn_viewStudents = findViewById(R.id.cv_viewStudents);
        btn_viewAssignment = findViewById(R.id.cv_viewAssignment);
        btn_viewNotes = findViewById(R.id.cv_viewNotes);
        btn_uploadNotes = findViewById(R.id.cv_uploadNotes);
        btn_uploadAssignment = findViewById(R.id.cv_uploadAssignment);

        intent = getIntent();
        email = intent.getStringExtra("email");
        userId = intent.getStringExtra("userId");
        password = intent.getStringExtra("password");

        btn_viewStudents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TutorRealDashboard.this, TutorDashboard.class);
                intent.putExtra("email", email);
                intent.putExtra("password", password);
                startActivity(intent);
            }
        });

        btn_uploadNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TutorRealDashboard.this, AddNotes.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });
        btn_uploadAssignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TutorRealDashboard.this, uploadAssignment.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });
        btn_viewNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TutorRealDashboard.this, ViewNotes.class);
                intent.putExtra("userId", userId);
                intent.putExtra("view", "notes");
                startActivity(intent);
            }
        });


        btn_viewAssignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TutorRealDashboard.this, ViewNotes.class);
                intent.putExtra("userId", userId);
                intent.putExtra("view", "assignment");
                startActivity(intent);
            }
        });
    }
}