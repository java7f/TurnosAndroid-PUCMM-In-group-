package com.example.turnosandroid_pucmm.JuanLuis;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.turnosandroid_pucmm.R;
import com.example.turnosandroid_pucmm.Models.UserId;
import com.example.turnosandroid_pucmm.Models.User;



public class UserProfileActivity extends AppCompatActivity {


    Toolbar toolbar;
    private static final String TAG = "UserProfileActivity";

    TextView idNombre;
    TextView idEmail;
    TextView idRol;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        toolbar = findViewById(R.id.idToolbarUserProfile);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        idNombre = findViewById(R.id.editTxtNombre);
        idEmail = findViewById(R.id.editTextEmail);
        idRol = findViewById(R.id.editTextRol);





    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home){
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
