package com.example.turnosandroid_pucmm.Javier;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.turnosandroid_pucmm.Models.Company;
import com.example.turnosandroid_pucmm.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class CompanyDetailsActivity extends AppCompatActivity {

    //Company for testing
    private Company mCompany;

    //Firebase instance
    private FirebaseFirestore mFirestore;
    private static final String TAG = "CompanyDetailsActivity";

    //Buttons
    private Button requestTurn, cancelTurn;

    //Test fields
    private TextView companyName, subsidiaryName, address, schedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_details);
        Intent intent = getIntent();
        mCompany = new Company();
        requestTurn = (Button) findViewById(R.id.requestTurn);
        cancelTurn = (Button) findViewById(R.id.cancelTurn);
        companyName = (TextView) findViewById(R.id.companyName);
        subsidiaryName = (TextView) findViewById(R.id.subsidiaryName);
        address = (TextView) findViewById(R.id.address);
        schedule = (TextView) findViewById(R.id.schedule);

        mFirestore = FirebaseFirestore.getInstance();
        fetchData();


        /*companyName.setText(mCompany.getName());
        subsidiaryName.setText(mCompany.getOffices().get(0).getName());
        address.setText(mCompany.getOffices().get(0).getAddress());
        schedule.setText(mCompany.getOffices().get(0).getOpensAt().toString() + " - " + mCompany.getOffices().get(0).getOpensAt().toString());
*/
        int test = intent.getIntExtra("hide", 1);

        if(intent.getExtras() != null && test == 1)
        {
            requestTurn.setEnabled(false);
            cancelTurn.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Prueba de paso de un activity a otro. Se llama cuando se presiona el botón de Pedir Turno.
     */
    public void selectService(View viewServices){
        Intent goToServices = new Intent(this, AskTicketActivity.class);
        goToServices.putExtra("hide", 1);
        startActivity(goToServices);
    }

    public void cancelTurn(View viewServices){

        requestTurn.setEnabled(true);
        cancelTurn.setVisibility(View.INVISIBLE);
    }

    private void fetchData() {
        mFirestore.collection("companies").document("wRjpAUyr25ZYiLtUL110")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        // Convierte la data y la lleva a tu modelo
                        Company temp = document.toObject(Company.class);
                        mCompany = temp;

                        Log.d(TAG,"DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d( TAG,"get failed with ", task.getException());
                }
            }
        });
    }

   /* private void fetchData()
    {
        DocumentReference docRef = mFirestore.collection("companies").document("wRjpAUyr25ZYiLtUL110");
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                mCompany = documentSnapshot.toObject(Company.class);
                Log.d(TAG,"DocumentSnapshot data: " + documentSnapshot.getData());
            }
        });
    }*/
}
