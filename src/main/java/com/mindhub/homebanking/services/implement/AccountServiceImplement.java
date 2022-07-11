package com.mindhub.homebanking.services.implement;


import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.List;

import static com.mindhub.homebanking.Utils.Utils.RandomNumberGenerate;
import static java.util.stream.Collectors.toList;

@Service
public class AccountServiceImplement implements AccountService {

    @Autowired
    public AccountRepository accountRepository;

    @Override
    public void saveAccounts(Account account){
        accountRepository.save(account);
    }

    @Override
    public List<AccountDTO> getAccount() {
        return accountRepository.findAll().stream().map(AccountDTO::new).collect(toList());
    }

    @Override
    public List<Account> getAccountAll() {
        return accountRepository.findAll();
    }


    @Override
    public AccountDTO getAccountByID(@PathVariable Long id) {
        return accountRepository.findById(id).map(AccountDTO::new).orElse(null);
    }

    @Override
    public Account getAccountNumber(String number){
        return accountRepository.findByNumber(number);
    }

    @Override
    public boolean disableAccount(Account account) {
        account.setActiveAccount(false);
        accountRepository.save(account);
        return true;
    }

    @Override
    public List<Account> getAccountByClient(Client client){return accountRepository.findByClient(client);}


}
