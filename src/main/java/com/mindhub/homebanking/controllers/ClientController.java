package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.UserAutority;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.services.implement.ClientServiceImplement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDateTime;
import java.util.List;

import static com.mindhub.homebanking.Utils.Utils.RandomNumberGenerate;

@RestController
@RequestMapping("/api")

public class ClientController {

    @Autowired
    ClientServiceImplement clientServiceImplement;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @GetMapping("/clients")
    public List<ClientDTO> getClients(){
        return clientServiceImplement.getClients();


    }

    @GetMapping("clients/{id}")
    public ClientDTO getClient(@PathVariable Long id){
        return clientServiceImplement.getClient(id);
    };

    @PostMapping("/clients")
    public ResponseEntity<Object> register(

            @RequestParam String firstName, @RequestParam String lastName,

            @RequestParam String email, @RequestParam String password) {

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {

            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);

        }

        if (clientServiceImplement.findClientByEmail(email) !=  null) {

            return new ResponseEntity<>("this email already in use", HttpStatus.FORBIDDEN);

        }

        Client client = clientServiceImplement.clientRepository.save(new Client(firstName, lastName, email, passwordEncoder.encode(password),UserAutority.CLIENT));
        accountRepository.save(new Account("VIN-" + RandomNumberGenerate(0,9999999),LocalDateTime.now(),0,client,true, AccountType.CURRENT));
        return new ResponseEntity<>("the client has benn created",HttpStatus.CREATED);




    }
    @GetMapping("clients/current")
    public ClientDTO getClient(Authentication authentication){
        Client client = clientServiceImplement.clientRepository.findByEmail(authentication.getName());
        ClientDTO clientDTO = new ClientDTO(client);
        return clientDTO;


    }


}
