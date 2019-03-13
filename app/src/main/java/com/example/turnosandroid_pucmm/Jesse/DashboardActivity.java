package com.example.turnosandroid_pucmm.Jesse;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Toast;

import com.example.turnosandroid_pucmm.Models.Company;
import com.example.turnosandroid_pucmm.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


public class DashboardActivity extends AppCompatActivity {

    //Company for testing
    private Company mCompany;

    //Firebase instance
    private FirebaseFirestore mFirestore;
    private static final String TAG = "CompanyDetailsActivity";

    ArrayList<String> listDatos;
    RecyclerView recycler;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recycler = new RecyclerView(this);

        toolbar=findViewById(R.id.idToolbar);
        setSupportActionBar(toolbar);

        recycler=findViewById(R.id.recyclerId);
        recycler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        listDatos= new ArrayList<String>();

        for(int i=0; i<5; i++) {
            listDatos.add("Sucursal #" + i + " ");
        }
        AdapterDatos adapter = new AdapterDatos(listDatos);

        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),
                        "Selección: " + listDatos.get
                                (recycler.getChildAdapterPosition(v)), Toast.LENGTH_SHORT).show();
            }
        });
        recycler.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    private void fetchData() {
        mFirestore.collection("companies").document("wRjpAUyr25ZYiLtUL110")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        // Convierte la data y la lleva a tu modelo
                        Company tempCompany = document.toObject(Company.class);
                        mCompany = tempCompany;

                        Log.d(TAG,"DocumentSnapshot data: " + mCompany.getTicketCriteria());

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d( TAG,"get failed with ", task.getException());
                }
            }
        });
    }
}