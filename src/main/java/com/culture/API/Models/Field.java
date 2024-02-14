package com.culture.API.Models;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

import com.culture.API.Repository.FieldRepository;
import com.culture.API.Repository.GroundTypeRepository;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Basic;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Field implements Serializable{
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private int idField;

    @Basic
    private String hashcode;

    @Basic
    private String description;
    
    @Basic
    private String location;

    @Basic
    private double area;

    @ManyToOne()
    @JoinColumn(name = "idOwner")

    private Owner owner;

    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "idPlot")
    @OneToMany(mappedBy = "field", fetch = FetchType.EAGER)
    private List<Plot> plots;

    
    public Field() {

    }
    
    public Field(int idField, String hashcode, String location, String description, double area) {
        this.idField = idField;
        this.hashcode = hashcode;
        this.location = location;
        this.description = description;
        this.area = area;
    }

    public int getIdField() {
        return idField;
    }

    public void setIdField(int idField) {
        this.idField = idField;
    }

    public String getHashcode() {
        return hashcode;
    }

    public void setHashcode(String hashcode) {
        this.hashcode = hashcode;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public List<Plot> getPlots() {
        return plots;
    }

    public void setPlots(List<Plot> plots) {
        this.plots = plots;
    }

    public static Field saveField(Field f, FieldRepository field) throws SQLException{
        Field fi = field.save(f);
        return fi;
    }

    public static List<Field> findAll(FieldRepository fi) throws SQLException{
        List<Field> listFields = fi.findAll();
        return listFields;
    }

    public static List<Field> findFieldByOwner(FieldRepository fi, Owner owner)
    {
        List<Field> listField = fi.findByOwner(owner);
        return listField;
    }

    public static Field findByHashcode(String hashcode, FieldRepository or)
    {
        Field ow = or.findByHashcode(hashcode);
        return ow;
    }
}
