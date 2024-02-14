package com.culture.API.Models;

import jakarta.persistence.Basic;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import com.culture.API.Repository.*;
import java.util.List;

@Entity
public class Ressource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idRessource;

    @Basic
    private String name;

    @Basic
    private double pricePerUnit;

    @ManyToOne
    @JoinColumn(name="idAction")
    private Action action;

    @Basic
    private double pros;

    public Ressource() {

    }

    public int getIdRessource() {
        return idRessource;
    }

    public void setIdRessource(int idRessource) {
        this.idRessource = idRessource;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public Action getAction(){
        return this.action;
    }

    public void setAction(Action action){
        this.action = action;
    }

    public double getPros(){
        return this.pros;
    }

    public void setPros(double pros){
        this.pros = pros;
    }

    public Ressource(int idRessource, String name, double pricePerUnit, Action action) {
        this.idRessource = idRessource;
        this.name = name;
        this.pricePerUnit = pricePerUnit;
        this.action = action; 
    }


    public static Ressource saveRessource(Ressource ressource, RessourceRepository rr) throws Exception{
        Ressource rs = rr.save(ressource);

        return rs;
    }

    public static List<Ressource> findAll(RessourceRepository rr) throws Exception{
        List<Ressource> r = rr.findAll();

        return r;
    }

}
