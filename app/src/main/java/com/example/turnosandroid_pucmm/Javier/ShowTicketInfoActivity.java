package com.example.turnosandroid_pucmm.Javier;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.example.turnosandroid_pucmm.Jesse.DashboardActivity;
import com.example.turnosandroid_pucmm.Models.CompanyId;
import com.example.turnosandroid_pucmm.Models.Office;
import com.example.turnosandroid_pucmm.Models.Turn;
import com.example.turnosandroid_pucmm.R;
import com.example.turnosandroid_pucmm.Robert.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.*;

import javax.annotation.Nullable;
import java.util.List;

public class ShowTicketInfoActivity extends AppCompatActivity {

    //Nombre asignado a SharedPreferences.
    public static final String SHARED_PREFS = "sharedPrefs";

    //Company
    private CompanyId mCompany;

    //Sucursal
    private Office mOffice;

    //IDs de la empresa y sucursal.
    private String companyId;
    private String officeId;

    //Firebase instance
    private FirebaseFirestore mFirestore;

    private static final String TAG = "ShowTicketInfoActivity";

    //Buttons
    private Button cancelTurn;

    //Text fields
    private TextView companyName, subsidiaryName, address, schedule, ticket, time, station;

    //Text from SharedPreferences
    private String compName, subName, addr, sched, waitingTime, turnId, stationId;

    //Toolbar
    private Toolbar toolbar;

    //Turno asignado.
    private Turn currentTurn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DashboardActivity.isTicketActive = true;

        //Enlaces con los layouts
        setContentView(R.layout.activity_show_ticket_info);
        toolbar = findViewById(R.id.idToolbarCompany);
        cancelTurn = findViewById(R.id.cancelTurn);
        companyName = findViewById(R.id.companyName);
        subsidiaryName = findViewById(R.id.subsidiaryName);
        address = findViewById(R.id.address);
        schedule = findViewById(R.id.schedule);
        ticket = findViewById(R.id.ticketNumber);
        time = findViewById(R.id.minutesLeft);
        station = findViewById(R.id.stationId);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        //mCompany = new CompanyId();

        //Instancia de la base de datos.
        mFirestore = FirebaseFirestore.getInstance();

        /**
         * Obteniendo la compañía, sucursal y el turno creado desde
         * el activity PickTypeOfTurn.
         */
        if (getIntent().getExtras() != null) {
            mCompany = intent.getParcelableExtra("company");
            mOffice = intent.getParcelableExtra("office");
            currentTurn = intent.getParcelableExtra("turn");
        }

        /**
         * Leer la empresa en firebase, buscar la oficina y buscar el turno id.
         * Luego, guardar la Empresa, Oficina y Turno localmente
         */

