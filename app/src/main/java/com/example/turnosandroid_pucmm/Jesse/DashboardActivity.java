package com.example.turnosandroid_pucmm.Jesse;
/**
 * @file DashboardActivity.java
 * @brief Fuente del Dashboard Activity.
 */

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.turnosandroid_pucmm.Alexander.LoginActivity;
import com.example.turnosandroid_pucmm.Javier.CompanyDetailsActivity;
import com.example.turnosandroid_pucmm.Javier.ShowTicketInfoActivity;
import com.example.turnosandroid_pucmm.JuanLuis.AboutActivity;
import com.example.turnosandroid_pucmm.JuanLuis.UserProfileActivity;
import com.example.turnosandroid_pucmm.Models.CompanyId;
import com.example.turnosandroid_pucmm.Models.Office;
import com.example.turnosandroid_pucmm.R;
import com.example.turnosandroid_pucmm.Robert.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Clase enlazada a la vista del Dashboard para la aplicación de Turnos.
 */
public class DashboardActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    public static final String SHARED_PREFS = "sharedPrefs";

    //Flag para identificar si existe una instancia activa de este activity.
    public static boolean isTicketActive;

    //Company for testing
    private List<CompanyId> mCompanies;
    private List<CompanyId> mCompaniesTemp;

    //Firebase instance
    private FirebaseFirestore mFirestore;
    private FirebaseAuth mAuth;

    //Tabla que asocia el ID de una sucursal con el ID de la compañía a la que pertenece.
    private HashMap<String, String> officeCompanyLinker;

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

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Enlaces al layout.
        setContentView(R.layout.activity_dashboard);
        toolbar = findViewById(R.id.idToolbar);
        recycler = findViewById(R.id.recyclerId);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);

        //Muestra el ActionBar
        setSupportActionBar(toolbar);

        //Inicialización del layout manager
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        //Obteniendo instancia de la base de datos.
        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        //Inicialización de atributos.
        mCompanies = new ArrayList<>();
        officeCompanyLinker = new HashMap<>();

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

        if (sharedPreferences != null)
            isTicketActive = sharedPreferences.getBoolean("isActive", false);

        //Obtención de la data para mostrar.

        if (mAuth.getCurrentUser() == null) {
            fetchGuestUserData();
        } else {
            fetchLoggedInData();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);

        if (mAuth.getCurrentUser() == null) {
            Log.d("Dash", "user == null");
            MenuItem profile = menu.findItem(R.id.idProfile);
            MenuItem logout = menu.findItem(R.id.logout);
            profile.setVisible(false);
            logout.setVisible(false);
        } else {
            MenuItem login = menu.findItem(R.id.login);
            login.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.idSearch:
                search();
                return true;
            case R.id.idProfile:
                Intent intent = new Intent(this, UserProfileActivity.class);
                startActivity(intent);
                return true;
            case R.id.idFilter:
                //filter();
                return true;
            case R.id.idAbout:
                Intent intent2 = new Intent(this, AboutActivity.class);
                startActivity(intent2);
                return true;
            case R.id.logout:
                mAuth.signOut();
                Intent goToLogin = new Intent(this, LoginActivity.class);
                startActivity(goToLogin);
                finish();
                return true;
            case R.id.login:
                // login because the user is "guest user"
                Intent goToLogin2 = new Intent(this, LoginActivity.class);
                startActivity(goToLogin2);
                finish();
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
    public void fetchLoggedInData() {

        mFirestore.collection(Util.COLLECTION_COMPANIES)
                .orderBy("name")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                company = document.toObject(CompanyId.class); //Convirtiendo el documento al objeto modelo
                                company.setId(document.getId()); //Colocando el ID de la empresa.

                                //Llena la tabla de asociación entre ID de la sucursal con ID de la compañía.
                                for (int i = 0; i < company.getOffices().size(); i++)
                                    officeCompanyLinker.put(company.getOffices().get(i).getId(), company.getId());

                                //Añade la compañía a la lista.
                                mCompanies.add(company);
                            }

                            mCompaniesTemp = mCompanies;

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
                                    if (isTicketActive)
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

    public void fetchGuestUserData() {

        mFirestore.collection(Util.COLLECTION_COMPANIES)
                .whereEqualTo("acceptGuest", true)
                .orderBy("name")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                company = document.toObject(CompanyId.class); //Convirtiendo el documento al objeto modelo
                                company.setId(document.getId()); //Colocando el ID de la empresa.

                                //Llena la tabla de asociación entre ID de la sucursal con ID de la compañía.
                                for (int i = 0; i < company.getOffices().size(); i++)
                                    officeCompanyLinker.put(company.getOffices().get(i).getId(), company.getId());

                                //Añade la compañía a la lista.
                                mCompanies.add(company);
                            }

                            mCompaniesTemp = mCompanies;


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
                                    if (isTicketActive)
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
     *
     * @param v View
     */
    public void goToCompanyDetails(View v) {
        selectedOffice = adapter.getListOffices().get
                (recycler.getChildAdapterPosition(v));
        Intent intent = new Intent(DashboardActivity.this, CompanyDetailsActivity.class);
        String officeID = selectedOffice.getId();
        intent.putExtra("officeId", selectedOffice.getId());
        intent.putExtra("companyId", officeCompanyLinker.get(officeID));
        startActivity(intent);
    }

    /**
     * Traslado a la vista de ShowTicketInfoActivity.
     *
     * @param v View.
     */
    public void goToShowTickedInfo(View v) {
        Intent intent = new Intent(DashboardActivity.this, ShowTicketInfoActivity.class);
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadRefreshData();
            }
        }, 1000);
    }

    private void loadRefreshData() {
        mCompanies.clear();
        adapter.notifyDataSetChanged();

        swipeRefreshLayout.setRefreshing(true);
        if (mAuth.getCurrentUser() == null) {
            fetchGuestUserData();
        } else {
            fetchLoggedInData();
        }
        swipeRefreshLayout.setRefreshing(false);
    }

    private void search() {
        // OUTTER
        final AlertDialog dialog;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_search, null);
        dialogBuilder.setView(dialogView);

        final EditText nameEditText = dialogView.findViewById(R.id.companyNameEditText);
        Button searchButton = dialogView.findViewById(R.id.searchButton);
        Button resetButton = dialogView.findViewById(R.id.resetButton);

        dialog = dialogBuilder.create();
        dialog.show();

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // INNER
                dialog.dismiss();

                List<CompanyId> temp = new ArrayList<>();
                Log.d("mCompaniesTemp", String.valueOf(mCompaniesTemp.size()));

                // TODO: Hacer un like
                for (CompanyId company : mCompaniesTemp) {
                    if (company.getName().toLowerCase().equals(nameEditText.getText().toString().toLowerCase())) {
                        temp.add(company);
                    }
                }

                // altice, banco caribe, banco popular, burger king

                Log.d("temp", String.valueOf(temp.size()));
                Log.d("mCompaniesTemp", String.valueOf(mCompaniesTemp.size()));

                // mCompanies.clear();
                mCompanies = temp;
                adapter = new AdapterDatos(mCompanies, officeCompanyLinker);
                recycler.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // INNER
                dialog.dismiss();

                mCompanies = mCompaniesTemp;
                adapter = new AdapterDatos(mCompanies, officeCompanyLinker);
                recycler.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });
    }

    /*private void filter() {
        // OUTTER
        final AlertDialog dialog;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_search, null);
        dialogBuilder.setView(dialogView);

        final Spinner serviceSpinner = dialogView.findViewById(R.id.companyNameEditText);
        Button resetButton = dialogView.findViewById(R.id.resetButton);

        // inyectar el arreglo de servicios al dropdown

        dialog = dialogBuilder.create();
        dialog.show();

        // DROPDOWN
        // String servicers[ ] =Transporte, Transporte2, Transporte3, Transporte4;

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // INNER
                dialog.dismiss();

                List<CompanyId> temp = new ArrayList<>();
                Log.d("mCompaniesTemp", String.valueOf(mCompaniesTemp.size()));

                // TODO: Hacer un like
                for (CompanyId company : mCompaniesTemp) {
                    if (company.getName().toLowerCase().equals(nameEditText.getText().toString().toLowerCase())) {
                        temp.add(company);
                    }
                }

                // altice, banco caribe, banco popular, burger king

                Log.d("temp", String.valueOf(temp.size()));
                Log.d("mCompaniesTemp", String.valueOf(mCompaniesTemp.size()));

                // mCompanies.clear();
                mCompanies = temp;
                adapter = new AdapterDatos(mCompanies, officeCompanyLinker);
                recycler.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // INNER
                dialog.dismiss();

                mCompanies = mCompaniesTemp;
                adapter = new AdapterDatos(mCompanies, officeCompanyLinker);
                recycler.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });
    }*/
}