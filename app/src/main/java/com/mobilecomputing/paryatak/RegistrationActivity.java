package com.mobilecomputing.paryatak;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.UploadTask;
import com.hbb20.CountryCodePicker;

import es.dmoral.toasty.Toasty;

public class RegistrationActivity extends AppCompatActivity {

    private ImageView profilePictureIV;
    private EditText usernameET;
    private CountryCodePicker ccp;
    private EditText numberET;
    private EditText handleET;

    private int REQUEST_CODE = 777;
    private Bitmap bitmap;
    private String ext;

    private MaterialDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        profilePictureIV = (ImageView) findViewById(R.id.profilePic);
        usernameET = (EditText) findViewById(R.id.username);
        ccp = (CountryCodePicker) findViewById(R.id.countryCodePicker);
        numberET = (EditText) findViewById(R.id.number);
        handleET = (EditText) findViewById(R.id.handle);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null && user.getDisplayName() != null)
            usernameET.setText(user.getDisplayName());

        if(user != null && user.getPhotoUrl() != null) {
            Glide.with(this)
                    .asBitmap()
                    .load(user.getPhotoUrl().toString())
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            bitmap = resource;
                            ext = "JPG";
                            profilePictureIV.setImageBitmap(resource);
                        }
                    });
        }
        else {
            bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.user);
            ext = "PNG";
        }
    }

    public void showProgressDialog(String title, String content) {
        dialog = new MaterialDialog.Builder(this)
                .contentColor(getResources().getColor(R.color.grey))
                .backgroundColor(getResources().getColor(R.color.white))
                .titleColor(getResources().getColor(R.color.grey))
                .widgetColorRes(R.color.grey)
                .backgroundColorRes(R.color.white)
                .title(title)
                .content(content)
                .progress(true, 0)
                .build();

        dialog.show();
    }


    public void pickAndUploadImage(View view) {
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
                profilePictureIV.setImageBitmap(bitmap);

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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void Complete(View view) {
        final String username = usernameET.getText().toString();
        final String number = numberET.getText().toString();
        final String country = ccp.getSelectedCountryName();
        final String handle = handleET.getText().toString();

        if(username.length() == 0 || number.length() != 10 || handle.length() <= 1 || !handle.startsWith("@")) {
            if(username.length() == 0)
                Toasty.warning(getApplicationContext(), "Invalid Username.", Toast.LENGTH_LONG, true).show();
            else if(number.length() != 10)
                Toasty.warning(getApplicationContext(), "Invalid Phone Number.", Toast.LENGTH_LONG, true).show();
            else
                Toasty.warning(getApplicationContext(), "Invalid Handle.", Toast.LENGTH_LONG, true).show();

            return;
        }

        showProgressDialog("Registering..", "Please Wait");

        UploadImageService uploadImageService = new UploadImageService(null);
        uploadImageService.uploadImage(bitmap, ext)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        final UserModal user = new UserModal(taskSnapshot.getDownloadUrl().toString(), username, country, number,true, null, null, handle);

                        FirestoreService firestoreService = FirestoreService.getInstance();
                        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

                        firebaseFirestore.collection("users")
                                .document(FirestoreService.getUserDocumentID())
                                .set(user, SetOptions.merge())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Intent searchIntent = new Intent(RegistrationActivity.this, HomeActivity.class);
                                        Toasty.success(getApplicationContext(), "Registration Complete.", Toast.LENGTH_LONG, true).show();
                                        startActivity(searchIntent);
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toasty.error(getApplicationContext(), "Registration Failed. " + e.getLocalizedMessage(), Toast.LENGTH_LONG, true).show();
//                                       dialog.dismiss();
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toasty.error(getApplicationContext(), "Registration Failed. " + e.getLocalizedMessage(), Toast.LENGTH_LONG, true).show();
//                        dialog.dismiss();
                    }
                });
    }
}