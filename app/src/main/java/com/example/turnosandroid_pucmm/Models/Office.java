/**
 * @file Office.java
 * @brief Fuente del modelo Office.
 */

package com.example.turnosandroid_pucmm.Models;

import com.google.firebase.Timestamp;

/**
 * Clase representativa de la información de una Sucursal en una Empresa.
 */
public class Office {

    //Indentificador único de la sucursal.
    private String id;

    //Nombre de la sucursal.
    private String name;

    //Dirección de la sucursal.
    private String address;

    //Teléfono de la sucursal.
    private String phone;

    //Tiempo de espera promedio.
    private int averageNumber;

    //Latitud de la localización.
    private String latitude;

    //Longitud de la localización.
    private String longitude;

    //Hora de apertura.
    private Timestamp opensAt;

    //Hora de cierre.
    private Timestamp closesAt;

    //¿Tiene estaciones para clientes preferenciales?
    private boolean hasStationsForPreferentials;

    //¿Tiene estaciones para clientes con membresía?
    private boolean hasStationsForMemberships;

    //Lista de servicios disponibles en la sucursal.
    private Service[] services;

    //Lista de las estaciones para atención al cliente.
    private Station[] stations;

    //Lista de turnos para gestionar generaciones de tickets.
    private Turn[] turns;

    //Lista de turnos para gestionar reservas de horario.
    private Reservation[] reservations;


    /**
     * Devuelve el ID de la sucursal.
     * @return String con ID.
     */
    public String getId() {
        return id;
    }

    /**
     * Fija el valor del ID de la sucursal.
     * @param id ID.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Devuelve el nombre de la sucursal.
     * @return String con nombre.
     */
    public String getName() {
        return name;
    }

    /**
     * Fija el nombre de la sucursal.
     * @param name Nombre.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Devuelve la dirección de la sucursal.
     * @return String con dirección.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Fija la dirección de la sucursal.
     * @param address Dirección.
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Devuelve el teléfono de la sucursal.
     * @return String con teléfono.
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Fija el teléfono de una sucursal.
     * @param phone Teléfono.
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Devuelve el tiempo de espera promedio en una sucursal.
     * @return Entero con tiempo de espera.
     */
    public int getAverageNumber() {
        return averageNumber;
    }

    /**
     * Fija el valor de tiempo de espera.
     * @param averageNumber Tiempo de espera.
     */
    public void setAverageNumber(int averageNumber) {
        this.averageNumber = averageNumber;
    }

    /**
     * Devuelve la latitud de la sucursal.
     * @return String con latitud.
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     * Fija la coordenada de latitud de la sucursal.
     * @param latitude Latitud.
     */
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    /**
     * Devuelve la coordenada de longitud de la sucursal.
     * @return String con longitud
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     * Fija el la longitud de la sucursal.
     * @param longitude Longitud.
     */
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    /**
     * Devuelve el horario de apertura.
     * @return Timestamp con tiempo de apertura.
     */
    public Timestamp getOpensAt() {
        return opensAt;
    }

    /**
     * Fija el horario de apertura.
     * @param opensAt Timestamp de tiempo de apertura.
     */
    public void setOpensAt(Timestamp opensAt) {
        this.opensAt = opensAt;
    }

    /**
     * Devuelve el horario de cierre.
     * @return Timestamp con horario de cierre.
     */
    public Timestamp getClosesAt() {
        return closesAt;
    }

    /**
     * Fija el valor del horario de cierre.
     * @param closesAt Timestamp de hora de cierre.
     */
    public void setClosesAt(Timestamp closesAt) {
        this.closesAt = closesAt;
    }

    /**
     * Devuelve si la sucursal tiene estaciones de atención preferencial.
     * @return True si tiene estaciones para preferenciales.
     */
    public boolean isHasStationsForPreferentials() {
        return hasStationsForPreferentials;
    }

    /**
     * Fija si una sucursal tiene estaciones para atención preferencial.
     * @param hasStationsForPreferentials true or false
     */
    public void setHasStationsForPreferentials(boolean hasStationsForPreferentials) {
        this.hasStationsForPreferentials = hasStationsForPreferentials;
    }

    /**
     * Devuelve si una sucursal tiene estaciones para membresías.
     * @return True si tiene estaciones para membresías.
     */
    public boolean isHasStationsForMemberships() {
        return hasStationsForMemberships;
    }

    /**
     * Fija si una sucursal tiene estaciones para membresías.
     * @param hasStationsForMemberships true or false.
     */
    public void setHasStationsForMemberships(boolean hasStationsForMemberships) {
        this.hasStationsForMemberships = hasStationsForMemberships;
    }

    /**
     * Devuelve la lista de servicios ofrecidos en la sucursal.
     * @return Lista de Service
     */
    public Service[] getServices() {
        return services;
    }

    /**
     * Fija la lista de servicios ofrecidos.
     * @param services Lista de Service
     */
    public void setServices(Service[] services) {
        this.services = services;
    }

    /**
     * Devuelve la lista de estaciones en la sucursal.
     * @return Lista de Station
     */
    public Station[] getStations() {
        return stations;
    }

    /**
     * Fija la lista de estaciones.
     * @param stations Lista de Station.
     */
    public void setStations(Station[] stations) {
        this.stations = stations;
    }

    /**
     * Devuelve la lista de turnos de generación de tickets.
     * @return Lista de Turn
     */
    public Turn[] getTurns() {
        return turns;
    }

    /**
     * Fija la lista de turnos por generación de ticket.
     * @param turns Lista de Turn
     */
    public void setTurns(Turn[] turns) {
        this.turns = turns;
    }

    /**
     * Devuelve la lista de turnos por reservaciones de horarios.
     * @return Lista de Reservation.
     */
    public Reservation[] getReservations() {
        return reservations;
    }

    /**
     * Fija la lista de turnos por reserva de horarios.
     * @param reservations Lista de Reservation.
     */
    public void setReservations(Reservation[] reservations) {
        this.reservations = reservations;
    }
}
