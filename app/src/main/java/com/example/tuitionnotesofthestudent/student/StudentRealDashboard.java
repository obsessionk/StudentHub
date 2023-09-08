package com.example.tuitionnotesofthestudent.student;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.tuitionnotesofthestudent.R;
import com.example.tuitionnotesofthestudent.tutor.ViewNotes;

public class StudentRealDashboard extends AppCompatActivity {

    CardView btn_viewAssignment;
    CardView btn_viewNotes;
    CardView btn_viewRemarks;

    Intent intent;
    String childName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_real_dashboard);

        btn_viewAssignment = findViewById(R.id.cv_viewAssignment);
        btn_viewNotes = findViewById(R.id.cv_viewNotes);
        btn_viewRemarks = findViewById(R.id.cv_viewRemarks);

        intent = getIntent();
        childName = intent.getStringExtra("childName");

        btn_viewNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentRealDashboard.this, StudentViewNotes.class);
                intent.putExtra("childName", "notes");
                startActivity(intent);
            }
        });

        btn_viewAssignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentRealDashboard.this, ViewAssignment.class);
                intent.putExtra("childName", "assignment");
                startActivity(intent);
            }
        });
        btn_viewRemarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentRealDashboard.this, StudentDashboard.class);
                intent.putExtra("childName", childName);
                startActivity(intent);
            }
        });
    }
}