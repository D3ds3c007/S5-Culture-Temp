package com.culture.API.Models;

import java.io.Serializable;

import jakarta.persistence.Basic;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
@Entity
public class Field implements Serializable{
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private int idField;
    
    @Basic
    private double latitude;

    @Basic
    private double longitude;

    
    @ManyToOne()
    @JoinColumn(name = "idOwner")
    private Owner owner;


    public Field() {
    }

    public int getIdField() {
        return idField;
    }

    public void setIdField(int idField) {
        this.idField = idField;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

   
    

    
}
