/**
 * @file Turn.java
 * @brief Fuente del modelo Turn.
 */

package com.example.turnosandroid_pucmm.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;

/**
 * Clase representativa de la información contenida en un turno.
 */
public class Turn implements Parcelable {

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Turn createFromParcel(Parcel in) {
            return new Turn(in);
        }

        public Turn[] newArray(int size) {
            return new Turn[size];
        }
    };

    /**
     * Indentificador único del turno.
     */
    private String turnId;

    /**
     * Tiempo de creación del turno.
     */
    private Timestamp createdAt;

    /**
     * Usuario que pide el turno
     */
    private UserId createdFor;

    /**
     * Tipo de servicio solicitado.
     */
    private String typeOfService;

    /**
     * Estación a la que irá asignada el turno.
     */
    private String stationId;

    /**
     * ¿Es un turno de atención preferencial?
     */
    private boolean isForPreferentialAttention;

    /**
     * ¿Es un turno por membresía?
     */
    private boolean isForMemberships;

    /**
     * Membresía elegida.
     */
    private String membership;

    /**
     * Constructor
     */
    public Turn() {
        turnId = "";
        createdAt = new Timestamp(0,0);
        typeOfService = "";
        isForPreferentialAttention = false;
        isForMemberships = false;
        membership = "";
        createdFor = new UserId();

    }

    public Turn(String turnId, Timestamp createdAt, UserId user,
                String typeOfService, String stationId, boolean isForPreferentialAttention,
                boolean isForMemberships, String membership) {
        this.turnId = turnId;
        this.createdAt = createdAt;
        this.typeOfService = typeOfService;
        this.stationId = stationId;
        this.isForPreferentialAttention = isForPreferentialAttention;
        this.isForMemberships = isForMemberships;
        this.membership = membership;
        this.createdFor = user;
    }


    /**
     * Devuelve el ID del turno.
     * @return String con ID.
     */
    public String getTurnId() {
        return turnId;
    }

    /**
     * Fija el valor del ID del turno.
     * @param turnId ID.
     */
    public void setTurnId(String turnId) {
        this.turnId = turnId;
    }

    /**
     * Devuelve el tiempo de creación del turno.
     * @return Timestamp del tiempo de creación.
     */
    public Timestamp getCreatedAt() {
        return createdAt;
    }

    /**
     * Fija el tiempo de creación del turno.
     * @param createdAt Timestamp con el tiempo de creación.
     */
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }


    /**
     * Devuelve el tipo de servicio solicitado.
     * @return Stringo con el tipo de servicio.
     */
    public String getTypeOfService() {
        return typeOfService;
    }

    /**
     * Fija el tipo de servicio solicitado.
     * @param typeOfService nombre del servicio.
     */
    public void setTypeOfService(String typeOfService) {
        this.typeOfService = typeOfService;
    }

    /**
     * Devuelve el ID de la estación a la que está asignada el turno.
     * @return String con ID.
     */
    public String getStationId() {
        return stationId;
    }

    /**
     * Fija el ID de la estación.
     * @param stationId ID.
     */
    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    /**
     * Devuelve si el turno es para atención preferencial.
     * @return true si es para preferencial.
     */
    public boolean isForPreferentialAttention() {
        return isForPreferentialAttention;
    }

    /**
     * Fija si el turno es para preferencial
     * @param forPreferentialAttention
     */
    public void setForPreferentialAttention(boolean forPreferentialAttention) {
        isForPreferentialAttention = forPreferentialAttention;
    }

    public boolean isForMemberships() {
        return isForMemberships;
    }

    public void setForMemberships(boolean forMemberships) {
        isForMemberships = forMemberships;
    }

    public String getMembership() {
        return membership;
    }

    public void setMembership(String membership) {
        this.membership = membership;
    }

    public UserId getCreatedFor() {
        return createdFor;
    }

    public void setCreatedFor(UserId createdFor) {
        this.createdFor = createdFor;
    }

    public Turn(Parcel in){
        this.turnId = in.readString();
        this.createdAt = in.readParcelable(Timestamp.class.getClassLoader());
        this.typeOfService = in.readString();
        this.stationId = in.readString();
        this.isForPreferentialAttention = in.readByte() != 0;
        this.isForMemberships = in.readByte() != 0;
        this.membership = in.readString();
        this.createdFor = in.readParcelable(UserId.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(turnId);
        dest.writeParcelable(createdAt, flags);
        dest.writeString(typeOfService);
        dest.writeString(stationId);
        dest.writeInt(isForPreferentialAttention ? 1 : 0);
        dest.writeInt(isForMemberships ? 1 : 0);
        dest.writeString(membership);
        dest.writeParcelable(createdFor, flags);
    }
}
