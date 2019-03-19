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
    private String id;

    /**
     * Tiempo de creación del turno.
     */
    private Timestamp createdAt;

    /**
     * Usuario que crea el turno.
     */
    private User createdBy;

    /**
     * Tipo de servicio solicitado.
     */
    private String typeOfService;

    /**
     * Constructor
     */
    public Turn() {
        id = "";
        createdAt = new Timestamp(0,0);
        createdBy = new User();
        typeOfService = "";
    }

    public Turn(String id, Timestamp createdAt, User createdBy, String typeOfService) {
        this.id = id;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.typeOfService = typeOfService;
    }

    /**
     * Devuelve el ID del turno.
     * @return String con ID.
     */
    public String getId() {
        return id;
    }

    /**
     * Fija el valor del ID del turno.
     * @param id ID.
     */
    public void setId(String id) {
        this.id = id;
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
     * Devuelve el usuario que creó el turno.
     * @return User que creó el turno.
     */
    public User getCreatedBy() {
        return createdBy;
    }

    /**
     * Fija el usuario que creó el turno.
     * @param createdBy User que crea el turno.
     */
    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
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

    public Turn(Parcel in){
        this.id = in.readString();
        this.createdAt = in.readParcelable(Timestamp.class.getClassLoader());
        this.createdBy = in.readParcelable(User.class.getClassLoader());
        this.typeOfService = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeParcelable(createdAt, flags);
        dest.writeParcelable(createdBy, flags);
        dest.writeString(typeOfService);
    }
}
