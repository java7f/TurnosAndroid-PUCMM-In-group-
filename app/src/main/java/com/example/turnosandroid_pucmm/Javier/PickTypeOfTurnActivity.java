package com.example.turnosandroid_pucmm.Javier;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.turnosandroid_pucmm.R;

public class PickTypeOfTurnActivity extends AppCompatActivity {

    /**
     * Lista que contendrá los horarios disponibles.
     */
    ListView turnType;

    /**
     * Adaptador.
     */
    ArrayAdapter adapter;

    String[] queues= {"Turno General", "Turno Preferencial", "Membresía Gold", "Membresía Platinum"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_type_of_turn);

        //Init adapter
        adapter = new ArrayAdapter<String>(this, R.layout.list_label, queues);

        //Linking to list
        turnType = (ListView) findViewById(R.id.turnType_list);

        //Set adapter
        turnType.setAdapter(adapter);

        turnType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                goToCompany();
            }
        });


    }

    public void goToCompany(){
        Intent intent = getIntent();
        Intent goToCompany = new Intent(this, CompanyDetailsActivity.class);
        goToCompany.putExtra("hide", intent.getStringExtra("hide"));
        startActivity(goToCompany);
    }
}
