package com.mindhub.homebanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private String name;
    private double maxAmount;

    private Double interest;

    @ElementCollection
    @Column(name = "payments")
    private List<Integer> payments=new ArrayList<>();

    @OneToMany (mappedBy ="loan",fetch = FetchType.EAGER)
    private List<ClientLoan> clientLoans = new ArrayList<>();


    public Loan(){}

    public Loan(String name, double maxAmount,Double interest, List<Integer> payments) {


        this.name = name;
        this.maxAmount = maxAmount;
        this.interest = interest;
        this.payments = payments;
    }

    @JsonIgnore
    public List<Client> GetClients(){return clientLoans.stream().map(ClientLoan::getClient).collect(Collectors.toList());}


    public long getId() {return id;}

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public double getMaxAmount() {return maxAmount;}

    public void setMaxAmount(int maxAmount) {this.maxAmount = maxAmount;}

    public List<Integer> getPayments() {return payments;}

    public void setPayments(List<Integer> payments) {this.payments = payments;}

    public List<ClientLoan> getClientLoans() {
        return clientLoans;
    }

    public void setClientLoans(List<ClientLoan> clientLoans) {
        this.clientLoans = clientLoans;
    }

    public Double getInterest() {
        return interest;
    }

    public void setInterest(Double interest) {
        this.interest = interest;
    }
}
