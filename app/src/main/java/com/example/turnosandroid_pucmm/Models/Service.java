/**
 * @file Service.java
 * @brief Fuente del modelo Service.
 */

package com.example.turnosandroid_pucmm.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Modelo representativo de la información contenida en un servicio de la empresa.
 */
public class Service implements Parcelable {

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Service createFromParcel(Parcel in) {
            return new Service(in);
        }

        public Service[] newArray(int size) {
            return new Service[size];
        }
    };

    /**
     * Nombre del servicio.
     */
    private String name;

    /**
     * Constructor
     */
    public Service() {
        name = "";
    }

    public Service(String name) {
        this.name = name;
    }

    /**
     * Función que devuelve el nombre del servicio.
     * @return String con el nombre del servicio.
     */
    public String getName() {
        return name;
    }

    /**
     * Función que fija el nombre de un servicio.
     * @param name Nombre del sevicio.
     */
    public void setName(String name) {
        this.name = name;
    }


    public Service(Parcel in){
        name = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
    }
}
