package com.example.turnosandroid_pucmm.Javier;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.turnosandroid_pucmm.Models.Company;
import com.example.turnosandroid_pucmm.Models.Membership;
import com.example.turnosandroid_pucmm.Models.Office;
import com.example.turnosandroid_pucmm.R;

import java.util.ArrayList;
import java.util.List;

public class PickTypeOfTurnActivity extends AppCompatActivity {

    /**
     * Lista que contendrá los horarios disponibles.
     */
    ListView turnType;

    /**
     * Adaptador.
     */
    ArrayAdapter adapter;

    List<String> queues;

    Company mCompany;
    Office mOffice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_type_of_turn);

        queues = new ArrayList<>();

        Intent intent = getIntent();

        mCompany = intent.getParcelableExtra("company");
        mOffice = intent.getParcelableExtra("office");

        boolean worksWithMemberships = mOffice.getHasStationsForMemberships(),
                worksWithPreferentials = mOffice.getHasStationsForPreferential();

        getTurnOptions(worksWithMemberships,worksWithPreferentials);

        //Init adapter
        adapter = new ArrayAdapter<String>(this, R.layout.list_label, queues);

        //Linking to list
        turnType = findViewById(R.id.turnType_list);

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

        Intent goToCompany = new Intent(this, CompanyDetailsActivity.class);
        goToCompany.putExtra("hide", 1);
        goToCompany.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(goToCompany);
    }

    private void getTurnOptions(boolean worksWithMemberships, boolean worksWithPreferentials)
    {

        List<Membership> memberships = mCompany.getMemberships();

        queues.add("Turno General");

        if(worksWithPreferentials)
            queues.add("Turno Preferencial");

        if(worksWithMemberships)
        {
            for(Membership membership : memberships)
                queues.add("Membresía " + membership.getName());
        }
    }
}
