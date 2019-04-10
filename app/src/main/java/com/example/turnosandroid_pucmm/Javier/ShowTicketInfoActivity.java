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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ShowTicketInfoActivity extends AppCompatActivity {

    public static final String SHARED_PREFS = "sharedPrefs";

    //Company
    private CompanyId mCompany;

    //Sucursal.
    private Office mOffice;

    //IDs de la empresa y sucursal.
    private String companyId;
    private String officeId;

    //Firebase instance
    private FirebaseFirestore mFirestore;

    private static final String TAG = "CompanyDetailsActivity";


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
        mCompany = new CompanyId();

        //Instancia de la base de datos.
        mFirestore = FirebaseFirestore.getInstance();

        //Obteniendo la compañía, sucursal y el turno creado.
        mCompany = intent.getParcelableExtra("company");
        mOffice = intent.getParcelableExtra("office");
        currentTurn = intent.getParcelableExtra("turn");

        /**
         * Si la compañía es nula, quiere decir que ya hay un turno asignado,
         * por lo que se reestablece el estado anterior de la pantalla.
         * En caso contrario, se acaba de crear el turno, or lo que se
         * busca la data en la base de datos y se salva el estado actual.
         */
        if(mCompany == null)
        {
            loadData();
            updateViews();
        }
        else
        {
            //Finalización de los activities anteriores.
            CompanyDetailsActivity.cda.finish();
            AskTicketActivity.ata.finish();
            PickTypeOfTurnActivity.ptota.finish();
            companyId = mCompany.getId();
            officeId = mOffice.getId();
            fetchData();
        }

    }

    /**
     * Cancelación del turno y traslado al activity de detalles de compañía.
     * @param viewServices View.
     */
    public void cancelTurn(View viewServices){
        if(mCompany == null)
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

                        for (Office office : mCompany.getOffices())
                        {
                            if(office.getId().equals(officeId))
                                mOffice = office;
                        }
                        deleteTurn();
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d( TAG,"get failed with ", task.getException());
                }
            }
        });
    }

    /**
     * Lógica de eliminar un turno.
     */
    public void deleteTurn() {
        if(currentTurn != null)
            turnId = currentTurn.getId();
        else
        {
            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
            turnId = sharedPreferences.getString("turnId", "");
        }

        List<Office> offices = mCompany.getOffices();
        List<Turn> turns = mOffice.getTurns();
        int turnSize = turns.size();

        for(int i=0; i<turnSize; i++)
        {
            if(turns.get(i).getId().equals(turnId))
            {
                turns.remove(i);
                break;
            }
        }

        for(int i=0; i<offices.size(); i++)
        {
            if(offices.get(i).getId().equals(mOffice.getId()))
            {
                mCompany.getOffices().set(i, mOffice);
                break;
            }
        }

        DocumentReference document = mFirestore.collection("companies").document(mCompany.getId());
        document.update("offices", offices)
                .addOnSuccessListener(new OnSuccessListener< Void >() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Paso 1 log.e()
                        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.commit();

                        DashboardActivity.isTicketActive = false;
                        Intent goToCompany = new Intent(ShowTicketInfoActivity.this, CompanyDetailsActivity.class);
                        goToCompany.putExtra("companyId", companyId);
                        goToCompany.putExtra("officeId", officeId);
                        startActivity(goToCompany);
                        finish();
                        Toast.makeText(ShowTicketInfoActivity.this, "Turno cancelado",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void fetchData() {
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
                        saveData();
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
        });
    }

    /**
     * Coloca la información correspondiente en pantalla.
     */
    private void setOfficeDetails()
    {
        int opensAtHours, opensAtMinutes, closesAtHours, closesAtMinutes;
        List<Office> companyOffices = mCompany.getOffices();

        for (Office office : companyOffices)
        {
            if(office.getId().equals(officeId))
                mOffice = office;
        }

        stationId = currentTurn.getStationId();


        String waitingTime = Integer.toString(getWaitingTime());

        opensAtHours = mOffice.getOpensAt().toDate().getHours();
        opensAtMinutes = mOffice.getOpensAt().toDate().getMinutes();

        closesAtHours = mOffice.getClosesAt().toDate().getHours();
        closesAtMinutes = mOffice.getClosesAt().toDate().getMinutes();


        companyName.setText(mCompany.getName());
        subsidiaryName.setText(mOffice.getName());
        address.setText(mOffice.getAddress());
        schedule.setText("Horario: " + formatHour(opensAtHours, opensAtMinutes) + " - " + formatHour(closesAtHours, closesAtMinutes));
        ticket.setText(currentTurn.getId());
        time.setText(waitingTime + " Minutos apróx.");
        station.setText(stationId);
    }

    private String formatHour(int hour, int minutes)
    {

        if(hour > 12)
            return Integer.toString(hour-12) + ":" + formatMinutes(minutes) + "PM";
        else
        {
            if (hour == 0)
                return "12:" + formatMinutes(minutes) + "AM";
            else
                return Integer.toString(hour) + ":" + formatMinutes(minutes) + "AM";
        }
    }

    private String formatMinutes(int minutes)
    {
        if(minutes == 0)
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
    private int getWaitingTime(){

        int turnsQuantity = mOffice.getTurns().size();

        if(currentTurn.isForMemberships())
            return searchTurns(true) * mOffice.getAverageTime();
        else if(currentTurn.isForPreferentialAttention())
            return searchTurns(false) * mOffice.getAverageTime();
        else
            return mOffice.getAverageTime() * turnsQuantity;
    }

    /**
     * Función que busca en la lista de turnos
     * la cantidad de turnos en cola
     * según el tipo de turno solicitado.
     * @param typeOfTurn true si es de membresía, false si es de preferenciales.
     * @return Entero con cantidad de turnos encontrados.
     */
    private int searchTurns(boolean typeOfTurn) {
        List<Turn> turns = mOffice.getTurns();
        int acum = 0;

        for(Turn turn : turns){

            if(typeOfTurn){
                if (currentTurn.isForMemberships() && currentTurn.getStationId().equals(turn.getStationId()))
                    acum++;
            }
            else {
                if (currentTurn.isForPreferentialAttention() && currentTurn.getStationId().equals(turn.getStationId()))
                    acum++;
            }
        }

        return acum;
    }

}
