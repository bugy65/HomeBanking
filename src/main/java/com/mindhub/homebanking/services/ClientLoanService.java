package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.ClientLoanDTO;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface ClientLoanService {

    public List<ClientLoanDTO> getClientsLoans();

    public ClientLoanDTO getClientLoans(@PathVariable Long id);
}
