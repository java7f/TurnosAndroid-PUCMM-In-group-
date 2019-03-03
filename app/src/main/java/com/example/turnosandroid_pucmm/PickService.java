package com.example.turnosandroid_pucmm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class PickService extends AppCompatActivity {

    /**
     * Lista que contendrá los servicios según la sucursal.
     */
    ListView servicesList;

    /**
     * Adapter para el View.
     */
    ArrayAdapter adapter;

    /**
     * Test de serivicios.
     */
    String[] services= {"Impresión", "Fotocopia", "Diseño personalizado"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_service);
        setTitle("Seleccione el servicio");


        //Init adapter
        adapter = new ArrayAdapter<String>(this, R.layout.activity_list_label, services);

        //Linking to list
        servicesList= (ListView) findViewById(R.id.services_list);

        //Set adapter
        servicesList.setAdapter(adapter);


        servicesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectSchedule();
            }
        });

    }

    public void selectSchedule(){
        Intent intent = getIntent();
        Intent goToSchedule = new Intent(this, PickSchedule.class);
        goToSchedule.putExtra("hide", intent.getStringExtra("hide"));
        startActivity(goToSchedule);
    }
}
