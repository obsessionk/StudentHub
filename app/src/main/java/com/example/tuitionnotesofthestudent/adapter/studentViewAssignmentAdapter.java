package com.example.tuitionnotesofthestudent.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tuitionnotesofthestudent.Model.NotesModel;
import com.example.tuitionnotesofthestudent.R;
import com.example.tuitionnotesofthestudent.student.StudentViewNotes;
import com.example.tuitionnotesofthestudent.student.ViewAssignment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class studentViewAssignmentAdapter extends RecyclerView.Adapter<studentViewAssignmentAdapter.viewholder> {
    Context context;
    ArrayList<NotesModel> viewNotes;
    public ProgressDialog progressDialog;
    String studentName = "";
    boolean StudentViewNotes = false;

    public studentViewAssignmentAdapter(Context context, ArrayList<NotesModel> viewNotes, String studentName,boolean StudentViewNotes) {
        this.context = context;
        this.viewNotes = viewNotes;
        progressDialog = new ProgressDialog(context);
        this.studentName = studentName;
        this.StudentViewNotes = StudentViewNotes;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notes_layout, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        NotesModel notes = viewNotes.get(position);
        Log.d("TAG1", "onBindViewHolder: " + notes);
        holder.textView.setText(notes.getName());
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Marking Completed");
                progressDialog.setTitle("Completed...");
                progressDialog.show();
//                markAsDone(notes);

                if (StudentViewNotes) {
                    ((StudentViewNotes) context).callPdfUrl(notes.getUrl());
                } else {
                    ((ViewAssignment) context).callPdfUrl(notes.getUrl());
                }

            }
        });
    }

    private void markAsDone(NotesModel model) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = firebaseUser.getUid();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Done").child(model.getName());
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("id", userId);
        hashMap.put("studentName", studentName);
        hashMap.put("name", model.getName());
        reference.push().setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                if (task.isSuccessful()) {
                    Toast.makeText(context, "Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return viewNotes.size();
    }

    class viewholder extends RecyclerView.ViewHolder {

        TextView textView;
//        Button btn_markAsDone;

        public viewholder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.textview);
//            btn_markAsDone = itemView.findViewById(R.id.btnMarkAsDone);
        }
    }
}
