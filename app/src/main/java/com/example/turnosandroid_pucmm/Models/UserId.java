package com.example.turnosandroid_pucmm.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class UserId extends User implements Parcelable {

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public UserId createFromParcel(Parcel in) {
            return new UserId(in);
        }

        public UserId[] newArray(int size) {
            return new UserId[size];
        }
    };

    /**
     * Identificador único del usuario.
     */
    private String id;

    /**
     * Constructor
     */
    public UserId() {
        id = "";
    }

    public UserId(String id, String firstName, String lastName, String emailAddress, Role roles)
    {
        super(firstName, lastName, emailAddress, roles);
        this.id = id;
    }

    /**
     * Devuelve el ID del usuario.
     * @return String con ID.
     */
    public String getId() {
        return id;
    }

    /**
     * Fifa el Id del usuario.
     * @param id ID.
     */
    public void setId(String id) {
        this.id = id;
    }

    public UserId(Parcel in){
        super(in);
        this.id = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(id);
    }
}
