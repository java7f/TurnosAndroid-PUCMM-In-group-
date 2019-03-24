package com.example.turnosandroid_pucmm.JuanLuis;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.example.turnosandroid_pucmm.R;

public class AboutActivity extends AppCompatActivity {

    TextView descripcion;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);


        toolbar = findViewById(R.id.idToolbarAbout);
        setSupportActionBar(toolbar);

        descripcion = findViewById(R.id.descripcion);
        descripcion.setText("Turnos es una aplicación creada "
                + "por un conjunto de estudiantes de "
                + "la PUCMM para la materia Ingeniería de Software. "
                + "El grupo está conformado por: Javier Falcón, "
                + "Jesse Peña, Alexander Schobel, "
                + "Robert Gómez y Juan Luis Mejía. "
                +  "Si esta teniendo algún error del "
                +  "cual no nos hemos percatado, por favor comunicarse "
                + "con nosotros al 809-535-0111. Le atenderemos en breve."
                + "  Gracias por su comprensión.");
    }




}




