package com.mindhub.homebanking.services;


import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

import static java.util.stream.Collectors.toList;

public interface AccountService {

    public void saveAccounts(Account account);


    public List<AccountDTO> getAccount();

    public AccountDTO getAccountByID(@PathVariable Long id);

    Account getAccountNumber(String number);

    boolean disableAccount(Account account);
    List<Account> getAccountAll();

    List<Account> getAccountByClient(Client client);


}
