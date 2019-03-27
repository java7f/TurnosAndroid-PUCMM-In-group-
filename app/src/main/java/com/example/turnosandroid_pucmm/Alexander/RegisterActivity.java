package com.example.turnosandroid_pucmm.Alexander;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.turnosandroid_pucmm.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText firstNameEditText, lastNameEditText, emailAddressEditText, passwordEditText;
    private Button registerButton;

    FirebaseAuth auth;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Init views
        firstNameEditText = findViewById(R.id.firstNameEditText);
        firstNameEditText = findViewById(R.id.firstNameEditText);
        firstNameEditText = findViewById(R.id.firstNameEditText);
        firstNameEditText = findViewById(R.id.firstNameEditText);
        firstNameEditText = findViewById(R.id.firstNameEditText);

        // Click listener
        registerButton.setOnClickListener(this);

        //  Auth
        auth = FirebaseAuth.getInstance();

        // Firestore
        firestore = FirebaseFirestore.getInstance();
    }

    @Override
    public void onClick(View v) {
        if (v == registerButton) register();
    }

    private void register() {
        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getTransitionName().toString().trim();
        String email = emailAddressEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        HashMap<String, Object> roles = new HashMap<>();
        roles.put("administrator", false);
        roles.put("moderator", false);
        roles.put("client", true);

        final HashMap<String, Object> user = new HashMap<>();
        user.put("firstName", firstName);
        user.put("lastName", lastName);
        user.put("emailAddress", email);
        user.put("roles", roles);

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    FirebaseUser userAuth = auth.getCurrentUser();
                    String id = userAuth.getUid();

                    firestore.collection("users")
                            .document(id)
                            .set(user)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        auth.signOut();
                                        // intent login///////.
                                        // int intent = new int
                                        // startact
                                    } else{
                                        // mostrar mensaje de que no se completo
                                        Toast.makeText(RegisterActivity.this, "no se pudo registrar", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                } else {
                    // diplei mesaje k no se crió
                    Toast.makeText(RegisterActivity.this, "no se pudo crear el crear la cuenta", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}
