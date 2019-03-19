/**
 * @file Office.java
 * @brief Fuente del modelo Office.
 */

package com.example.turnosandroid_pucmm.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase representativa de la información de una Sucursal en una Empresa.
 */
public class Office implements  Parcelable{

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Office createFromParcel(Parcel in) {
            return new Office(in);
        }

        public Office[] newArray(int size) {
            return new Office[size];
        }
    };

    //Indentificador único de la sucursal.
    private String id;

    //Nombre de la sucursal.
    private String name;

    //Dirección de la sucursal.
    private String address;

    //Teléfono de la sucursal.
    private String phone;

    //Tiempo de espera promedio.
    private Integer averageTime;

    //Latitud de la localización.
    private String latitude;

    //Longitud de la localización.
    private String longitude;

    //Hora de apertura.
    private Timestamp opensAt;

    //Hora de cierre.
    private Timestamp closesAt;

    //¿Tiene estaciones para clientes preferenciales?
    private Boolean hasStationsForPreferential;

    //¿Tiene estaciones para clientes con membresía?
    private Boolean hasStationsForMemberships;

    //Lista de servicios disponibles en la sucursal.
    private List<Service> services;

    //Lista de las estaciones para atención al cliente.
    private List<Station> stations;

    //Lista de turnos para gestionar generaciones de tickets.
    private List<Turn> turns;

    //Lista de turnos para gestionar reservas de horario.
    private List<Reservation> reservations;

    /**
     * Constructor
     */
    public Office() {
        id = "";
        name = "";
        address = "";
        phone = "";
        averageTime = 0;
        latitude = "";
        longitude = "";
        opensAt = new Timestamp(0,0);
        closesAt = new Timestamp(0,0);
        hasStationsForMemberships = false;
        hasStationsForPreferential = false;
        services = new ArrayList<>();
        stations = new ArrayList<>();
        turns = new ArrayList<>();
        reservations  = new ArrayList<>();
    }

    public Office(String id, String name, String address, String phone, Integer averageTime, String latitude, String longitude, Timestamp opensAt, Timestamp closesAt, Boolean hasStationsForPreferential, Boolean hasStationsForMemberships, List<Service> services, List<Station> stations, List<Turn> turns, List<Reservation> reservations) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.averageTime = averageTime;
        this.latitude = latitude;
        this.longitude = longitude;
        this.opensAt = opensAt;
        this.closesAt = closesAt;
        this.hasStationsForPreferential = hasStationsForPreferential;
        this.hasStationsForMemberships = hasStationsForMemberships;
        this.services = services;
        this.stations = stations;
        this.turns = turns;
        this.reservations = reservations;
    }

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
    public Integer getAverageTime() {
        return averageTime;
    }

    /**
     * Fija el valor de tiempo de espera.
     * @param averageTime Tiempo de espera.
     */
    public void setAverageTime(Integer averageTime) {
        this.averageTime = averageTime;
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
    public Boolean getHasStationsForPreferential() {
        return hasStationsForPreferential;
    }

    /**
     * Fija si una sucursal tiene estaciones para atención preferencial.
     * @param hasStationsForPreferential true or false
     */
    public void setHasStationsForPreferential(Boolean hasStationsForPreferential) {
        this.hasStationsForPreferential = hasStationsForPreferential;
    }

    /**
     * Devuelve si una sucursal tiene estaciones para membresías.
     * @return True si tiene estaciones para membresías.
     */
    public Boolean getHasStationsForMemberships() {
        return hasStationsForMemberships;
    }

    /**
     * Fija si una sucursal tiene estaciones para membresías.
     * @param hasStationsForMemberships true or false.
     */
    public void setHasStationsForMemberships(Boolean hasStationsForMemberships) {
        this.hasStationsForMemberships = hasStationsForMemberships;
    }

    /**
     * Devuelve la lista de servicios ofrecidos en la sucursal.
     * @return Lista de Service
     */
    public List<Service> getServices() {
        return services;
    }

    /**
     * Fija la lista de servicios ofrecidos.
     * @param services Lista de Service
     */
    public void setServices(List<Service> services) {
        this.services = services;
    }

    /**
     * Devuelve la lista de estaciones en la sucursal.
     * @return Lista de Station
     */
    public List<Station> getStations() {
        return stations;
    }

    /**
     * Fija la lista de estaciones.
     * @param stations Lista de Station.
     */
    public void setStations(List<Station> stations) {
        this.stations = stations;
    }

    /**
     * Devuelve la lista de turnos de generación de tickets.
     * @return Lista de Turn
     */
    public List<Turn> getTurns() {
        return turns;
    }

    /**
     * Fija la lista de turnos por generación de ticket.
     * @param turns Lista de Turn
     */
    public void setTurns(List<Turn> turns) {
        this.turns = turns;
    }

    /**
     * Devuelve la lista de turnos por reservaciones de horarios.
     * @return Lista de Reservation.
     */
    public List<Reservation> getReservations() {
        return reservations;
    }

    /**
     * Fija la lista de turnos por reserva de horarios.
     * @param reservations Lista de Reservation.
     */
    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public Office(Parcel in){
        this.id = in.readString();
        this.name = in.readString();
        this.address = in.readString();
        this.phone = in.readString();
        this.averageTime = in.readInt();
        this.latitude = in.readString();
        this.longitude = in.readString();
        this.opensAt = in.readParcelable(Timestamp.class.getClassLoader());
        this.closesAt = in.readParcelable(Timestamp.class.getClassLoader());
        this.hasStationsForPreferential = in.readByte() != 0;
        this.hasStationsForMemberships = in.readByte() != 0;
        this.services = in.readArrayList(Service.class.getClassLoader());
        this.stations = in.readArrayList(Station.class.getClassLoader());
        this.turns = in.readArrayList(Turn.class.getClassLoader());
        this.reservations = in.readArrayList(Reservation.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(address);
        dest.writeString(phone);
        dest.writeInt(averageTime);
        dest.writeString(latitude);
        dest.writeString(longitude);
        dest.writeParcelable(opensAt, flags);
        dest.writeParcelable(closesAt, flags);
        dest.writeInt(hasStationsForPreferential ? 1 : 0);
        dest.writeInt(hasStationsForMemberships ? 1 : 0);
        dest.writeList(services);
        dest.writeList(stations);
        dest.writeList(turns);
        dest.writeList(reservations);
    }
}
