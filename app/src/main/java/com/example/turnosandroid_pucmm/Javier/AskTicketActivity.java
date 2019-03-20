package com.example.turnosandroid_pucmm.Javier;

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

public class AskTicketActivity extends AppCompatActivity {

    /**
     * Lista que contendrá los servicios según la sucursal.
     */
    ListView servicesList;

    /**
     * Adaptador.
     */
    ArrayAdapter adapter;

    CompanyId mCompany;
    Office mOffice;

    List<Service> services;
    List<String> servicesName;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_ticket);
        setTitle("Seleccione el servicio");

        servicesName = new ArrayList<>();
        intent =  getIntent();

        mCompany = intent.getParcelableExtra("company");
        mOffice = intent.getParcelableExtra("office");
        services =  mCompany.getServices();

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

    public void selectSchedule(int position){
        Intent goToSchedule = new Intent(this, PickTypeOfTurnActivity.class);
        goToSchedule.putExtra("company", mCompany);
        goToSchedule.putExtra("office", mOffice);
        goToSchedule.putExtra("service", servicesList.getItemAtPosition(position).toString());
        startActivity(goToSchedule);
    }
}
