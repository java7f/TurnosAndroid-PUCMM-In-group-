/**
 * @file User.java
 * @brief Fuente del model User.
 */

package com.example.turnosandroid_pucmm.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Modelo representativo de la información contenida por un usuario.
 */
public class User implements Parcelable {

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

    /**
     * Primer nombre.
     */
    private String firstName;

    /**
     * Primer Apellido
     */
    private String lastName;

    /**
     * Correo
     */
    private String emailAddress;

    /**
     * Rol del usuario.
     */
    private Role roles;

    /**
     * Si es administrador y tiene sucursal asignada.
     */
    private String officeAssignedId;

    /**
     * Contructor
     */
    public User() {
        firstName = "";
        lastName = "";
        emailAddress = "";
        roles = new Role();
    }

    public User(String firstName, String lastName, String emailAddress, Role roles) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.roles = roles;
    }

    /**
     * Devuelve el primer nombre;
     * @return String con nombre.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Fija el nombre del usuario.
     * @param firstName Primer nombre.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Devuelve el apellido.
     * @return String con apellido.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Fija el apellido del usuario.
     * @param lastName Apellido.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Devuelve el correo electrónico.
     * @return String con correo.
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * Fija el correo electrónico del usuario.
     * @param emailAddress Correo.
     */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    /**
     * Devuelve el rol que emplea el usuario en el sistema
     * @return Rol del usuario.
     */
    public Role getRoles() {
        return roles;
    }

    /**
     * Fija el rol del usuario.
     * @param roles Rol.
     */
    public void setRoles(Role roles) {
        this.roles = roles;
    }

    public String getOfficeAssignedId() {
        return officeAssignedId;
    }

    public void setOfficeAssignedId(String officeAssignedId) {
        this.officeAssignedId = officeAssignedId;
    }

    public User(Parcel in){
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.emailAddress = in.readString();
        this.roles = in.readParcelable(Role.class.getClassLoader());
        this.officeAssignedId = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(emailAddress);
        dest.writeParcelable(roles, flags);
        dest.writeString(officeAssignedId);
    }
}
