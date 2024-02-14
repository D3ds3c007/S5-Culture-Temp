package com.culture.API.Models;

import java.io.Serializable;

import jakarta.persistence.Basic;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Action implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idAction;

    @Basic
    private String name;

    @Basic
    private double cons;

    public int getIdAction() {
        return idAction;
    }

    public void setIdAction(int idAction) {
        this.idAction = idAction;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCons(){
        return this.cons;
    }

    public void setCons(double cons){
        this.cons = cons;
    }

    public Action(int idAction, String name) {
        this.idAction = idAction;
        this.name = name;
    }

    public Action()
    {
        
    }
}
