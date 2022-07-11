package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.models.Loan;

public class ClientLoanDTO {
    private long id;

    private long idLoan;

    private String name;
    private Double amount;
    private int payments;





    public ClientLoanDTO(){}

    public ClientLoanDTO(ClientLoan clientLoan) {
        this.id = clientLoan.getId();
        this.idLoan = clientLoan.getLoan().getId();
        this.name = clientLoan.getLoan().getName();
        this.amount = clientLoan.getAmount();
        this.payments = clientLoan.getPayments();


    }


    public long getId() {return id;}

    public long getIdLoan() {
        return idLoan;
    }

    public String getName() {
        return name;
    }

    public Double getAmount() {return amount;}

    public int getPayments() {return payments;}


}
