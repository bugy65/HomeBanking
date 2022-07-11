package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface ClientService {


    List<ClientDTO> getClients();

     ClientDTO getClient(@PathVariable Long id);

    Client getClientByEmail(Authentication authentication);

    Client findClientByEmail(String email);
}
