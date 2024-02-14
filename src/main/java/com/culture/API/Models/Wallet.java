package com.culture.API.Models;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.culture.API.Repository.WalletRepository;
import com.culture.API.Repository.WalletTransactionRepository;

import jakarta.persistence.Basic;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Wallet implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idWallet;
    
    @Basic
    private String number;
    
    @Basic
    private double balance;
    
    @OneToOne
    @JoinColumn(name = "idOwner")
    private Owner owner;

    @OneToMany(mappedBy = "wallet", fetch = FetchType.EAGER)
    private List<WalletTransaction> walletTransactions;

    
    public Wallet() {
    }


    public Wallet(String number, double balance, Owner owner) {
        this.number = number;
        this.balance = balance;
        this.owner = owner;
    }
    
    public Wallet(int idWallet, String number, double balance, Owner owner,
            List<WalletTransaction> walletTransactions) {
        this.idWallet = idWallet;
        this.number = number;
        this.balance = balance;
        this.owner = owner;
        this.walletTransactions = walletTransactions;
    }


    public int getIdWallet() {
        return idWallet;
    }

    public void setIdWallet(int idWallet) {
        this.idWallet = idWallet;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    

    public void setUser(Owner owner) {
        this.owner = owner;
    }

    public Wallet updateWallet(WalletRepository wr) throws Exception
    {

        Wallet w = wr.save(this);
        return w;

    }

	public WalletTransaction createTransaction(WalletTransactionRepository wtr, WalletRepository wr, double amount, int type) throws Exception {
        Wallet wallet = this;
        double balance = wallet.getBalance();
        WalletTransaction walletTransaction = new WalletTransaction();
        walletTransaction.setAmount(amount);
        walletTransaction.setType(type);
        walletTransaction.setWallet(wallet);
        if(walletTransaction.getAmount() > balance && type < 0)
        {
            throw new Exception("Insufficient balance");
        }else{
                wallet.setBalance(balance+(type*walletTransaction.getAmount()));
                long currentTimestampMillis = System.currentTimeMillis();
                Date currentDate = new java.util.Date(currentTimestampMillis);
                walletTransaction.setDateTransaction(new Timestamp(currentDate.getTime()));
                WalletTransaction transaction = wtr.save(walletTransaction);
                wallet.updateWallet(wr);
                return transaction;
            } 

	}
    public static Wallet saveWallet(Wallet w, WalletRepository wr) {
        return wr.save(w);
    }

    public static List<Wallet> findAllWallet(WalletRepository wr) {
        return wr.findAll();
    }


}
