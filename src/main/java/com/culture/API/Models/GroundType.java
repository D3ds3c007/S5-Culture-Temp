package com.culture.API.Models;

import java.io.Serializable;

import jakarta.persistence.Basic;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class GroundType implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idGroundType;

    @Basic
    private String name;

    public int getIdGroundType() {
        return idGroundType;
    }

    public void setIdGroundType(int idGroundType) {
        this.idGroundType = idGroundType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GroundType(int idGroundType, String name) {
        this.idGroundType = idGroundType;
        this.name = name;
    }

    public GroundType()
    {

    }
}
