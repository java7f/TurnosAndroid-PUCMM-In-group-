package com.example.turnosandroid_pucmm.Javier;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.turnosandroid_pucmm.R;

public class CompanyDetailsActivity extends AppCompatActivity {

    Button requestTurn, cancelTurn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_details);
        Intent intent = getIntent();
        requestTurn = (Button) findViewById(R.id.requestTurn);
        cancelTurn = (Button) findViewById(R.id.cancelTurn);

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
}
