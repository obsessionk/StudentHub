package com.example.tuitionnotesofthestudent.tutor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.tuitionnotesofthestudent.Model.NotesModel;
import com.example.tuitionnotesofthestudent.R;
import com.example.tuitionnotesofthestudent.adapter.viewNotesAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewAssignment extends AppCompatActivity {
    ArrayList<NotesModel> arrayList = new ArrayList<>();
    viewNotesAdapter adapter;
    RecyclerView rv_viewnotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_assignment);

        rv_viewnotes = findViewById(R.id.rv_viewnotes);
        rv_viewnotes.setHasFixedSize(true);
        rv_viewnotes.setLayoutManager(new LinearLayoutManager(ViewAssignment.this));

        loadNotes();

    }

    private void loadNotes() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser.getUid() != null) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("assignment").child(firebaseUser.getUid());
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    arrayList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        NotesModel model = dataSnapshot.getValue(NotesModel.class);
                        Log.d("TAG1", "onDataChange: " + model);
                        arrayList.add(model);
                    }
                    Log.d("TAG1", "arraylist size : " + arrayList.size());
                    adapter = new viewNotesAdapter(ViewAssignment.this, arrayList);
                    rv_viewnotes.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}