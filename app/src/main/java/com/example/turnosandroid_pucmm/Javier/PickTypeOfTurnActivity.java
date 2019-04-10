package com.example.turnosandroid_pucmm.Javier;

/**
 * @file PickTypeOfTurnActivity.java
 * @brief Fuente del activity PickTypeOfTurn.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.turnosandroid_pucmm.Models.CompanyId;
import com.example.turnosandroid_pucmm.Models.Membership;
import com.example.turnosandroid_pucmm.Models.Office;
import com.example.turnosandroid_pucmm.Models.Station;
import com.example.turnosandroid_pucmm.Models.Turn;
import com.example.turnosandroid_pucmm.Models.UserId;
import com.example.turnosandroid_pucmm.R;
import com.example.turnosandroid_pucmm.Robert.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Clase enlazada al layout de seleccionar el tipo de turno.
 */
public class PickTypeOfTurnActivity extends AppCompatActivity {

    private static final String PREFERENTIAL_TURN = "Turno Preferencial";

    //TAG para el log.
    private static final String TAG = "PickTypeOfTurnActivity";

    public static Activity ptota;
    /**
     * Lista que contendrá los turnos disponibles.
     */
    private ListView turnType;

    /**
     * Adaptador.
     */
    private ArrayAdapter adapter;

    /**
     * Tipos de turnos.
     */
    private List<String> queues;

    /**
     * Lista de estaciones posibles según el tipo de servicio seleccionado.
     */
    private List<String> stations;

    /**
     * Compañía y sucursal donde se registrará el turno.
     */
    private CompanyId mCompany;
    private Office mOffice;

    /**
     * Turno a crear.
     */
    private Turn newTurn;

    //Firebase instance
    private FirebaseFirestore mFirestore;

    //Autenticación.
    private FirebaseAuth mAuth;

    /**
     * Nombre del servicio seleccionado.
     */
    private String serviceSelected;

    /**
     * Tipo de turno seleccionado.
     */
    private String typeOfTurn;

    //Toolbar
    Toolbar toolbar;

    private boolean worksWithMemberships, worksWithPreferentials;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ptota = this;
        setContentView(R.layout.activity_pick_type_of_turn);
        toolbar = findViewById(R.id.idToolbarCompany);
        setSupportActionBar(toolbar);

        queues = new ArrayList<>();
        stations = new ArrayList<>();
        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();

        mCompany = intent.getParcelableExtra("company");
        mOffice = intent.getParcelableExtra("office");
        serviceSelected = intent.getStringExtra("service");

        //Se obtiene si la sucursal puede atender preferenciales y/o membresías.
        worksWithMemberships = mOffice.getHasStationsForMemberships();
        worksWithPreferentials = mOffice.getHasStationsForPreferential();

        //TODO: ¿Qué clase de turnos están disponibles en esta sucursal?
        getTurnOptions(worksWithMemberships, worksWithPreferentials);

        //Init adapter
        adapter = new ArrayAdapter<String>(this, R.layout.list_label, queues);

        //Linking to list
        turnType = findViewById(R.id.turnType_list);

        //Set adapter
        turnType.setAdapter(adapter);

