/**
 * @file Role.java
 * @brief Fuente del modelo Role.
 */

package com.example.turnosandroid_pucmm.Models;

/**
 * Clase representativa de los roles de un usuario.
 */
public class Role {

    /**
     * ¿Es administrador?
     */
    boolean administrator;

    /**
     * ¿Es un moderador?
     */
    boolean moderator;

    /**
     * ¿Es un cliente?
     */
    //boolean client;

    /**
     * Constructor
     */
    public Role() {
        administrator = false;
        moderator = false;
        //client = false;
    }

    public Role(boolean administrator, boolean moderator, boolean client) {
        this.administrator = administrator;
        this.moderator = moderator;
        //this.client = client;
    }

    /**
     * Devuelve si el usuario es administrador o no.
     * @return true si es administrador.
     */
    public boolean isAdministrator() {
        return administrator;
    }

    /**
     * Fija el atributo que indica si el usuario es o no administrador.
     * @param administrator true or false
     */
    public void setAdministrator(boolean administrator) {
        this.administrator = administrator;
    }

    /**
     * Devuelve si el usuario es moderador o no.
     * @return true si es moderador.
     */
    public boolean isModerator() {
        return moderator;
    }

    /**
     * Fija el atributo que indica si el usuario es o no moderador.
     * @param moderator true or false.
     */
    public void setModerator(boolean moderator) {
        this.moderator = moderator;
    }


    /**
     * Devuelve si el usuario es un cliente o no.
     * @return true si es un cliente.
     */
    /*public boolean isClient() {
        return client;
    }*/

    /**
     * Fija el atributo que indica si el usuario es o no un cliente.
     * @param client true or false.
     */
   /* public void setClient(boolean client) {
        this.client = client;
    }*/
}
