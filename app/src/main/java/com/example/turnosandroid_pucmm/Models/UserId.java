package com.example.turnosandroid_pucmm.Models;

public class UserId extends User {

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
}
