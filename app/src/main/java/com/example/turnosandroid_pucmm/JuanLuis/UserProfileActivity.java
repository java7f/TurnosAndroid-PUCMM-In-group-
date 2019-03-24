package com.example.turnosandroid_pucmm.JuanLuis;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;


import com.example.turnosandroid_pucmm.R;
//import com.google.firebase.firestore.FirebaseFirestore;

public class UserProfileActivity extends AppCompatActivity {


    //Firebase instance
    //private FirebaseFirestore mFirestore;
    //private static final String TAG = "UserProfileActivity";

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        toolbar = findViewById(R.id.idToolbarUserProfile);
        setSupportActionBar(toolbar);

        //mFirestore = FirebaseFirestore.getInstance();


    }
}
