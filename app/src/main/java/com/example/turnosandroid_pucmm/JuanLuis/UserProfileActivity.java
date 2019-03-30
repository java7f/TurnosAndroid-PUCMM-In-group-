package com.example.turnosandroid_pucmm.JuanLuis;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.turnosandroid_pucmm.Models.Role;
import com.example.turnosandroid_pucmm.Models.User;
import com.example.turnosandroid_pucmm.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserProfileActivity extends AppCompatActivity {


    private static final String TAG = "UserProfileActivity";
    Toolbar toolbar;
    TextView idNombre;
    TextView idEmail;
    TextView idRol;

    FirebaseAuth mAuth;
    FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        toolbar = findViewById(R.id.idToolbarUserProfile);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Views
        idNombre = findViewById(R.id.editTxtNombre);
        idEmail = findViewById(R.id.editTextEmail);
        idRol = findViewById(R.id.editTextRol);

        // Firebase
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        loadUserData();
    }

    private void loadUserData() {
        mFirestore.collection("users")
                .document(mAuth.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            User user = task.getResult().toObject(User.class);
                            Role role = user.getRoles();
                            idNombre.setText(user.getFirstName());
                            idEmail.setText(user.getLastName());
                            idRol.setText(
                                    (role.isAdministrator())
                                            ? "Administrador"
                                            : (role.isModerator()
                                            ? "Moderador"
                                            : "Cliente"));
                        } else {
                            Log.d(TAG, "loadUserData error");
                        }
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
