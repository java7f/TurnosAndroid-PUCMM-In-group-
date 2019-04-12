/**
 * @file Station.java
 * @brief Fuente del modelo Station.
 */

package com.example.turnosandroid_pucmm.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Clase representativa de la información contenida en una estación de trabajo
 * de una sucursal.
 */
public class Station implements Parcelable{

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Station createFromParcel(Parcel in) {
            return new Station(in);
        }

        public Station[] newArray(int size) {
            return new Station[size];
        }
    };

    /**
     * Identificador único de la estación.
     */
    private String id;

    /**
     * Tipo de servicio
     */
    private String typeOfService;

    /**
     * ¿Acepta clientes preferenciales?
     */
    private Boolean isForPreferentialAttention;

    /**
     * ¿Acepta clientes con membresía?
     */
    private Boolean isForMemberships;

    /**
     * Constructor
     */
    public Station() {
        id = "";
        typeOfService = "";
        isForPreferentialAttention = false;
        isForMemberships = false;
    }

    public Station(String id, String typeOfService, Boolean isForPreferentialAttention, Boolean isForMemberships) {
        this.id = id;
        this.typeOfService = typeOfService;
        this.isForPreferentialAttention = isForPreferentialAttention;
        this.isForMemberships = isForMemberships;
    }

    /**
     * Devuelve el ID de la sucursal;
     * @return String que contiene el ID.
     */
    public String getId() {
        return id;
    }

    /**
     * Fija el valor del ID.
     * @param id ID de la estación.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Devuelve el tipo de servicio de la estación.
     * @return String que contiene el tipo de servicio.
     */
    public String getTypeOfService() {
        return typeOfService;
    }

    /**
     * Fija el atributo de tipo de servicio.
     * @param typeOfService Nombre del servicio.
     */
    public void setTypeOfService(String typeOfService) {
        this.typeOfService = typeOfService;
    }

    /**
     * Devuelve si la estación acepta clientes preferenciales o no.
     * @return true si acepta clientes preferenciales.
     */
    public Boolean getIsForPreferentialAttention() {
        return isForPreferentialAttention;
    }

    /**
     * Fija el atributo que indica si acepta clientes preferenciales.
     * @param isForPreferentialAttention verdadero o falso.
     */
    public void setIsForPreferentialAttention(Boolean isForPreferentialAttention) {
        this.isForPreferentialAttention = isForPreferentialAttention;
    }

    /**
     * Devuelve si la estación acepta clientes con membresía o no.
     * @return true si acepta clientes con membresía.
     */
    public Boolean getIsForMemberships() {
        return isForMemberships;
    }

    /**
     * Fija el atributo que indica si acepta clientes con membresía.
     * @param isForMemberships verdadero o falso.
     */
    public void setIsForMemberships(Boolean isForMemberships) {
        this.isForMemberships = isForMemberships;
    }


    public Station(Parcel in){
        this.id = in.readString();
        this.typeOfService = in.readString();
        this.isForPreferentialAttention = in.readByte() != 0;
        this.isForMemberships = in.readByte() != 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(typeOfService);
        dest.writeInt(isForPreferentialAttention ? 1 : 0);
        dest.writeInt(isForMemberships ? 1 : 0);
    }
}
