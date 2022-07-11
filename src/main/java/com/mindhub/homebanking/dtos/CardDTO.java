package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.cardColorType;
import com.mindhub.homebanking.models.cardType;

import java.time.LocalDateTime;

public class CardDTO {

    private long id;
    private cardType type;
    private cardColorType color;
    private String cardholder;
    private String number;
    private int cvv;
    private LocalDateTime fromDate;
    private LocalDateTime thruDate;

    private Boolean enabled;
    private Client client;

    public CardDTO (){}

    public CardDTO(Card card) {
        this.id = card.getId();
        this.type = card.getType();
        this.color = card.getColor();
        this.cardholder = card.getCardholder();
        this.number = card.getNumber();
        this.cvv = card.getCvv();
        this.fromDate = card.getFromDate();
        this.thruDate = card.getThruDate();
        this.client = card.getClient();
        this.enabled = card.getEnabled();
    }

    public long getId() {return id;}

    public cardType getType() {return type;}

    public cardColorType getColor() {return color;}

    public String getCardholder() {return cardholder;}

    public String getNumber() {return number;}

    public int getCvv() {return cvv;}

    public LocalDateTime getFromDate() {return fromDate;}

    public LocalDateTime getThruDate() {return thruDate;}


    public Boolean getEnabled() {
        return enabled;
    }
}
