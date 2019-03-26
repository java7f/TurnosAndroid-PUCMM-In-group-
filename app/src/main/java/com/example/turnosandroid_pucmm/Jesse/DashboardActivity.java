package com.example.turnosandroid_pucmm.Jesse;
/**
 * @file DashboardActivity.java
 * @brief Fuente del Dashboard Activity.
 */

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
import android.view.MenuItem;
import android.view.View;

import com.example.turnosandroid_pucmm.Javier.CompanyDetailsActivity;
import com.example.turnosandroid_pucmm.Javier.ShowTicketInfoActivity;
import com.example.turnosandroid_pucmm.JuanLuis.AboutActivity;
import com.example.turnosandroid_pucmm.JuanLuis.UserProfileActivity;
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

/**
 * Clase enlazada a la vista del Dashboard para la aplicación de Turnos.
 */
public class DashboardActivity extends AppCompatActivity {

    //Company for testing
    private List<CompanyId> mCompanies;

    //Firebase instance
    private FirebaseFirestore mFirestore;

    //Tabla que asocia el ID de una sucursal con el ID de la compañía a la que pertenece.
    private HashMap<String,String> officeCompanyLinker;

    //Tag para el Log.
    private static final String TAG = "Dashboard Activity";

    //Recycler View utilizado.
    private RecyclerView recycler;

    //Toolbar a mostrar.
    private Toolbar toolbar;

    //Company extraída del documento en Firebase.
    private CompanyId company;

    //Adapter utilizado para el RecyclerView.
    private AdapterDatos adapter;

    //Sucursal seleccionada para pedir turno.
    private Office selectedOffice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Enlaces al layout.
        setContentView(R.layout.activity_dashboard);
        toolbar = findViewById(R.id.idToolbar);
        recycler = findViewById(R.id.recyclerId);

        //Muestra el ActionBar
        setSupportActionBar(toolbar);

        //Inicialización del layout manager
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        //Obteniendo instancia de la base de datos.
        mFirestore = FirebaseFirestore.getInstance();

        //Inicialización de atributos.
        mCompanies = new ArrayList<>();
        officeCompanyLinker = new HashMap<>();

        //Obtención de la data para mostrar.
        fetchData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.idSearch:
                return true;
            case R.id.idProfile:
                Intent intent = new Intent(this, UserProfileActivity.class);
                startActivity(intent);
                return true;
            case R.id.idFilter:
                return true;
            case R.id.idAbout:
                Intent intent2 = new Intent(this, AboutActivity.class);
                startActivity(intent2);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }


    /**
     * Función que obtiene la lista de compañías registradas
     * en la base de datos que utilizará el adapter para mostrar
     * las sucursales en pantalla.
     */

    public void fetchData() {

        mFirestore.collection("companies")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                company = document.toObject(CompanyId.class); //Convirtiendo el documento al objeto modelo
                                company.setId(document.getId()); //Colocando el ID de la empresa.

                                //Llena la tabla de asociación entre ID de la sucursal con ID de la compañía.
                                for(int i = 0; i < company.getOffices().size(); i++)
                                    officeCompanyLinker.put(company.getOffices().get(i).getId(), company.getId());

                                //Añade la compañía a la lista.
                                mCompanies.add(company);
                            }

                            //Inicialización del adapter
                            adapter = new AdapterDatos(mCompanies, officeCompanyLinker);
                            recycler.setAdapter(adapter);

                            //Maneja el item seleccionado en el dashboard.
                            adapter.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    /**
                                     * Si existe una instancia de ShowTicketInfoActivity significa que hay
                                     * un turno activo. Por lo tanto, se verifica si existe o no una instancia activa
                                     * para controlar cuál activity abrir. Si no existe instancia, se abre los detalles
                                     * de la compañía.
                                     */
                                    if(ShowTicketInfoActivity.isActive)
                                        goToShowTickedInfo(v);
                                    else
                                        goToCompanyDetails(v);
                                }
                            });

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    /**
     * Traslado a la vista de CompanyDetailsActivity.
     * @param v View
     */
    public void goToCompanyDetails(View v)
    {
        selectedOffice = adapter.getListOffices().get
                (recycler.getChildAdapterPosition(v));
        Intent intent = new Intent(DashboardActivity.this, CompanyDetailsActivity.class);
        String officeID = selectedOffice.getId();
        intent.putExtra("officeId", officeID);
        intent.putExtra("companyId", officeCompanyLinker.get(officeID));
        startActivity(intent);
    }

    /**
     * Traslado a la vista de ShowTicketInfoActivity.
     * @param v View.
     */
    public void goToShowTickedInfo(View v)
    {
        Intent intent = new Intent(DashboardActivity.this, ShowTicketInfoActivity.class);
        startActivity(intent);
    }
}