package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class AccountDTO {

    private long id;

    private String number;
    private LocalDateTime creationDate;
    private double balance;

    private Boolean activeAccount;

    private AccountType accountType;

    private Set <TransactionDTO> transactionsDTO = new HashSet<>();
    public AccountDTO(){};


    public AccountDTO(Account account) {
        this.id = account.getId();
        this.number = account.getNumber();
        this.creationDate = account.getCreationDate();
        this.balance = account.getBalance();
        this.transactionsDTO = account.getTransactions().stream().map(TransactionDTO::new).collect(Collectors.toSet());
        this.activeAccount= account.getActiveAccount();
        this.accountType= account.getAccountType();


    }

    public long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public double getBalance() {
        return balance;
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

    public Set<TransactionDTO> getTransactionsDTO() {
        return transactionsDTO;
    }


    public AccountType getAccountType() {
        return accountType;
    }
}
