package com.culture.API.Models;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import com.culture.API.Repository.ActionRepository;
import com.culture.API.Repository.SimulationDetailsRepository;
import com.culture.API.Repository.YieldRepository;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
public class Yield implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idYield;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="idSimulation")
    private Simulation simulation;

    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp dateYield;

    @Basic
    private double quantity;

    public int getIdYield() {
        return idYield;
    }

    public void setIdYield(int idYield) {
        this.idYield = idYield;
    }

    public Simulation getSimulation() {
        return simulation;
    }

    public void setSimulation(Simulation simulation) {
        this.simulation = simulation;
    }

    public Timestamp getDateYield() {
        return dateYield;
    }

    public void setDateYield(Timestamp dateYield) {
        this.dateYield = dateYield;
    }

    public double getQuantity() {
        return quantity;
    }

    private void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public Yield(int idYield, Simulation simulation, Timestamp dateYield, int quantity) {
        this.idYield = idYield;
        this.simulation = simulation;
        this.dateYield = dateYield;
        this.quantity = quantity;
    }

    public Yield() {

    }

    public Yield saveYield(YieldRepository yr, Yield yield){
        return yr.save(yield);
    }

    public void calculateQuantity(SimulationDetailsRepository sdr, ActionRepository ar) {
        List<SimulationDetails> simDetailsList = sdr.findAllBySimulation(simulation);
        
        double yield_base = simulation.getCulture().getYieldQuantity() * simulation.getPlot().getArea();
        double quantity = yield_base;
        System.out.println("PRICING -----------------------------");
        System.out.println("YIELD BASE : ------------->> " + yield_base);
        List<Action> actions = ar.findAll();

        try {
            
            for (SimulationDetails simulationDetails : simDetailsList) {
                if(simulationDetails.getRessource() != null){
                    double bonus = ( ( simulationDetails.getRessource().getPros() / 100 ) * simulationDetails.getQuantity() ) * yield_base;
                    quantity += bonus;
                    System.out.println("WITH : "+ simulationDetails.getRessource().getName() +" ------------->> PLUS : " + bonus);
                }
            }
    
            for (Action action : actions) {
                List<SimulationDetails> simDetails = sdr.findAllBySimulationAndRessource_Action(simulation, action);
                if(simDetails.size() == 0){
                    double minus = yield_base * (action.getCons() / 100);
                    quantity -= minus;
                    System.out.println("NO : "+ action.getName() +" ------------->> MINUS : " + minus);
                }
            }

            this.setQuantity(quantity);

        } catch (Exception e) {
            System.out.println("YIELD QUANTITY ERROR :"+ e.getMessage());
            throw new RuntimeException("YIELD QUANTITY ERROR :"+ e);
        }
    }

}
