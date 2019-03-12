package com.example.turnosandroid_pucmm.Models;

public class CompanyId extends Company {

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
}
