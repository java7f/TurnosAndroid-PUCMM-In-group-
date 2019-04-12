package com.example.turnosandroid_pucmm.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class CompanyId extends Company implements Parcelable {

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public CompanyId createFromParcel(Parcel in) {
            return new CompanyId(in);
        }

        public CompanyId[] newArray(int size) {
            return new CompanyId[size];
        }
    };

    /**
     * Identificador único de la compañía.
     */
    private String id;

    /**
     * Constructor
     */
    public CompanyId() {
        this.id = "";
    }

    /**
     * Devuelve el ID de la empresa.
     * @return String con ID.
     */
    public String getId() {
        return id;
    }

    /**
     * Fifa el Id de la empresa.
     * @param id ID.
     */
    public void setId(String id) {
        this.id = id;
    }

    public CompanyId(Parcel in){
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