        /**
         * Si la compañía es nula, quiere decir que se viene directamente
         * desde el DashboardActivity porque ya hay un turno asignado,
         * por lo que se reestablece el estado anterior de la pantalla.
         * En caso contrario, se acaba de crear el turno, por lo que se
         * busca la data en la base de datos y se salva el estado actual.
         */
        if (mCompany == null) {
            loadData();
            updateViews();
            addListener();
        } else {
            //Finalización de los activities anteriores.
            CompanyDetailsActivity.cda.finish();
            AskTicketActivity.ata.finish();
            PickTypeOfTurnActivity.ptota.finish();

            //Se busca la data en la base de datos.
            companyId = mCompany.getId();
            officeId = mOffice.getId();

            //Click listener del botón de cancelar.
            cancelTurn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancelTurn(v);
                }
            });

            fetchDataListener();
        }

    }

    private void addListener() {
        mFirestore.collection(Util.COLLECTION_COMPANIES)
                .document(companyId)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                        if (documentSnapshot != null && documentSnapshot.exists()) {

                            CompanyId companyTemp = documentSnapshot.toObject(CompanyId.class);
                            mCompany = companyTemp;

                            for (Office office : mCompany.getOffices()) {
                                if (office.getId().equals(officeId)) {
                                    Log.d(TAG, "addListener | Paso 1. Oficina encontrada");
                                    mOffice = office;

                                    boolean isTurnFound = false;
                                    for (Turn turn : office.getTurns()) {
                                        if (turn.getId().equals(turnId)) {
                                            Log.d(TAG, "addListener | Paso 2. Turno encontrado");
                                            isTurnFound = true;
                                            currentTurn = turn;
                                            setOfficeDetails();
                                            break;
                                        }
                                    }

                                    // If the turn was deleted (not found)
                                    if (!isTurnFound) {
                                        Log.d(TAG, "addListener | Turno no encontrado, fue borrado desde la web");

                                        // Paso 1. Borrar sharedP
                                        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.clear();
                                        editor.commit();

                                        // Paso 2. Regresar a CompanyDetailsActivity
                                        Intent goToCompany = new Intent(ShowTicketInfoActivity.this, CompanyDetailsActivity.class);
                                        goToCompany.putExtra("companyId", companyId);
                                        goToCompany.putExtra("officeId", officeId);
                                        startActivity(goToCompany);
                                        finish();
                                        Toast.makeText(ShowTicketInfoActivity.this, "Turno cancelado",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                    }
                });
    }

    /**
     * Cancelación del turno y traslado al activity de detalles de compañía.
     */
    public void cancelTurn(View v) {

        /**
         * Existen 2 casos a la hora de cancelar un turno. El primero, es el caso
         * en el cual el usuario cancela el turno justo después de haberlo pedido.
         * Para ese entonces, la compañía no es nula. El caso 2 sucede cuando
         * se cierra la aplicación o cuando salimos de la pantalla de ShowTicketInfo.
         * Para este caso, es que se utiliza el SharedPreferences, que guarda el estado
         * en el que se quedó el activity antes de salir. En el caso 2, la compañía es nula,
         * por lo que hay que sacar la data de la base de datos antes de hacer la cancelación.
         */
        if (mCompany == null)
            getCompanyAndDeleteTurn();
        else
            deleteTurn();


        // log Paso 2.
    }

    /**
     * Si la compañía era nula, buscarla en la base de datos y borrar el turno.
     */
    public void getCompanyAndDeleteTurn() {


        mFirestore.collection("companies").document(companyId)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        // Convierte la data y la lleva a tu modelo
                        CompanyId tempCompany = document.toObject(CompanyId.class);
                        mCompany = tempCompany;
                        mCompany.setId(companyId);

                        //Se busca la sucursal en la que se pidió el turno.
                        for (Office office : mCompany.getOffices()) {
                            if (office.getId().equals(officeId))
                                mOffice = office;
                        }

                        //Finalmente, con los datos extraídos de la base de datos,
                        //se procede a eliminar el turno.
                        deleteTurn();
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    /**
     * Lógica de eliminar un turno.
     * Localmente, se busca el turno creado usando su ID dentro de la lista de turnos de la sucursal,
     * se borra y luego se actualiza la sucursal entera.
     */
    public void deleteTurn() {
        Log.d(TAG, "DeleteTurn()");

        if (currentTurn != null)
            turnId = currentTurn.getId();
        else {
            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
            turnId = sharedPreferences.getString("turnId", "");
        }

        Log.d(TAG, "turnId: " + turnId);

        List<Office> offices = mCompany.getOffices();
        List<Turn> turns = mOffice.getTurns();

        for (int i = 0; i < turns.size(); i++) {

            if (turns.get(i).getId().equals(turnId)) {

                Log.d(TAG, "Removiendo turno localmente...");
                turns.remove(i);
                break;

            }

        }

        for (int i = 0; i < offices.size(); i++) {

            if (offices.get(i).getId().equals(mOffice.getId())) {
                mCompany.getOffices().set(i, mOffice);
                break;
            }
        }

        DocumentReference companyRef = mFirestore.collection(Util.COLLECTION_COMPANIES).document(companyId);

        companyRef
                .update("offices", offices)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Paso 1 log.e()
                        Log.d(TAG, "Borrando turno de sharedP");
                        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.commit();

                        Intent goToCompany = new Intent(ShowTicketInfoActivity.this, CompanyDetailsActivity.class);
                        goToCompany.putExtra("companyId", companyId);
                        goToCompany.putExtra("officeId", officeId);
                        startActivity(goToCompany);
                        finish();
                        Toast.makeText(ShowTicketInfoActivity.this, "Turno cancelado",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Failed canceling turn", e);

                        Toast.makeText(ShowTicketInfoActivity.this, "Turno no se pudo cancelar",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Trae data de la base de datos.
     */
    private void fetchDataListener() {

        mFirestore.collection(Util.COLLECTION_COMPANIES)
                .document(companyId)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                        if (documentSnapshot != null && documentSnapshot.exists()) {
                            Log.d(TAG, "Data: " + documentSnapshot.getData());

                            // Convierte la data y la lleva a tu modelo
                            CompanyId tempCompany = documentSnapshot.toObject(CompanyId.class);
                            mCompany = tempCompany;
                            mCompany.setId(companyId);

                            for (Office office : mCompany.getOffices()) {
                                if (office.getId().equals(officeId)) {
                                    Log.d(TAG, "fetchDataListener | Paso 1. Oficina encontrada");
                                    mOffice = office;

                                    boolean isTurnFound = false;
                                    turnId  = currentTurn.getId();
                                    for (Turn turn : office.getTurns()) {
                                        if (turn.getId().equals(turnId)) {
                                            Log.d(TAG, "fetchDataListener | Paso 2. Turno encontrado");
                                            isTurnFound = true;
                                            currentTurn = turn;
                                            setOfficeDetails();

                                            //Salva la data en el SharedPreferences.
                                            saveData();
                                            break;
                                        }
                                    }

                                    // If the turn was deleted (not found)
                                    if (!isTurnFound) {
                                        Log.d(TAG, "fetchDataListener | Turno no encontrado, fue borrado desde la web");

                                        // Paso 1. Borrar sharedP
                                        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.clear();
                                        editor.commit();

                                        // Paso 2. Regresar a CompanyDetailsActivity
                                        Intent goToCompany = new Intent(ShowTicketInfoActivity.this, CompanyDetailsActivity.class);
                                        goToCompany.putExtra("companyId", companyId);
                                        goToCompany.putExtra("officeId", officeId);
                                        startActivity(goToCompany);
                                        finish();
                                        Toast.makeText(ShowTicketInfoActivity.this, "Turno cancelado",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                    }
                });
/*
        mFirestore.collection("companies").document(companyId)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        // Convierte la data y la lleva a tu modelo
                        CompanyId tempCompany = document.toObject(CompanyId.class);
                        mCompany = tempCompany;
                        mCompany.setId(companyId);

                        //Muestra la información en pantalla
                        setOfficeDetails();

                        //Salva la data en el SharedPreferences.
                        saveData();

                        //Click listener del botón de cancelar.
                        cancelTurn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                cancelTurn(v);
                            }
                        });
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d( TAG,"get failed with ", task.getException());
                }
            }
        });*/
    }

    /**
     * Coloca la información correspondiente en pantalla.
     */
    private void setOfficeDetails() {
        //Variables para los horarios
        int opensAtHours, opensAtMinutes, closesAtHours, closesAtMinutes;

        //Consiguiendo el ID de la estación a la que se asignó el turno.
        stationId = currentTurn.getStationId();

        //Consiguiendo el tiempo de espera.
        String waitingTime = Integer.toString(getWaitingTime());

        /**
         * Horarios.
         * @Remark Puedes ignorarlo.
         */
        opensAtHours = mOffice.getOpensAt().toDate().getHours();
        opensAtMinutes = mOffice.getOpensAt().toDate().getMinutes();
        closesAtHours = mOffice.getClosesAt().toDate().getHours();
        closesAtMinutes = mOffice.getClosesAt().toDate().getMinutes();

        //Colocando la información en los TextViews.
        companyName.setText(mCompany.getName());
        subsidiaryName.setText(mOffice.getName());
        address.setText(mOffice.getAddress());
        schedule.setText("Horario: " + formatHour(opensAtHours, opensAtMinutes) + " - " + formatHour(closesAtHours, closesAtMinutes));
        ticket.setText(currentTurn.getId());
        time.setText(waitingTime + " Minutos apróx.");
        station.setText(stationId);
    }

    /**
     * Devuelve la hora en formato de 12 horas.
     *
     * @param hour
     * @param minutes
     * @return
     */
    private String formatHour(int hour, int minutes) {

        if (hour > 12)
            return Integer.toString(hour - 12) + ":" + formatMinutes(minutes) + "PM";
        else {
            if (hour == 0)
                return "12:" + formatMinutes(minutes) + "AM";
            else
                return Integer.toString(hour) + ":" + formatMinutes(minutes) + "AM";
        }
    }

    private String formatMinutes(int minutes) {
        if (minutes == 0)
            return "00";
        else
            return Integer.toString(minutes);
    }

    /**
     * SharedPreferences save data.
     */
    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("compName", companyName.getText().toString());
        editor.putString("officeName", subsidiaryName.getText().toString());
        editor.putString("address", address.getText().toString());
        editor.putString("schedule", schedule.getText().toString());
        editor.putString("turnId", currentTurn.getId());
        editor.putString("ticket", ticket.getText().toString());
        editor.putString("companyId", mCompany.getId());
        editor.putString("officeId", mOffice.getId());
        editor.putString("time", time.getText().toString());
        editor.putString("station", stationId);
        editor.putBoolean("isActive", DashboardActivity.isTicketActive);

        editor.commit();
    }

    /**
     * SharedPreferences load data.
     */
    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        compName = sharedPreferences.getString("compName", "");
        subName = sharedPreferences.getString("officeName", "");
        addr = sharedPreferences.getString("address", "");
        sched = sharedPreferences.getString("schedule", "");
        turnId = sharedPreferences.getString("turnId", "");
        companyId = sharedPreferences.getString("companyId", "");
        officeId = sharedPreferences.getString("officeId", "");
        waitingTime = sharedPreferences.getString("time", "");
        stationId = sharedPreferences.getString("station", "");
        DashboardActivity.isTicketActive = sharedPreferences.getBoolean("isActive", false);
    }

    /**
     * SharedPreferences update de la data.
     */
    private void updateViews() {
        companyName.setText(compName);
        subsidiaryName.setText(subName);
        address.setText(addr);
        schedule.setText(sched);
        ticket.setText(turnId);
        time.setText(waitingTime);
        station.setText(stationId);
        time.setText(waitingTime);
    }

    /**
     * Función que calcula el tiempo de espera
     * según el tipo de turno solicitado.
     */
    private int getWaitingTime() {

        int turnsQuantity = mOffice.getTurns().size();

        //Si no hay nadie adelante, no hay tiempo de espera ya que es su turno.
        if (turnsQuantity == 1)
            return 0;

        //Se calcula el tiempo de espera dependiendo de la cantidad de turnos existentes
        //según el criterio elegido.
        if (currentTurn.isForMemberships())
            return searchTurns(true) * mOffice.getAverageTime();
        else if (currentTurn.isForPreferentialAttention())
            return searchTurns(false) * mOffice.getAverageTime();
        else
            return searchGenericTurns();
    }

    /**
     * Función que busca en la lista de turnos
     * la cantidad de turnos en cola
     * según el tipo de turno solicitado.
     *
     * @param typeOfTurn true si es de membresía, false si es de preferenciales.
     * @return Entero con cantidad de turnos encontrados.
     */
    private int searchTurns(boolean typeOfTurn) {
        List<Turn> turns = mOffice.getTurns();
        int acum = 0;

        for (Turn turn : turns) {

            if (currentTurn.getId().equals(turn.getId()))
                break;

            if (typeOfTurn) {
                if (currentTurn.isForMemberships() && currentTurn.getStationId().equals(turn.getStationId()))
                    acum += mOffice.getAverageTime();
            } else {
                if (currentTurn.isForPreferentialAttention() && currentTurn.getStationId().equals(turn.getStationId()))
                    acum = mOffice.getAverageTime();
            }
        }

        //Se resta el tiempo de espera del turno perteneciente a ese cliente.
        return acum;
    }

    /**
     * Función que busca en la lista de turnos
     * la cantidad de turnos en cola
     * según el tipo de turno solicitado.
     *
     * @return Entero con cantidad de turnos encontrados.
     */
    private int searchGenericTurns() {
        List<Turn> turns = mOffice.getTurns();
        int acum = 0;

        for (Turn turn : turns) {

            if (currentTurn.getId().equals(turn.getId()))
                break;

            if (!turn.isForMemberships() && !turn.isForPreferentialAttention())
                acum += mOffice.getAverageTime();
        }

        return acum;
    }

}
