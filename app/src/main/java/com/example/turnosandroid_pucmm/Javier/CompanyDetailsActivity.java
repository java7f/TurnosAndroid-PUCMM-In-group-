package com.example.turnosandroid_pucmm.Javier;

/**
 * @file CompanyDetailsActivity.java
 * @brief Fuente del activity CompanyDetails.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.turnosandroid_pucmm.Models.CompanyId;
import com.example.turnosandroid_pucmm.Models.Office;
import com.example.turnosandroid_pucmm.R;
import com.example.turnosandroid_pucmm.Robert.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.List;

/**
 * Clase enlazada con el layout de detalles de la sucursal.
 */
public class CompanyDetailsActivity extends AppCompatActivity {

    //Company a mostrar.
    private CompanyId mCompany;

    //Sucursal seleccionada.
    private Office mOffice;

    //ID de la compañía.
    private String companyId;

    //ID de la sucursal.
    private String officeId;

    //Firebase instance
    private FirebaseFirestore mFirestore;

    //TAG para el log.
    private static final String TAG = "CompanyDetailsActivity";

    //Buttons
    private Button requestTurn;

    //Text fields
    private TextView companyName, subsidiaryName, address, schedule, time;

    public static Activity cda;

    //Toolbar
    Toolbar toolbar;

    //Intent
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_details);

        // Views
        toolbar = findViewById(R.id.idToolbarCompany);
        requestTurn = findViewById(R.id.requestTurn);
        companyName = findViewById(R.id.companyName);
        subsidiaryName = findViewById(R.id.subsidiaryName);
        address = findViewById(R.id.address);
        schedule = findViewById(R.id.schedule);
        time = findViewById(R.id.time);

        cda = this;

        //Coloca el toolbar en la vista.
        setSupportActionBar(toolbar);

        intent = getIntent();

        //Inicialización del company.
        mCompany = new CompanyId();

        //Obteniendo instancia de la base de datos.
        mFirestore = FirebaseFirestore.getInstance();

        //Obteniendo los ID para buscar en la base de datos.
        companyId = intent.getStringExtra("companyId");
        officeId = intent.getStringExtra("officeId");

        //Obteniendo la información en la base de datos.
        fetchData();
    }

    /**
     * Función que busca los datos de la empresa en la base de datos para mostrar su información en
     * pantalla.
     */
    private void fetchData() {
        mFirestore.collection(Util.COLLECTION_COMPANIES).document(companyId)
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

                        //Acción del click en el botón.
                        requestTurn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                selectService(v);
                            }
                        });
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
     * Paso de un activity a otro. Se llama cuando se presiona el botón de Pedir Turno.
     */
    public void selectService(View viewServices) {
        Intent goToServices = new Intent(this, AskTicketActivity.class);
        goToServices.putExtra("company", mCompany);
        goToServices.putExtra("office", mOffice);
        startActivity(goToServices);
    }

    /**
     * Función que se encanrga de tomar la data obtenida para colocar
     * la información correspondiente a la sucursal.
     */
    private void setOfficeDetails() {
        int opensAtHours, opensAtMinutes, closesAtHours, closesAtMinutes;
        List<Office> companyOffices = mCompany.getOffices();

        //Obteniendo la sucursal correspondiente al ID conseguido en los extras.
        for (Office office : companyOffices) {
            if (office.getId().equals(officeId)) {
                mOffice = office;
                break;
            }
        }

        int turnsQuantity = mOffice.getTurns().size();
        String waitingTime = Integer.toString(mOffice.getAverageTime() * turnsQuantity);

        opensAtHours = mOffice.getOpensAt().toDate().getHours();
        opensAtMinutes = mOffice.getOpensAt().toDate().getMinutes();

        closesAtHours = mOffice.getClosesAt().toDate().getHours();
        closesAtMinutes = mOffice.getClosesAt().toDate().getMinutes();

        if ((mOffice.getOpensAt().toDate().before(new Date()))
                && (new Date().before(mOffice.getOpensAt().toDate()))) {
            // opened
            requestTurn.setEnabled(true);
        } else {
            // closed
            requestTurn.setEnabled(false);
            requestTurn.setText("Cerrado");
        }

        companyName.setText(mCompany.getName());
        subsidiaryName.setText(mOffice.getName());
        address.setText(mOffice.getAddress());
        schedule.setText("Horario: " + formatHour(opensAtHours, opensAtMinutes) + " - " + formatHour(closesAtHours, closesAtMinutes));
        time.setText(waitingTime);
    }

    /**
     * Función que recibe la hora y los minutos y las coloca en
     * el formato adecuado.
     *
     * @param hour    Hora
     * @param minutes Minutos
     * @return Cadena con la hora en formato 12 horas.
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

    /**
     * Función que recibe los minutos y devuelve
     * su formato correcto.
     *
     * @param minutes Minutos.
     * @return Cadena con minutos en formato.
     */
    private String formatMinutes(int minutes) {
        if (minutes == 0)
            return "00";
        else
            return Integer.toString(minutes);
    }

}
