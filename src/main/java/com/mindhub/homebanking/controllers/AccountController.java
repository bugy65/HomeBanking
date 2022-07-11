package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.implement.AccountServiceImplement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.mindhub.homebanking.Utils.Utils.RandomNumberGenerate;
import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class AccountController {



    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;

    @GetMapping("/accounts")
    public List<AccountDTO> getAccount() {
        return accountService.getAccount();
    }

    @GetMapping("accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id) {
        return accountService.getAccountByID(id);
    }

    @GetMapping("/clients/current/accounts/{id}")
    public AccountDTO getAccountDTO(@PathVariable Long id, Authentication authentication) {
//    Client client = clientRepository.findByEmail(authentication.getName());
        Client client = clientService.getClientByEmail(authentication);
        Set<Account> accounts = client.getAccounts();
        return accounts.stream().filter(account2 -> account2.getId() == id).map(account -> new AccountDTO(account)).findFirst().orElse(null);
    }

    @GetMapping("/clients/current/accounts")
    public Set<AccountDTO> getAccountDTO(Authentication authentication) {
        Client client = clientService.getClientByEmail(authentication);
        return client.getAccounts().stream().filter(account -> account.getActiveAccount() == true).map(account -> new AccountDTO(account)).collect(Collectors.toSet());
    }

    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> createAccount(Authentication authentication,@RequestParam AccountType accountType) {
        Client client = clientRepository.findByEmail(authentication.getName());
        List<Account> accounts = accountRepository.findByClient(client);
        if (accounts.size() < 3) {
            accountService.saveAccounts(new Account("VIN-" + RandomNumberGenerate(0, 9999999), LocalDateTime.now(), 0, client, true, accountType));
            return new ResponseEntity<>(HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>("Max limit account created", HttpStatus.FORBIDDEN);}
    }

    @PostMapping("/accounts/delete")
    public ResponseEntity<String> disableAccount(Authentication authentication, @RequestParam String accountNumber){
        Client client = clientRepository.findByEmail(authentication.getName());
        Account account = accountService.getAccountNumber(accountNumber);
        Double balance = accountService.getAccountNumber(accountNumber).getBalance();


        if (!client.getAccounts().stream().filter(Account::getActiveAccount).map(Account::getNumber).collect(Collectors.toList()).contains(accountNumber)){
            return new ResponseEntity<>("Account does not belong to authenticated client", HttpStatus.FORBIDDEN);
        }
        if(balance > 0){
            return new ResponseEntity<>("Account has a positive balance, it cannot be deleted", HttpStatus.FORBIDDEN);
        }

        if (client.getLoans().size() < 0){
            return new ResponseEntity<>("The client has an active loan. Accounts cannot be deleted until they are paid in full", HttpStatus.FORBIDDEN);
        }

        if (accountService.disableAccount(account)) {
            return new ResponseEntity<>("Account dissabled successfully", HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>("Error", HttpStatus.BAD_REQUEST);
    }
}
