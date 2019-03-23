package com.example.turnosandroid_pucmm.Javier;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.turnosandroid_pucmm.Models.CompanyId;
import com.example.turnosandroid_pucmm.Models.Membership;
import com.example.turnosandroid_pucmm.Models.Office;
import com.example.turnosandroid_pucmm.Models.Role;
import com.example.turnosandroid_pucmm.Models.Turn;
import com.example.turnosandroid_pucmm.Models.UserId;
import com.example.turnosandroid_pucmm.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PickTypeOfTurnActivity extends AppCompatActivity {

    public static Activity ptota;
    /**
     * Lista que contendrá los horarios disponibles.
     */
    ListView turnType;

    /**
     * Adaptador.
     */
    ArrayAdapter adapter;

    List<String> queues;

    CompanyId mCompany;
    Office mOffice;
    //Firebase instance
    private FirebaseFirestore mFirestore;

    String serviceSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ptota = this;
        setContentView(R.layout.activity_pick_type_of_turn);

        queues = new ArrayList<>();
        mFirestore = FirebaseFirestore.getInstance();

        Intent intent = getIntent();

        mCompany = intent.getParcelableExtra("company");
        mOffice = intent.getParcelableExtra("office");
        serviceSelected = intent.getStringExtra("service");

        boolean worksWithMemberships = mOffice.getHasStationsForMemberships(),
                worksWithPreferentials = mOffice.getHasStationsForPreferential();

        getTurnOptions(worksWithMemberships,worksWithPreferentials);

        //Init adapter
        adapter = new ArrayAdapter<String>(this, R.layout.list_label, queues);

        //Linking to list
        turnType = findViewById(R.id.turnType_list);

        //Set adapter
        turnType.setAdapter(adapter);

        System.out.println("COMPANY ID = " + mCompany.getId());
        turnType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                registerTurn();
                goToCompany();
            }
        });


    }

    public void goToCompany(){

        Intent goToCompany = new Intent(this, ShowTicketInfoActivity.class);
        goToCompany.putExtra("company", mCompany);
        goToCompany.putExtra("office", mOffice);
        startActivity(goToCompany);
        finish();
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


    private void registerTurn()
    {
        Random random = new Random();
        int idTurn = random.nextInt();
        Timestamp createdAt = Timestamp.now();
        List<Office> offices = mCompany.getOffices();
        String userId = Integer.toString(random.nextInt());
        UserId user = new UserId(userId, "Juanito", "Perez", "hola@hotmail.com", new Role());

        Log.d("SERVICE: ", createdAt.toString());

        Turn newTurn = new Turn(Integer.toString(idTurn),createdAt, user ,serviceSelected, "01", false, false,"");

        mOffice.getTurns().add(newTurn);

        for(int i=0; i<offices.size(); i++)
        {
            if(offices.get(i).getId().equals(mOffice.getId()))
            {
                mCompany.getOffices().set(i, mOffice);
            }

        }

        DocumentReference document = mFirestore.collection("companies").document(mCompany.getId());
        document.update("offices", offices)
                .addOnSuccessListener(new OnSuccessListener< Void >() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(PickTypeOfTurnActivity.this, "Updated Successfully",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
