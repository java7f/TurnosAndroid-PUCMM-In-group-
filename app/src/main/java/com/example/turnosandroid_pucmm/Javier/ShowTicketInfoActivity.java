package com.example.turnosandroid_pucmm.Javier;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.turnosandroid_pucmm.Jesse.DashboardActivity;
import com.example.turnosandroid_pucmm.Models.CompanyId;
import com.example.turnosandroid_pucmm.Models.Office;
import com.example.turnosandroid_pucmm.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ShowTicketInfoActivity extends AppCompatActivity {

    //Company for testing
    private CompanyId mCompany;

    private Office mOffice;

    private String companyId;

    private String officeId;

    //Firebase instance
    private FirebaseFirestore mFirestore;
    private static final String TAG = "CompanyDetailsActivity";

    //Buttons
    private Button requestTurn, cancelTurn;

    //Text fields
    private TextView companyName, subsidiaryName, address, schedule, time;

    Toolbar toolbar;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CompanyDetailsActivity.cda.finish();
        AskTicketActivity.ata.finish();
        PickTypeOfTurnActivity.ptota.finish();

        setContentView(R.layout.activity_show_ticket_info);

        toolbar = findViewById(R.id.idToolbarCompany);
        setSupportActionBar(toolbar);

        intent = getIntent();
        mCompany = new CompanyId();
        cancelTurn = findViewById(R.id.cancelTurn);
        companyName = findViewById(R.id.companyName);
        subsidiaryName = findViewById(R.id.subsidiaryName);
        address = findViewById(R.id.address);
        schedule = findViewById(R.id.schedule);
        time = findViewById(R.id.ticketNumber);

        mFirestore = FirebaseFirestore.getInstance();

        mCompany = intent.getParcelableExtra("company");
        mOffice = intent.getParcelableExtra("office");
        companyId = mCompany.getId();
        officeId = mOffice.getId();

        fetchData();

    }

    @Override
    protected void onResume() {
        intent = getIntent();
        int test = intent.getIntExtra("hide",0);
        if(intent.getExtras() != null && test == 1)
        {
            requestTurn.setEnabled(false);
            cancelTurn.setVisibility(View.VISIBLE);
        }
        super.onResume();
    }

    public void cancelTurn(View viewServices){
        Intent goToCompany = new Intent(this, CompanyDetailsActivity.class);
        goToCompany.putExtra("company", mCompany);
        goToCompany.putExtra("office", mOffice);
        startActivity(goToCompany);
        finish();
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
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d( TAG,"get failed with ", task.getException());
                }
            }
        });
    }

    private void setOfficeDetails()
    {
        int opensAtHours, opensAtMinutes, closesAtHours, closesAtMinutes;
        List<Office> companyOffices = mCompany.getOffices();

        for (Office office : companyOffices)
        {
            if(office.getId().equals(officeId))
                mOffice = office;
        }

        int turnsQuantity = mOffice.getTurns().size();
        String waitingTime = Integer.toString(mOffice.getAverageTime() * turnsQuantity);

        opensAtHours = mOffice.getOpensAt().toDate().getHours();
        opensAtMinutes = mOffice.getOpensAt().toDate().getMinutes();

        closesAtHours = mOffice.getClosesAt().toDate().getHours();
        closesAtMinutes = mOffice.getClosesAt().toDate().getMinutes();


        companyName.setText(mCompany.getName());
        subsidiaryName.setText(mOffice.getName());
        address.setText(mOffice.getAddress());
        schedule.setText("Horario: " + formatHour(opensAtHours, opensAtMinutes) + " - " + formatHour(closesAtHours, closesAtMinutes));
        time.setText(waitingTime);
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

}
