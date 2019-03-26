package com.example.turnosandroid_pucmm.Jesse;

/**
 * @file AdapterDatos.java
 * @brief Fuente del Adapter utilizado por el Recycler.
 */

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.turnosandroid_pucmm.Models.Company;
import com.example.turnosandroid_pucmm.Models.CompanyId;
import com.example.turnosandroid_pucmm.Models.Office;
import com.example.turnosandroid_pucmm.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Clase que toma la lista de compañías y despliega
 * todas las sucursales disponibles.
 */
public class AdapterDatos
        extends RecyclerView.Adapter<AdapterDatos.ViewHolderDatos>
        implements View.OnClickListener {

    //Lista de compañías.
    private List<CompanyId> companies;

    //Tabla que asocia el ID de una sucursal con el ID de la compañía a la que pertenece.
    private HashMap<String, String> officeCompanyLinker;

    //Lista de sucursale.
    private List<Office> listOffices;

    //Click listener.
    private View.OnClickListener listener;


    /**
     * Constructor
     *
     * @param companies Lista de compañías.
     */
    public AdapterDatos(List<CompanyId> companies, HashMap<String, String> officeCompanyLinker) {
        this.companies = companies;
        this.officeCompanyLinker = officeCompanyLinker;
        listOffices = new ArrayList<>();

        //Obteniendo la lista de sucursales total.
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


        holder.asignarDatos(position);
    }

    /**
     * Devuelve la lista de sucursales desplegada.
     *
     * @return Lista de Office.
     */
    public List<Office> getListOffices() {
        return listOffices;
    }

    /**
     * Renorta la contidad de elementos que serán mostrados.
     *
     * @return Entero con el tamaño de la lista de sucursales.
     */
    @Override
    public int getItemCount() {
        return listOffices.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
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

        //Vistas del View.
        TextView idSucursal;
        TextView idHorario;
        TextView idNombre;
        TextView idTiempo;
        TextView idCantidadTurnos;
        LinearLayout linearDashboard;

        /**
         * Asocia cada vista con su elemento en el layout.
         *
         * @param itemView
         */
        public ViewHolderDatos(@NonNull View itemView) {
            super(itemView);

            idNombre = itemView.findViewById(R.id.idNombre);
            idSucursal = itemView.findViewById(R.id.idSucursal);
            idHorario = itemView.findViewById(R.id.idHorario);
            idTiempo = itemView.findViewById(R.id.idTiempo);
            idCantidadTurnos = itemView.findViewById(R.id.idCantidadTurnos);
            linearDashboard = itemView.findViewById(R.id.linearDashboard);

        }

        /**
         * Coloca el texto correspondiente en cada elemento del layout.
         *
         * @param position Posición de la carta.
         */
        public void asignarDatos(int position) {

            //Sucursal a mostrar
            Office currentOffice = listOffices.get(position);

            String currentCompanyName = officeCompanyLinker.get(currentOffice.getId());

            for(CompanyId companyId : companies)
            {
                if(companyId.getId().equals(currentCompanyName))
                {
                    currentCompanyName = companyId.getName();
                    break;
                }
            }

            //Cantidad de turnos en cola.
            int turnsQuantity = currentOffice.getTurns().size();
            //Cálculo del tiempo apróximado de espera.
            String waitingTime = Integer.toString(currentOffice.getAverageTime() * turnsQuantity);

            idNombre.setText(currentCompanyName);
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

