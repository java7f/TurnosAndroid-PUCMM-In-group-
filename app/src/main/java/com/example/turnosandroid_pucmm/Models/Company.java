/**
 * @file Company.java
 * @brief Fuente del modelo Company.
 */

package com.example.turnosandroid_pucmm.Models;

import com.google.firebase.Timestamp;

import java.util.List;

/**
 * Modelo representativo de la información contenida en una Empresa.
 */
public class Company {

    /**
     * Tiempo de creación de la empresa.
     */
    private Timestamp createdAt;

    /**
     * Usuario que crea la empresa.
     */
    private User createdBy;

    /**
     * Nombre de la empresa.
     */
    private String name;

    /**
     * Correo de la empresa.
     */
    private String emailAddress;

    /**
     * Tipo de servicio ofrecido.
     */
    private String typeOfService;

    /**
     * ¿Acepta guest users?
     */
    private boolean acceptGuest;

    /**
     * Criterio de turnos.
     */
    private String ticketCriteria;

    /**
     * Lista de membresías.
     */
    private List<Membership> memberships;

    /**
     * Lista de servicios.
     */
    private List<Service> services;

    /**
     * Tiempo máximo de cancelación del ticket.
     */
    private int timeLimitCancelTicket;

    /**
     * Lista de sucursales.
     */
    private List<Office> offices;

    /**
     * ¿Tiene membresías?
     */
    private Boolean hasMemberships;

    public Company(Timestamp createdAt, User createdBy, String name, String emailAddress, String typeOfService, boolean acceptGuest, String ticketCriteria, List<Membership> memberships, List<Service> services, int timeLimitCancelTicket, List<Office> offices, Boolean hasMemberships) {
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.name = name;
        this.emailAddress = emailAddress;
        this.typeOfService = typeOfService;
        this.acceptGuest = acceptGuest;
        this.ticketCriteria = ticketCriteria;
        this.memberships = memberships;
        this.services = services;
        this.timeLimitCancelTicket = timeLimitCancelTicket;
        this.offices = offices;
        this.hasMemberships = hasMemberships;
    }

    public Company() {
        /*createdAt = new Timestamp(0,0);
        createdBy = new User();
        name = "";
        emailAddress = "";
        typeOfService = "";
        acceptGuest = false;
        ticketCriteria = "";
        memberships = new ArrayList<>();
        timeLimitCancelTicket = 0;
        offices = new ArrayList<>();
        hasMemberships = false;         */
    }

    /**
     * Devuelve el tiempo de creación de la empresa.
     * @return Timestamp del tiempo de creación.
     */
    public Timestamp getCreatedAt() {
        return createdAt;
    }

    /**
     * Fija el tiempo de creación de la empresa.
     * @param createdAt Timestamp con el tiempo de creación.
     */
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Devuelve el usuario que creó la empresa.
     * @return User que creó el turno.
     */
    public User getCreatedBy() {
        return createdBy;
    }

    /**
     * Fija el usuario que creó la empresa.
     * @param createdBy User que crea el turno.
     */
    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * Función que devuelve el nombre de la empresa.
     * @return String de nombre.
     */
    public String getName() {
        return name;
    }

    /**
     * Función que fija el atributo de nombre de la empresa.
     * @param name Nombre a fijar.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Devuelve el correo electrónico.
     * @return String con correo.
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * Fija el correo electrónico de la empresa.
     * @param emailAddress Correo.
     */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    /**
     * Devuelve el tipo de servicio ofrecido.
     * @return Stringo con el tipo de servicio.
     */
    public String getTypeOfService() {
        return typeOfService;
    }

    /**
     * Fija el tipo de servicio ofrecido.
     * @param typeOfService nombre del servicio.
     */
    public void setTypeOfService(String typeOfService) {
        this.typeOfService = typeOfService;
    }

    /**
     * Devuelve si la empresa acepta guest users.
     * @return True si acepta guest users.
     */
    public boolean isAcceptGuest() {
        return acceptGuest;
    }

    /**
     * Fija si la empresa acepta guest users.
     * @param acceptGuest true or false.
     */
    public void setAcceptGuest(boolean acceptGuest) {
        this.acceptGuest = acceptGuest;
    }

    /**
     * Devuelve el criterio para otorgar turnos.
     * @return String con criterio.
     */
    public String getTicketCriteria() {
        return ticketCriteria;
    }

    /**
     * Fija el criterio para otorgar turnos.
     * @param ticketCriteria Tipo de criterio.
     */
    public void setTicketCriteria(String ticketCriteria) {
        this.ticketCriteria = ticketCriteria;
    }

    /**
     * Devuelve la lista de membresías.
     * @return Lista de Membership.
     */
    public List<Membership> getMemberships() {
        return memberships;
    }

    /**
     * Fija la lista de membresías de la empresa.
     * @param memberships Lista de Membership.
     */
    public void setMemberships(List<Membership> memberships) {
        this.memberships = memberships;
    }

    /**
     * Devuelve la lista de servicios ofrecidos en la empresa.
     * @return Lista de Service
     */
    public List<Service> getServices() {
        return services;
    }

    /**
     * Fija la lista de servicios ofrecidos en la empresa.
     * @param services Lista de Service
     */
    public void setServices(List<Service> services) {
        this.services = services;
    }

    /**
     * Devuelve el tiempo máximo para cancelar un turno.
     * @return Entero con tiempo de cancelación.
     */
    public int getTimeLimitCancelTicket() {
        return timeLimitCancelTicket;
    }

    /**
     * Fija el tiempo máximo para cancelar el ticket.
     * @param timeLimitCancelTicket Tiempo máximo de cancelación.
     */
    public void setTimeLimitCancelTicket(int timeLimitCancelTicket) {
        this.timeLimitCancelTicket = timeLimitCancelTicket;
    }

    /**
     * Devuelve la lista de sucursales.
     * @return Lista de Office.
     */
    public List<Office> getOffices() {
        return offices;
    }

    /**
     * Fija la lista de sucursales.
     * @param offices Lista de Office.
     */
    public void setOffices(List<Office> offices) {
        this.offices = offices;
    }

    /**
     * Devuelve si la compañía tiene membresías.
     * @return true si tiene membresías.
     */
    public Boolean getHasMemberships() {
        return hasMemberships;
    }

    /**
     * Fija si la compañía tiene o no membresías.
     * @param hasMemberships true or false.
     */
    public void setHasMemberships(Boolean hasMemberships) {
        this.hasMemberships = hasMemberships;
    }
}
