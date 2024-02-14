package com.culture.API.Models;

import java.io.Serializable;
import java.util.Date;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import com.culture.API.Repository.ActionRepository;
import com.culture.API.Repository.FieldRepository;
import com.culture.API.Repository.SimulationDetailsRepository;
import com.culture.API.Repository.SimulationRepository;
import com.culture.API.Repository.WalletRepository;
import com.culture.API.Repository.WalletTransactionRepository;
import com.culture.API.Repository.YieldRepository;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

@Entity
public class Simulation implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idSimulation;

    @ManyToOne
    @JoinColumn(name="idPlot")
    private Plot plot;

    @ManyToOne
    @JoinColumn(name="idCulture")
    private Culture culture;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "UTC")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable=false)
    private Date dateSimulation;

    public int getIdSimulation() {
        return idSimulation;
    }

    public void setIdSimulation(int idSimulation) {
        this.idSimulation = idSimulation;
    }

    public Plot getPlot() {
        return plot;
    }

    public void setPlot(Plot plot) {
        this.plot = plot;
    }

    public Culture getCulture() {
        return this.culture;
    }

    public void setCulture(Culture culture) {
        this.culture = culture;
    }

    public Date getDateSimulation() {
        return dateSimulation;
    }

    public void setDateSimulation(Date dateSimulation) {
        this.dateSimulation = dateSimulation;
    }

    public Simulation(int idSimulation, Plot plot, Culture culture, Timestamp dateSimulation) {
        this.idSimulation = idSimulation;
        this.plot = plot;
        this.culture = culture;
        this.dateSimulation = dateSimulation;
    }

    public Simulation()
    {
        
    }

    public Simulation saveSimulation(Simulation simulation, SimulationRepository sr) throws Exception{
        Simulation s = sr.save(simulation);
        return s;
    }

    public List<Simulation> findAllSimulation(SimulationRepository sr) throws Exception{
        List<Simulation> s = sr.findAll();
        return s;
    }

    @Transactional(rollbackFor = Exception.class)
    public Simulation insertSimulation( FieldRepository fr, WalletRepository wr, WalletTransactionRepository wtr,
                                     YieldRepository yr, SimulationRepository sr, SimulationDetailsRepository sdr,
                                     ActionRepository ar, Integer sid , Plot plot, Culture culture, 
                                     Ressource ressource, int quantity ) throws Exception {
        try {

            /* insert Simulation */
            Simulation simulation = new Simulation();
            Simulation s = sid != null? sr.findById(sid).get() : null;

            double price =  ressource.getPricePerUnit() * quantity;

            if( s == null && ressource.getAction().getName().equals("Plantation")){
                
                /** check if able to continue simulation insertion */
                Simulation lastSimulation = sr.findFirstByPlotOrderByDateSimulationDesc(plot);
                if(
                    sdr.findFirstBySimulationAndSimulation_PlotAndRessource_Action_Name(lastSimulation ,plot, "Plantation") != null &&
                    sdr.findFirstBySimulationAndSimulation_PlotAndRessource_Action_Name(lastSimulation, plot, "Recolte") == null
                ){
                    throw new RuntimeException("NOT RECOLTED ON THIS PLOT");
                }
                
                price =  culture.getSeedPrice() * plot.getArea();

                simulation.setPlot(plot);
                simulation.setCulture(culture);
                simulation.setDateSimulation(new Timestamp(System.currentTimeMillis()));

                simulation = simulation.saveSimulation(simulation, sr);
            }else{
                if(s != null && sdr.findFirstBySimulationAndRessource_Action_Name(s, "Recolte") == null){
                    simulation = s ;
                }else{
                    throw new RuntimeException("CLOSED OR UNOPENED SIMULATION");
                }
            }

            /* insert details */
            SimulationDetails simulationDetails = new SimulationDetails();
            simulationDetails.setRessource(ressource);
            simulationDetails.setSimulation(simulation);
            simulationDetails.setQuantity(quantity);
            simulationDetails.setPrice(price);

            /**plot.getField().getOwner().getWallet(). transac(  ); */
            simulation.getPlot().getField(fr).getOwner().getWallet().createTransaction(wtr, wr, price, -1);

            simulationDetails = simulationDetails.saveSimulationDetails(simulationDetails, sdr);

            /* add yield if "recolte" */
            if(ressource.getAction().getName().equals("Recolte")){
                Yield yield = new Yield();
                yield.setSimulation(simulation);
                yield.setDateYield(new Timestamp(System.currentTimeMillis()));
                yield.calculateQuantity(sdr, ar);

                yield.saveYield(yr, yield);
                System.out.println("YIELD : " + yield.getQuantity());
            }
            
            return simulation;
        } catch (Exception e) {
            throw new RuntimeException("INSERT SIMULATION ERROR : " + e);
        }
    }
    
}
