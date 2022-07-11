package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.UserAutority;


import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;



public class ClientDTO {

    private long id;
    private String firstName;
    private String lastName;
    private String email;

    private UserAutority userAutority;
    private Set <AccountDTO> accountDTO = new HashSet<>();
    private Set <ClientLoanDTO> clientLoanDTO = new HashSet<>();

    private Set <CardDTO> cards = new HashSet<>();
    public ClientDTO(){};

    public ClientDTO(Client client) {
        this.id = client.getId();
        this.firstName = client.getFirstName();
        this.lastName = client.getLastName();
        this.email = client.getEmail();
        this.accountDTO = client.getAccounts().stream().map(AccountDTO::new).collect(Collectors.toSet());
        this.clientLoanDTO = client.getClientLoans().stream().map(ClientLoanDTO::new).collect(Collectors.toSet());
        this.cards = client.getCards().stream().map(CardDTO::new).collect(Collectors.toSet());
        this.userAutority = client.getUserAutority();
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }


    public String getLastName() {
        return lastName;
    }


    public String getEmail() {
        return email;
    }


    public Set<AccountDTO> getAccounts() {
        return accountDTO;
    }


    public Set<ClientLoanDTO> getLoans() {return clientLoanDTO;}

    public Set<CardDTO> getCards() {
        return cards;
    }

    public UserAutority getUserAutority() {
        return userAutority;
    }
}
