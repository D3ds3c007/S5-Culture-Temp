package com.culture.API.Models;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.Basic;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import com.culture.API.Repository.SimulationDetailsRepository;

@Entity
public class SimulationDetails implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idDetails;

    @ManyToOne
    @JoinColumn(name = "idSimulation")
    private Simulation simulation;

    @ManyToOne
    @JoinColumn(name="idRessource")
    private Ressource ressource;

    @Basic
    private int quantity;

    @Basic
    private double price;

    public int getIdDetails() {
        return idDetails;
    }

    public void setIdDetails(int idDetails) {
        this.idDetails = idDetails;
    }
    
    public Simulation getSimulation() {
        return simulation;
    }

    public void setSimulation(Simulation simulation) {
        this.simulation = simulation;
    }

    public Ressource getRessource() {
        return ressource;
    }

    public void setRessource(Ressource ressource) {
        this.ressource = ressource;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    } 

    public SimulationDetails(int idDetails, Simulation simulation, Ressource ressource, int quantity, double price) {
        this.idDetails = idDetails;
        this.simulation = simulation;
        this.ressource = ressource;
        this.quantity = quantity;
        this.price = price;
    }

    public SimulationDetails()
    {
        
    }

    public SimulationDetails saveSimulationDetails(SimulationDetails simulationDetails, SimulationDetailsRepository sdr) throws Exception{
        SimulationDetails s = sdr.save(simulationDetails);
        return s;
    }

    public List<SimulationDetails> findAllSimulationDetails(SimulationDetailsRepository sdr) throws Exception{
        List<SimulationDetails> s = sdr.findAll();
        return s;
    }
    
}
