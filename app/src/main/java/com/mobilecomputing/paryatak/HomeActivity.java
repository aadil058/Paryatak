package com.mobilecomputing.paryatak;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void GoToForum(View view) {
        Intent intent = new Intent(HomeActivity.this, ForumActivity.class);
        startActivity(intent);
    }

    public void GoToVlogs(View view) {
        Intent intent = new Intent(HomeActivity.this, VLogsActivity.class);
        startActivity(intent);
    }
}