package com.example.turnosandroid_pucmm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class PickSchedule extends AppCompatActivity {

    /**
     * Lista que contendrá los horarios disponibles.
     */
    ListView scheduleList;

    /**
     *
     */
    ArrayAdapter adapter;

    String[] hours= {"11:30am", "12:00pm", "12:30pm", "1:00pm", "1:30pm", "2:00pm", "2:30pm", "3:00pm", "3:30pm", "4:00pm"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_schedule);
        setTitle("Seleccione su horario");


        //Init adapter
        adapter = new ArrayAdapter<String>(this, R.layout.activity_list_label, hours);

        //Linking to list
        scheduleList = (ListView) findViewById(R.id.schedule_list);

        //Set adapter
        scheduleList.setAdapter(adapter);


        scheduleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                goToCompany();
            }
        });
    }

    public void goToCompany(){
        Intent intent = getIntent();
        Intent goToCompany = new Intent(this, MainActivity.class);
        goToCompany.putExtra("hide", intent.getStringExtra("hide"));
        startActivity(goToCompany);
    }


}
