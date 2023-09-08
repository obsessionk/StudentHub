package com.example.tuitionnotesofthestudent.student;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tuitionnotesofthestudent.Model.NotesModel;
import com.example.tuitionnotesofthestudent.R;
import com.example.tuitionnotesofthestudent.adapter.studentViewAssignmentAdapter;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class StudentViewNotes extends AppCompatActivity {

    ArrayList<NotesModel> arrayList = new ArrayList<>();
    studentViewAssignmentAdapter adapter;
    RecyclerView rv_viewnotes;

    Intent intent;
    String childName = "";
    PDFView idPDFView;
    LinearLayout llPdf;
    TextView tvClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_view_notes);

        rv_viewnotes = findViewById(R.id.rv_viewNotes);
        llPdf = findViewById(R.id.llPdf);
        tvClose = findViewById(R.id.tvClose);
        idPDFView = findViewById(R.id.idPDFView);

        rv_viewnotes.setHasFixedSize(true);
        rv_viewnotes.setLayoutManager(new LinearLayoutManager(StudentViewNotes.this));

        intent = getIntent();
        childName = intent.getStringExtra("childName");
        Toast.makeText(StudentViewNotes.this, "child name : " + childName, Toast.LENGTH_SHORT).show();

        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llPdf.setVisibility(View.GONE);
                rv_viewnotes.setVisibility(View.VISIBLE);
            }
        });

        loadNotes();
    }

    private void loadNotes() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Log.d("TAG1", "inside load notes");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(childName);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("TAG1", "loading notes");
                arrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        NotesModel model = dataSnapshot1.getValue(NotesModel.class);
                        Log.d("TAG1", "onDataChange: " + dataSnapshot1);
                        arrayList.add(model);
                    }
                }
                Log.d("TAG1", "arraylist size : " + arrayList.size());
                adapter = new studentViewAssignmentAdapter(StudentViewNotes.this, arrayList, childName,true);
                rv_viewnotes.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void callPdfUrl(String url) {
        new RetrivePDFfromUrl().execute(url);
    }

    // create an async task class for loading pdf file from URL.
    public class RetrivePDFfromUrl extends AsyncTask<String, Void, InputStream> {
        @Override
        protected InputStream doInBackground(String... strings) {
            // we are using inputstream
            // for getting out PDF.
            InputStream inputStream = null;
            try {
                URL url = new URL(strings[0]);
                // below is the step where we are
                // creating our connection.
                HttpURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    // response is success.
                    // we are getting input stream from url
                    // and storing it in our variable.
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }

            } catch (IOException e) {
                // this is the method
                // to handle errors.
                e.printStackTrace();
                return null;
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            adapter.progressDialog.dismiss();
            // after the execution of our async
            // task we are loading our pdf in our pdf view.
            rv_viewnotes.setVisibility(View.GONE);
            llPdf.setVisibility(View.VISIBLE);
            idPDFView.fromStream(inputStream).load();
        }
    }
}