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

import java.util.ArrayList;
import java.util.List;

public class AdapterDatos
        extends RecyclerView.Adapter<AdapterDatos.ViewHolderDatos>
        implements View.OnClickListener {

    private List<Company> companies;
    private List<Office> listOffices;
    private View.OnClickListener listener;
    private int officesAcum, companyIndex;

    public AdapterDatos(List<Company> companies) {
        this.companies = companies;
        companyIndex = 0;
        officesAcum = companies.get(0).getOffices().size();

        listOffices = new ArrayList<>();
        for (Company company : companies) {
            listOffices.addAll(company.getOffices());
        }
    }

    @NonNull
    @Override
    public ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list, null, false);

        view.setOnClickListener(this);

        return new ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDatos holder, int position) {

        /**
         * Proceso que maneja el índice de la compañía a la cual se están desplegando sus sucursales.
         * La idea del procedimiento consiste en poder determinar a cuál compañía pertenece
         * la sucursal que está siendo desplegada en pantalla, obetenida del atributo listOffices.
         */
        if (companyIndex < companies.size()) {

            holder.asignarDatos(companies.get(companyIndex), position);
            if ((officesAcum - 1) == position) {
                companyIndex++;
                if (companyIndex < companies.size())
                    officesAcum += companies.get(companyIndex).getOffices().size();
            }

            System.out.println(companyIndex + " / " + officesAcum);
        }

    }

    @Override
    public int getItemCount() {
        return listOffices.size();
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
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

            System.out.println("Position: " + position + "/Company: " + company.getName() + "/Size = " + company.getOffices().size());
            idNombre.setText(company.getName());
            idSucursal.setText(listOffices.get(position).getName());
            if(listOffices.get(position).getOpensAt().toDate().getHours() < 12 & listOffices.get(position).getClosesAt().toDate().getHours() < 12)
                idHorario.setText(listOffices.get(position).getOpensAt().toDate().getHours() + ":00AM - " + listOffices.get(position).getClosesAt().toDate().getHours() + ":00AM");
            else if(listOffices.get(position).getOpensAt().toDate().getHours() < 12 & listOffices.get(position).getClosesAt().toDate().getHours() > 12)
                idHorario.setText(listOffices.get(position).getOpensAt().toDate().getHours() + ":00AM - " + (listOffices.get(position).getClosesAt().toDate().getHours() - 12) + ":00PM");
            else if(listOffices.get(position).getOpensAt().toDate().getHours() > 12 & listOffices.get(position).getClosesAt().toDate().getHours() < 12)
                idHorario.setText((listOffices.get(position).getOpensAt().toDate().getHours() - 12) + ":00PM - " + listOffices.get(position).getClosesAt().toDate().getHours() + ":00AM");
            else
                idHorario.setText((listOffices.get(position).getOpensAt().toDate().getHours() - 12) + ":00PM - " + (listOffices.get(position).getClosesAt().toDate().getHours() - 12) + ":00PM");
        }
    }
}

