package com.culture.API.Models;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.Basic;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;

import com.culture.API.Repository.*;

@Entity
public class GroundType implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idGroundType;

    @Basic
    private String name;

    @OneToMany(mappedBy = "groundType", fetch=FetchType.EAGER)
    @Transient
    private List<Culture> cultures;


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

    public List<Culture> getCultures() {
        return cultures;
    }

    public void setCultures(List<Culture> cultures) {
        this.cultures = cultures;
    }

    public GroundType(int idGroundType, String name, List<Culture> cultures) {
        this.idGroundType = idGroundType;
        this.name = name;
        this.cultures = cultures;
    }

    public GroundType(){

    }

    public static GroundType saveGroundType(GroundType groundType, GroundTypeRepository groundTypeRepository){
        GroundType groundType2 = groundTypeRepository.save(groundType);
        return groundType2;
    }

    public static List<GroundType> listGroundType(GroundTypeRepository groundTypeRepository){
        List<GroundType> groundType = groundTypeRepository.findAll();
        return groundType;
    }

    public static GroundType findGroundTypeById(int id, GroundTypeRepository or)
    {
        GroundType ow = or.findByIdGroundType(id);
        return ow;
    }

}