        //Acción cuando se da click en un item de la lista.
        turnType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                typeOfTurn = turnType.getItemAtPosition(i).toString();
                registerTurn();
            }
        });


    }

    /**
     * Traslado al activity de mostrar información del ticket.
     */
    public void goToTicketInfo() {

        Intent goToCompany = new Intent(this, ShowTicketInfoActivity.class);
        goToCompany.putExtra("company", mCompany);
        goToCompany.putExtra("office", mOffice);
        goToCompany.putExtra("turn", newTurn);
        startActivity(goToCompany);
        finish();
    }

    /**
     * Llena la lista con los tipos de turnos.
     *
     * @param worksWithMemberships   ¿La sucursal trabaja con membresías?
     * @param worksWithPreferentials ¿La sucursal trabaja con clientes preferenciales?
     */
    private void getTurnOptions(boolean worksWithMemberships, boolean worksWithPreferentials) {

        List<Membership> memberships = mCompany.getMemberships();

        queues.add("Turno General");

        if (worksWithPreferentials)
            queues.add(PREFERENTIAL_TURN);

        if (worksWithMemberships) {
            for (Membership membership : memberships)
                queues.add("Membresía " + membership.getName());
        }
    }

    /**
     * Acción de registrar un turno en la base de datos.
     */
    private void registerTurn() {
        final FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            mFirestore.collection(Util.COLLECTION_USERS).document(user.getUid())
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {

                            // Convierte la data y la lleva al modelo de UserId
                            UserId userId = document.toObject(UserId.class);
                            userId.setId(document.getId());

                            Log.d("USER ID: ", user.getUid());
                            Log.d(TAG, userId.getFirstName());
                            Log.d(TAG, userId.getId());
                            getUserAndRegisterTurn(userId);

                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
        }
        else
        {
            getUserAndRegisterTurn(new UserId());
        }
    }

    /**
     * Acción de registrar un turno en la base de datos.
     */
    private void getUserAndRegisterTurn(UserId currentUser) {
        //Id random para el ticket. TODO: Restringir el rango.

        int idTurn = 1;

        //Si la cola no está vacía, agarra el último turno, toma su ID y súmale 1;
        if(mOffice.getTurns().size() != 0){
            String lastTurnId = mOffice.getTurns().get(mOffice.getTurns().size()-1).getId();
            idTurn = Integer.parseInt(lastTurnId) + 1;
        }

        //Tiempo de creación.
        Timestamp createdAt = Timestamp.now();

        //Lista de sucursales.
        List<Office> offices = mCompany.getOffices();


        //Creación del turno. //TODO: Realizar lógica de asignar a la estación correcta.
        newTurn = new Turn(Integer.toString(idTurn), createdAt, currentUser, serviceSelected, getStationForTurn(), false, false, "");

        //Añade el nuevo turno a la cola localmente.
        mOffice.getTurns().add(newTurn);


        /**
         * Se busca la sucursal y se reemplaza por ella misma con el nuevo turno.
         */
        for (int i = 0; i < offices.size(); i++) {
            if (offices.get(i).getId().equals(mOffice.getId())) {
                mCompany.getOffices().set(i, mOffice);
                break;
            }
        }

        /**
         * Update de las sucursales con el nuevo turno registrado.
         * TODO: Mostrar mensaje de que el registro fue exitoso.
         */
        DocumentReference document = mFirestore.collection("companies").document(mCompany.getId());
        document.update("offices", offices)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(PickTypeOfTurnActivity.this, "El turno fue registrado correctamente",
                                Toast.LENGTH_SHORT).show();
                    }
                });

        goToTicketInfo();
    }

    //TODO: Hacer la lógica de asignar una estación al turno.
    private String getStationForTurn() {

        Random random = new Random();

        Log.d("STATIONS NUMBER: ", Boolean.toString(isForPreferential(typeOfTurn)));

        for (Station station : mOffice.getStations()) {
            if (station.getTypeOfService().equals(serviceSelected)) {
                if (isForPreferential(typeOfTurn) && station.getIsForPreferentialAttention())
                    stations.add(station.getId());
                else if (isForMemberships(typeOfTurn) && station.getIsForMemberships())
                    stations.add(station.getId());
                else if (!isForMemberships(typeOfTurn) && !isForPreferential(typeOfTurn))
                    stations.add(station.getId());
            }
        }

        Log.d("STATIONS AFTER: ", Integer.toString(stations.size()));
        int station = random.nextInt(stations.size());
        return stations.get(station);
    }

    /**
     * Función que obtiene si el tipo de turno seleccionado es preferencial.
     */
    private boolean isForPreferential(String type) {
        return PREFERENTIAL_TURN.equals(type);
    }

    /**
     * Función que obtiene si el tipo de turno seleccionado es una membresía.
     */
    private boolean isForMemberships(String type) {
        List<Membership> memberships = mCompany.getMemberships();
        String membershipName;

        for (Membership membership : memberships) {
            membershipName = "Membresía " + membership.getName();
            if (type.equals(membershipName))
                return true;
        }

        return false;
    }
}
