package com.example.tuitionnotesofthestudent.tutor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tuitionnotesofthestudent.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddNotes extends AppCompatActivity {
    EditText editText;
    Button btn, btn_selectPdf;

    StorageReference storageReference;
    DatabaseReference databaseReference;

    String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);

        editText = findViewById(R.id.editext);
        btn = findViewById(R.id.btn);
        btn_selectPdf = findViewById(R.id.btn_selectPdf);

        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        btn.setEnabled(false);
        btn_selectPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPdf();
            }
        });

        Intent intent = getIntent();
        id = intent.getStringExtra("userId");

    }

    private void selectPdf() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "PDF FILE SELECT "), 12);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 12 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            btn.setEnabled(true);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = editText.getText().toString();
                    if (name.isEmpty()) {
                        editText.setError("Empty name");
                        return;
                    }
                    uploadFileToFirebase(data.getData(), name);
                }
            });
        }
    }

    private void uploadFileToFirebase(Uri data, String name) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("File is loading...");
        progressDialog.show();
        StorageReference reference = storageReference.child("upload" + System.currentTimeMillis() + ".pdf");

        reference.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isComplete()) ;
                        Uri uri = uriTask.getResult();

                        putPDF putPDF = new putPDF(name, uri.toString());
                        databaseReference.child("notes").child(id).push().setValue(putPDF);

                        Toast.makeText(AddNotes.this, "Notes Uploaded", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();


                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                progressDialog.setMessage("File Uploaded.." + (int) progress + "%");
            }
        });

    }
}