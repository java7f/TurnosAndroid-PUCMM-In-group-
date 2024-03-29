package com.example.turnosandroid_pucmm.Jesse;
/**
 * @file DashboardActivity.java
 * @brief Fuente del Dashboard Activity.
 */

import android.content.DialogInterface;
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
import android.view.*;
import android.widget.*;
import com.example.turnosandroid_pucmm.Alexander.LoginActivity;
import com.example.turnosandroid_pucmm.Javier.CompanyDetailsActivity;
import com.example.turnosandroid_pucmm.Javier.ShowTicketInfoActivity;
import com.example.turnosandroid_pucmm.JuanLuis.AboutActivity;
import com.example.turnosandroid_pucmm.JuanLuis.UserProfileActivity;
import com.example.turnosandroid_pucmm.Models.Company;
import com.example.turnosandroid_pucmm.Models.CompanyId;
import com.example.turnosandroid_pucmm.Models.Office;
import com.example.turnosandroid_pucmm.Models.Turn;
import com.example.turnosandroid_pucmm.R;
import com.example.turnosandroid_pucmm.Robert.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;

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
    private static final String TAG = "DashboardActivity";

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
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //Inicialización del layout manager
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        //Obteniendo instancia de la base de datos.
        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        //Inicialización de atributos.
        mCompanies = new ArrayList<>();
        officeCompanyLinker = new HashMap<>();

        /**
         * Cambio #1
         * @Robert Gomez
         */
        checkIfTurnStillExits();

        /*SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

        if (sharedPreferences != null)
            isTicketActive = sharedPreferences.getBoolean("isActive", false)*/

        //Obtención de la data para mostrar.

        if (mAuth.getCurrentUser() == null) {
            fetchGuestUserData();
        } else {
            fetchLoggedInData();
        }

    }

    private void checkIfTurnStillExits() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

        if (sharedPreferences != null) {
            String companyId = sharedPreferences.getString("companyId", " ");
            final String officeId = sharedPreferences.getString("officeId", " ");
            final String turnId = sharedPreferences.getString("turnId", "");

            DocumentReference companyRef = mFirestore.collection(Util.COLLECTION_COMPANIES).document(companyId);
            companyRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();

                        if (document.exists()) {
                            Company company = document.toObject(Company.class);

                            for (Office office : company.getOffices()) {
                                if (office.getId().equals(officeId)){
                                    Log.d(TAG, "Paso 1. Oficina encontrada");

                                    boolean isFound = false;
                                    for (Turn turn : office.getTurns()) {
                                        if (turn.getId().equals(turnId)){
                                            isFound = true;
                                            Log.d(TAG, "Paso 2. Turno encontrado");
                                            goToShowTickedInfo();
                                        }
                                    }

                                    if (!isFound) {
                                        Log.d(TAG, "No existe turno, borrando de sharedP...");

                                        // Borrar de sharedPreferences
                                        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.clear();
                                        editor.commit();
                                    }
                                }
                            }

                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
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
                filter();
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
        checkIfTurnStillExits();
        startActivity(intent);
    }

    /**
     * Traslado a la vista de ShowTicketInfoActivity.
     */
    public void goToShowTickedInfo() {
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

    /**
     * Actualiza la data al hacer refresh.
     */
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

    /**
     * Función que busca una compañía.
     */
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
                    if (company.getName().toLowerCase().contains(nameEditText.getText().toString().toLowerCase())) {
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

    /**
     * Función que filtra las compañías según su servicio.
     */
    private void  filter() {
        // OUTTER
        final AlertDialog dialog;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_filter, null);
        dialogBuilder.setView(dialogView);

        final Spinner serviceSpinner = dialogView.findViewById(R.id.spinner1);
        //Button resetButton = dialogView.findViewById(R.id.resetButton);


        // inyectar el arreglo de servicios al dropdown
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.dropdown_filter, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        serviceSpinner.setAdapter(spinnerAdapter);

        dialogBuilder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (!serviceSpinner.getSelectedItem().toString().equals("Elija un servicio...")) {
                    // INNER
                    dialogInterface.dismiss();

                    List<CompanyId> temp = new ArrayList<>();
                    Log.d("mCompaniesTemp", String.valueOf(mCompaniesTemp.size()));

                    // TODO: Hacer un like
                    for (CompanyId company : mCompaniesTemp) {
                        if (company.getTypeOfService().toLowerCase().equals(serviceSpinner.getSelectedItem().toString().toLowerCase())) {
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

                    if(temp.size() == 0)
                    {
                        Toast.makeText(DashboardActivity.this, "No se encontró ningun resultado.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        dialog = dialogBuilder.create();
        dialog.show();

    }
}