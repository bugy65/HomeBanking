package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientLoanDTO;
import com.mindhub.homebanking.services.implement.ClientLoanServiceImplement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api")
@RestController
public class ClientLoanController {

    @Autowired
    ClientLoanServiceImplement clientLoanServiceImplement;

    @GetMapping("/clientLoans")
    public List<ClientLoanDTO> getClientsLeans() {
        return clientLoanServiceImplement.getClientsLoans();
    }

    @GetMapping("/clientLoans/{id}")
    public ClientLoanDTO getClientLoans(@PathVariable Long id) {
        return clientLoanServiceImplement.getClientLoans(id);
    }
}

