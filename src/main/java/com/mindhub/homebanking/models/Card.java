package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private cardType type;
    private cardColorType color;
    private String cardholder;
    private String number;
    private int cvv;
    private LocalDateTime fromDate;
    private LocalDateTime thruDate;
    private Boolean enabled;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="client_id")
    private Client client;

    public Card (){}

    public Card(cardType type, cardColorType color, String number, int cvv, LocalDateTime fromDate, LocalDateTime thruDate, Client client, Boolean enabled) {
        this.cardholder = client.getFullName();
        this.type = type;
        this.color = color;
        this.number = number;
        this.cvv = cvv;
        this.fromDate = fromDate;
        this.thruDate = thruDate;
        this.client = client;
        this.enabled = true;
    }

    public long getId() {return id;}

    public cardType getType() {return type;}

    public void setType(cardType type) {this.type = type;}

    public cardColorType getColor() {return color;}

    public void setColor(cardColorType color) {this.color = color;}

    public String getCardholder() {return cardholder;}

    public void setCardholder(String cardholder) {this.cardholder = cardholder;}

    public String getNumber() {return number;}

    public void setNumber(String number) {this.number = number;}

    public int getCvv() {return cvv;}

    public void setCvv(int cvv) {this.cvv = cvv;}

    public LocalDateTime getFromDate() {return fromDate;}

    public void setFromDate(LocalDateTime fromDate) {this.fromDate = fromDate;}

    public LocalDateTime getThruDate() {return thruDate;}

    public void setThruDate(LocalDateTime thruDate) {this.thruDate = thruDate;}

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
