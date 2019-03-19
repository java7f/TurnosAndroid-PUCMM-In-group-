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
import com.example.turnosandroid_pucmm.Models.CompanyId;
import com.example.turnosandroid_pucmm.Models.Office;
import com.example.turnosandroid_pucmm.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class DashboardActivity extends AppCompatActivity {

    //Company for testing
    private List<Company> mCompanies;

    //Firebase instance
    private FirebaseFirestore mFirestore;
    private HashMap<String,String> officeCompanyLinker;
    private static final String TAG = "CompanyDetailsActivity";

    List<Office> listDatos;
    RecyclerView recycler;
    Toolbar toolbar;
    CompanyId company;
    AdapterDatos adapter;
    Office selectedOffice;

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
        officeCompanyLinker = new HashMap<>();


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
                                company = document.toObject(CompanyId.class);
                                company.setId(document.getId());

                                for(int i = 0; i < company.getOffices().size(); i++)
                                    officeCompanyLinker.put(company.getOffices().get(i).getId(), company.getId());

                                mCompanies.add(company);
                            }

                            adapter = new AdapterDatos(mCompanies);
                            recycler.setAdapter(adapter);

                            adapter.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    selectedOffice = adapter.getListOffices().get
                                            (recycler.getChildAdapterPosition(v));
                                    Intent intent = new Intent(DashboardActivity.this, CompanyDetailsActivity.class);
                                    String officeID = selectedOffice.getId();
                                    intent.putExtra("officeId", officeID);
                                    intent.putExtra("companyId", officeCompanyLinker.get(officeID));
                                    startActivity(intent);
                                }
                            });


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