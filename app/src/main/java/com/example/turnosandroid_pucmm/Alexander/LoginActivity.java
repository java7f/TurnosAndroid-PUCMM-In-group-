package com.example.turnosandroid_pucmm.Alexander;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.turnosandroid_pucmm.Jesse.DashboardActivity;
import com.example.turnosandroid_pucmm.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    private Button btnSignup, btnLogin, btnReset, btnGuest;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Asignacion de variables
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.passwordEditText);
        btnSignup = (Button) findViewById(R.id.btn_signup);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnReset = (Button) findViewById(R.id.btn_reset_password);
        btnGuest = (Button) findViewById(R.id.btn_guest);

        // FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();

        // FirebaseUser instance
        mUser = mAuth.getCurrentUser();

        initViews();
    }

    private void initViews() {
        //button que va hacia vista register
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        //button que va hacia la vista forgot password
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
            }
        });
        //button que va hacia vista dashboard como guest
        btnGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                intent.putExtra("isGuest", 1);
                startActivity(intent);
                finish();

            }
        });
//button para el login que va hacia dashboard y valida usuario
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString();
                final String password = inputPassword.getText().toString();

                //si se deja vacio el email, dar un mensaje que debe digitar su email
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Digite email", Toast.LENGTH_SHORT).show();
                    return;
                }

                //si el password esta vacio dar mensaje de que debe digitarlo
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Digite password!", Toast.LENGTH_SHORT).show();
                    return;
                }


                //authenticate user
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // Si el sign in funciona dar un mensaje(toast) que funciono

                                if (!task.isSuccessful()) {
                                    // Si hubo algun error

                                    Toast.makeText(LoginActivity.this, "Login failed.", Toast.LENGTH_LONG).show();
                                } else {
                                    //si no hubo error ir al dashboard
                                    Toast.makeText(LoginActivity.this, "Sesión iniciada.", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            user.reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();

                        if (user != null) {

                            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Log.d("reload", "guest user");
                        }
                    }
                }
            });
        }
    }
}
