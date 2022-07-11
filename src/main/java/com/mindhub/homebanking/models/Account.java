package com.mindhub.homebanking.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private String number;
    private LocalDateTime creationDate;
    private double balance;

    private Boolean activeAccount;

    private AccountType accountType;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="client_id")

    private Client client;

    @OneToMany(mappedBy="account" ,fetch = FetchType.EAGER)
    private Set<Transaction> transactions = new HashSet<>();

    public Account(){
    }

    public Account(String number, LocalDateTime creationDate, double balance, Client client,Boolean activeAccount,AccountType accountType) {
        this.number = number;
        this.creationDate = creationDate;
        this.balance = balance;
        this.client = client;
        this.activeAccount = true;
        this.accountType = accountType;

    }




    public long getId() {
        return this.id;
    }


    public String getNumber() {
        return this.number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public LocalDateTime getCreationDate() {
        return this.creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public double getBalance() {
        return this.balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Boolean getActiveAccount() {
        return activeAccount;
    }

    public void setActiveAccount(Boolean activeAccount) {
        this.activeAccount = activeAccount;
    }

    @JsonIgnore
    public Client getClient() {return this.client;
    }

    public void setAccount(Client client) {
        this.client=client;
    }

    public Set<Transaction> getTransactions() {
        return transactions;
    }


    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }
}
