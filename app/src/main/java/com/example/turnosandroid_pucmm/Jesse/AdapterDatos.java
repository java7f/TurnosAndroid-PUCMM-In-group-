package com.example.turnosandroid_pucmm.Jesse;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.turnosandroid_pucmm.Models.Company;
import com.example.turnosandroid_pucmm.Models.CompanyId;
import com.example.turnosandroid_pucmm.Models.Office;
import com.example.turnosandroid_pucmm.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdapterDatos
        extends RecyclerView.Adapter<AdapterDatos.ViewHolderDatos>
        implements View.OnClickListener {

    private List<CompanyId> companies;
    private List<Office> listOffices;
    private View.OnClickListener listener;
    private int officesAcum, companyIndex;

    public AdapterDatos(List<CompanyId> companies) {
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

    /**
     * Devuelve la lista de sucursales desplegada.
     * @return Lista de Office.
     */
    public List<Office> getListOffices()
    {
        return listOffices;
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

            Office currentOffice = listOffices.get(position);
            int turnsQuantity = currentOffice.getTurns().size();
            String waitingTime = Integer.toString(currentOffice.getAverageTime() * turnsQuantity);

            System.out.println("Position: " + position + "/Company: " + company.getName() + "/Size = " + company.getOffices().size());
            idNombre.setText(company.getName());
            idSucursal.setText(currentOffice.getName());
            idCantidadTurnos.setText(Integer.toString(turnsQuantity));
            idTiempo.setText(waitingTime + " Mins aprox");

            int opensAtHours = currentOffice.getOpensAt().toDate().getHours();
            int opensAtMinutes = currentOffice.getOpensAt().toDate().getMinutes();

            int closesAtHours = currentOffice.getClosesAt().toDate().getHours();
            int closesAtMinutes = currentOffice.getClosesAt().toDate().getMinutes();

            idHorario.setText(formatHour(opensAtHours, opensAtMinutes) + " - " + formatHour(closesAtHours, closesAtMinutes));

        }
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

