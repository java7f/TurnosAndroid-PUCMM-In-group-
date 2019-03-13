package com.example.turnosandroid_pucmm.Jesse;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.turnosandroid_pucmm.Models.Company;
import com.example.turnosandroid_pucmm.Models.Office;
import com.example.turnosandroid_pucmm.R;

import java.util.List;

public class AdapterDatos
        extends RecyclerView.Adapter<AdapterDatos.ViewHolderDatos>
        implements View.OnClickListener{

    private Company company;
    private List<Office> listOffices;
    private View.OnClickListener listener;

    public AdapterDatos(Company company) {
        this.company = company;
    }

    @NonNull
    @Override
    public ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list,null,false);

        view.setOnClickListener(this);

        return new ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDatos holder, int position) {
        holder.asignarDatos(company, position);
    }

    @Override
    public int getItemCount() {
        if(company != null)
            return company.getOffices().size();
        else
            return 1;

    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }
    @Override
    public void onClick(View v) {
        if(listener!=null){
            listener.onClick(v);
        }
    }

    public class ViewHolderDatos extends RecyclerView.ViewHolder {

        TextView idSucursal;
        TextView idHorario;
        TextView idNombre;
        TextView idTiempo;
        TextView idCantidadTurnos;


        public ViewHolderDatos(@NonNull View itemView) {
            super(itemView);

            idNombre = itemView.findViewById(R.id.idNombre);
            idSucursal = itemView.findViewById(R.id.idSucursal);
            idHorario = itemView.findViewById(R.id.idHorario);
            idTiempo = itemView.findViewById(R.id.idTiempo);
            idCantidadTurnos = itemView.findViewById(R.id.idCantidadTurnos);
        }

        public void asignarDatos(Company company, int position) {

            listOffices = company.getOffices();
            idNombre.setText(company.getName());
            idSucursal.setText(listOffices.get(position).getName());
            idHorario.setText(listOffices.get(position).getOpensAt().toDate().getHours() + ":00 - " + listOffices.get(position).getClosesAt().toDate().getHours() + ":00");

        }
    }
}

