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
import jakarta.persistence.OneToOne;

@Entity
public class Owner implements Serializable
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idOwner;

    @Basic
    private String name;

    @Basic
    private String email;

    @Basic
    private String pwd;

    @OneToOne(mappedBy = "owner")
    private Wallet wallet;

    @Basic 
    private int privilege;

    @OneToMany(mappedBy = "owner" , fetch = FetchType.EAGER)
    private List<Field> fields;

   

    public Owner() {
    }

    public int getIdOwner() {
        return idOwner;
    }

    public void setIdUser(int idUser) {
        this.idOwner = idUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public int getPrivilege() {
        return privilege;
    }

    public void setPrivilege(int privilege) {
        this.privilege = privilege;
    }


    



}
