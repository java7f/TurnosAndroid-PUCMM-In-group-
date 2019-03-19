/**
 * @file Reservation.java
 * @brief Fuente del modelo Reservation.
 */

package com.example.turnosandroid_pucmm.Models;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.Toolbar;

import com.google.firebase.Timestamp;

/**
 * Modelo representativo de la información contenida en una reservación de horario.
 */
public class Reservation implements Parcelable {

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Reservation createFromParcel(Parcel in) {
            return new Reservation(in);
        }

        public Reservation[] newArray(int size) {
            return new Reservation[size];
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
     * Horario a reservar.
     */
    private Timestamp schedule;

    /**
     * Tipo de servicio solicitado.
     */
    private String typeOfService;


    /**
     * Constructor
     */
    public Reservation() {
        id = "";
        createdAt = new Timestamp(0,0);
        createdBy = new User();
        schedule = new Timestamp(0,0);
        typeOfService = "";
    }

    public Reservation(String id, Timestamp createdAt, User createdBy, Timestamp schedule, String typeOfService) {
        this.id = id;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.schedule = schedule;
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

    /**
     * Devuelve el horario a reservar.
     * @return Timestamp con el horario de reserva.
     */
    public Timestamp getSchedule() {
        return schedule;
    }

    /**
     * Fija el horario a reservar.
     * @param schedule Timestamp con horario a reservar.
     */
    public void setSchedule(Timestamp schedule) {
        this.schedule = schedule;
    }

    public Reservation(Parcel in){
        id = in.readString();
        createdAt = in.readParcelable(Timestamp.class.getClassLoader());
        createdBy = in.readParcelable(User.class.getClassLoader());
        schedule = in.readParcelable(Timestamp.class.getClassLoader());
        typeOfService = in.readString();
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
        dest.writeParcelable(schedule, flags);
        dest.writeString(typeOfService);
    }
}
