/**
 * @file User.java
 * @brief Fuente del model User.
 */

package com.example.turnosandroid_pucmm.Models;

/**
 * Modelo representativo de la información contenida por un usuario.
 */
public class User {

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
}
