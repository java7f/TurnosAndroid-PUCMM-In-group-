/**
 * @file Turn.java
 * @brief Fuente del modelo Turn.
 */

package com.example.turnosandroid_pucmm.Models;

import com.google.firebase.Timestamp;

/**
 * Clase representativa de la información contenida en un turno.
 */
public class Turn {

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
}
