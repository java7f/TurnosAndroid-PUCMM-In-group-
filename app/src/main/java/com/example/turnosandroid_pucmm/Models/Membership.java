/**
 * @file Membership.java
 * @brief Fuente del modelo Membership.
 */

package com.example.turnosandroid_pucmm.Models;

/**
 * Modelo representativo de la información contenida en una membresía.
 */
public class Membership {

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
}
