package com.mobilecomputing.paryatak;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class VLogsActivity extends AppCompatActivity {

    private int REQUEST_CODE = 777;
    private Bitmap bitmap;
    private String ext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vlogs);

        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);

                if(selectedImage != null) {
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String filePath = cursor.getString(columnIndex);
                        ext = filePath.substring(filePath.lastIndexOf(".") + 1);
                        ext = ext.toUpperCase();
                        cursor.close();
                    }
                }

                final String Time = String.valueOf(System.currentTimeMillis());
                final String PostID = FirebaseAuth.getInstance().getCurrentUser().getUid() + " " + Time;
                UploadImageService uploadImageService = new UploadImageService(PostID);
                uploadImageService.uploadImage(bitmap, ext)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Log.i("INFO ", String.valueOf(taskSnapshot.getDownloadUrl()));

                                Post post = new Post(PostID, Time, "Content " + Time, taskSnapshot.getDownloadUrl().toString());
                                FirestoreService firestoreService = FirestoreService.getInstance();
                                firestoreService.PublishPost(post)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Toast.makeText(getApplicationContext(), "Published Successfully", Toast.LENGTH_LONG).show();
                                                getAllPosts();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getApplicationContext(), "Not Published", Toast.LENGTH_LONG).show();
                                            }
                                        });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "Image Upload Error", Toast.LENGTH_LONG).show();
                            }
                        });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void getAllPosts() {
        FirestoreService.getInstance().getPosts()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                        List<DocumentSnapshot> documents = documentSnapshots.getDocuments();
                        Map<String, Post> posts = new HashMap<>();

                        for(DocumentSnapshot snapshot : documents) {
                            Map<String, Object> data = snapshot.getData();
                            Post post = Post.snapshotToPost(data);
                            posts.put(snapshot.getId(), post);
                            // Log.i("INFO ", String.valueOf(question.getQuestionID()) + " " + question.getUpvotes());
                        }

                        boolean first = true;
                        for (Map.Entry<String, Post> entry : posts.entrySet()) {
                            // if(first)
                            //    UpdatePost();
                            // first = false;

                            Log.i("INFO ", entry.getKey() + " " + entry.getValue().getPostID());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toasty.error(getApplicationContext(), "Some error occurred while fetching posts.", Toast.LENGTH_LONG).show();

                    }
                });
    }
}
