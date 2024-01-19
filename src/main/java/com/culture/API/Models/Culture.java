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
public class Culture implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idCulture;

    @Basic
    private String name;

    @Basic
    private double seedQuantity;

    @Basic
    private double yieldQuantity;

    @Basic
    private int unit; // 1 || 2

    @Basic
    private double seedPrice;

    @Basic
    private double yieldPrice;

    @ManyToOne()
    @JoinColumn(name="idGroundType")
    private GroundType groundType;


    public int getIdCulture() {
        return idCulture;
    }

    public void setIdCulture(int idCulture) {
        this.idCulture = idCulture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getSeedQuantity() {
        return seedQuantity;
    }

    public void setSeedQuantity(double seedQuantity) {
        this.seedQuantity = seedQuantity;
    }

    public double getYieldQuantity() {
        return yieldQuantity;
    }

    public void setYieldQuantity(double yieldQuantity) {
        this.yieldQuantity = yieldQuantity;
    }

    public int getUnit() {
        return unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }

    public double getSeedPrice() {
        return seedPrice;
    }

    public void setSeedPrice(double seedPrice) {
        this.seedPrice = seedPrice;
    }

    public double getYieldPrice() {
        return yieldPrice;
    }

    public void setYieldPrice(double yieldPrice) {
        this.yieldPrice = yieldPrice;
    }

    public GroundType getGroundType() {
        return groundType;
    }

    public void setGroundType(GroundType groundType) {
        this.groundType = groundType;
    }

    public Culture(int idCulture, String name, double seedQuantity, double yieldQuantity, int unit, double seedPrice,
            double yieldPrice, GroundType groundType) {
        this.idCulture = idCulture;
        this.name = name;
        this.seedQuantity = seedQuantity;
        this.yieldQuantity = yieldQuantity;
        this.unit = unit;
        this.seedPrice = seedPrice;
        this.yieldPrice = yieldPrice;
        this.groundType = groundType;
    }

    public Culture(){}





    
}
