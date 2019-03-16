package com.example.turnosandroid_pucmm.Jesse;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.example.turnosandroid_pucmm.Javier.CompanyDetailsActivity;
import com.example.turnosandroid_pucmm.Models.Company;
import com.example.turnosandroid_pucmm.Models.Office;
import com.example.turnosandroid_pucmm.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class DashboardActivity extends AppCompatActivity {

    //Company for testing
    private List<Company> mCompanies;

    //Firebase instance
    private FirebaseFirestore mFirestore;
    private static final String TAG = "CompanyDetailsActivity";

    List<Office> listDatos;
    RecyclerView recycler;
    Toolbar toolbar;
    Company company;
    AdapterDatos adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        toolbar = findViewById(R.id.idToolbar);
        setSupportActionBar(toolbar);

        recycler = findViewById(R.id.recyclerId);
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mFirestore = FirebaseFirestore.getInstance();
        mCompanies = new ArrayList<>();


/*
        for(int i=0; i<8; i++) {
            listDatos.add("Sucursal #" + i + " ");
        }
 */
        fetchData();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    public void fetchData() {

        mFirestore.collection("companies")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                mCompanies.add(document.toObject(Company.class));
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }

                            adapter = new AdapterDatos(mCompanies);

                            adapter.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(DashboardActivity.this, CompanyDetailsActivity.class);
                                    intent.putExtra("officeId","");
                                    startActivity(intent);
                                }
                            });

                            recycler.setAdapter(adapter);

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });


       /* mFirestore.collection("companies").document("wRjpAUyr25ZYiLtUL110")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        // Convierte la data y la lleva a tu modelo
                        mCompany = document.toObject(Company.class);
                        adapter = new AdapterDatos(mCompany);

                        adapter.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(DashboardActivity.this, CompanyDetailsActivity.class);
                                intent.putExtra("officeId","");
                                startActivity(intent);
                            }
                        });

                        recycler.setAdapter(adapter);


                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });*/
    }
}