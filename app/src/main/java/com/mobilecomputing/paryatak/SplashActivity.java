/* This activity will handle splash screen. Show random quote. + Fetch locations data and store in SearchData. */

package com.mobilecomputing.paryatak;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import es.dmoral.toasty.Toasty;

public class SplashActivity extends AppCompatActivity {

    private String[] quotes;
    private TextView quoteTV;
    private Random random;
    private static int SCREEN_TIMEOUT = 2500;
    private FirestoreService firestoreService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Set random quote
        random = new Random();
        quoteTV = (TextView) findViewById(R.id.quoteTV);
        quotes = getResources().getStringArray(R.array.quotes);
        int index = random.nextInt(quotes.length);
        quoteTV.setText(String.format("\"%s\"", quotes[index]));

        // Initialize FirestoreService
        firestoreService = FirestoreService.getInstance();

        // Configure Toasty
        Toasty.Config.getInstance().setTextSize(18).apply();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Fetch 1554 locations from FireStore
        final PlacesData searchData = PlacesData.getInstance();
        searchData.getPlacesFromFirestore()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                        storeSearchData(documentSnapshots);
                        startProcedures();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toasty.error(getApplicationContext(), e.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    // Function to fetch places
    public void storeSearchData(QuerySnapshot documentSnapshots) {
        List<DocumentSnapshot> documents = documentSnapshots.getDocuments();
        for(int i = 0; i < documentSnapshots.size(); ++i) {
            DocumentSnapshot documentSnapshot = documents.get(i);
            String City = documentSnapshot.getId();
            List<String> Places = new ArrayList<>();
            Map<String, Object> pairs = documentSnapshot.getData();
            for(int j = 0; j < pairs.size(); ++j) {
                int index = j + 1;
                if(index <= 9)
                    Places.add(String.valueOf(pairs.get("Place0" + index)));
                else
                    Places.add(String.valueOf(pairs.get("Place" + index)));
            }

            PlacesData.addCity(City, Places);
        }
    }

    // Check if user is logged in or not.
    // If not logged in, take him to AuthenticationActivity
    // Else if not registered, take him to Register Activity
    // Else, take him to search screen.
    public void startProcedures() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseUser user = mAuth.getCurrentUser();

                if(user == null) {
                    Intent authIntent = new Intent(SplashActivity.this, AuthenticationActivity.class);
                    startActivity(authIntent);
                }
                else {
                    // Get current user document in 'users' collection
                    firestoreService.getCurrentUser().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                    // If successful, save the document id.
                                    if (task.isSuccessful()) {
                                        for (DocumentSnapshot document : task.getResult()) {
                                            Map<String, Object> user = document.getData();
                                            FirestoreService.setUserDocumentID(document.getId());

                                            // Now, Proceed based on current user state.
                                            if(Boolean.valueOf(user.get("registered").toString()))
                                                gotoSearch(user.get("name").toString());
                                            else
                                                gotoRegistration("Welcome Back!!!");
                                        }
                                    }
                                }
                            });
                }
            }
        }, SCREEN_TIMEOUT);
    }

    /*** REGISTRATION INTENT ***/
    public void gotoRegistration(String message) {
        Intent intent = new Intent(SplashActivity.this, RegistrationActivity.class);
        Toasty.success(getApplicationContext(), message, Toast.LENGTH_LONG, true).show();
        startActivity(intent);
    }

    /*** SEARCH INTENT ***/
    public void gotoSearch(String name) {
        Intent intent = new Intent(SplashActivity.this, HomeActivity.class  );
        Toasty.success(getApplicationContext(), "Welcome back, " + name + "!", Toast.LENGTH_LONG, true).show();
        startActivity(intent);
    }
}