/**
 * @file Membership.java
 * @brief Fuente del modelo Membership.
 */

package com.example.turnosandroid_pucmm.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Modelo representativo de la información contenida en una membresía.
 */
public class Membership implements Parcelable{

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Membership createFromParcel(Parcel in) {
            return new Membership(in);
        }

        public Membership[] newArray(int size) {
            return new Membership[size];
        }
    };

    /**
     * Nombre de la membresía.
     */
    private String name;

    /**
     * Nivel de la membresía.
     */
    private String levelOfPreference;

    /**
     * Constructor.
     */
    public Membership() {
        name = "";
        levelOfPreference= "";
    }

    public Membership(String name, String levelOfPreference) {
        this.name = name;
        this.levelOfPreference = levelOfPreference;
    }

    /**
     * Función que devuelve el nombre de la membresía.
     * @return String de nombre.
     */
    public String getName() {
        return name;
    }

    /**
     * Función que fija el atributo de nombre de la membresía.
     * @param name Nombre a fijar.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Función que devuelve el nivel de preferencia de la membresía.
     * @return Entero con el nivel de preferencia.
     */
    public String getLevelOfPreference() {
        return levelOfPreference;
    }

    /**
     * Función que fija el nivel de preferencia de la membresía.
     * @param levelOfPreference Nivel de preferencia.
     */
    public void setLevelOfPreference(String levelOfPreference) {
        this.levelOfPreference = levelOfPreference;
    }

    public Membership(Parcel in){
        this.name = in.readString();
        this.levelOfPreference = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(levelOfPreference);
    }
}
