package com.example.turnosandroid_pucmm.Javier;

/**
 * @file AskTicketActivity.java
 * @brief Fuente del activity AskTicket.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.turnosandroid_pucmm.Models.CompanyId;
import com.example.turnosandroid_pucmm.Models.Office;
import com.example.turnosandroid_pucmm.Models.Service;
import com.example.turnosandroid_pucmm.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase enlazada al layout de seleccionar el servicio para un turno.
 */
public class AskTicketActivity extends AppCompatActivity {

    public static Activity ata;

    /**
     * Lista que contendrá los servicios según la sucursal.
     */
    ListView servicesList;

    /**
     * Adaptador.
     */
    ArrayAdapter adapter;

    /**
     * Compañía y sucursal seleccionada.
     */
    CompanyId mCompany;
    Office mOffice;

    /**
     * Lista de servicios presentes en la sucursal.
     */
    List<Service> services;

    /**
     * Lista con los nombres de los servicios que utilizará
     * el adapter del ListView.
     */
    List<String> servicesName;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ata = this;
        setContentView(R.layout.activity_ask_ticket);

        //Título del toolbar.
        setTitle("Seleccione el servicio");

        servicesName = new ArrayList<>();
        intent =  getIntent();

        //Obtención de la compañía, sucursal y la lista de servicios.
        mCompany = intent.getParcelableExtra("company");
        mOffice = intent.getParcelableExtra("office");
        services =  mOffice.getServices();

        for(Service service : services)
            servicesName.add(service.getName());


        //Init adapter
        adapter = new ArrayAdapter<String>(this, R.layout.list_label, servicesName);

        //Linking to list
        servicesList = findViewById(R.id.services_list);

        //Set adapter
        servicesList.setAdapter(adapter);


        servicesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectSchedule(i);
            }
        });

    }

    /**
     * Traslado al activity de seleccionar el tipo de turno.
     * @param position Posición del item seleccionado en la lista.
     */
    public void selectSchedule(int position){
        Intent goToSchedule = new Intent(this, PickTypeOfTurnActivity.class);
        goToSchedule.putExtra("company", mCompany);
        goToSchedule.putExtra("office", mOffice);
        goToSchedule.putExtra("service", servicesList.getItemAtPosition(position).toString());
        startActivity(goToSchedule);
    }
}
