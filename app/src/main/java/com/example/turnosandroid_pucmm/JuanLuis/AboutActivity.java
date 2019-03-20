package com.example.turnosandroid_pucmm.JuanLuis;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.turnosandroid_pucmm.R;

public class AboutActivity extends AppCompatActivity {

    TextView descripcion;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        getSupportActionBar().setTitle("contactenos");

        button = (Button) findViewById(R.id.contactenos);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openContactenos();
            }
        });


        descripcion = findViewById(R.id.descripcion);
        descripcion.setText("Turnos es una aplicación creada"
                + "por un conjunto de estudiantes de"
                + "la PUCMM para la materia Ingeniería de Software."
                + "El grupo está conformado por: Javier Falcón,"
                + "Jesse Peña, Alexander Schobel,"
                + "Robert Gómez y Juan Luis Mejía.");
    }

    public void openContactenos(){

        //Intent intent = new Intent(this, ContactenosActivity.class);
        //startActivity(intent);

    }


}




