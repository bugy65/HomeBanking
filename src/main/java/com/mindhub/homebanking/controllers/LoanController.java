package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.LoanService;
import com.mindhub.homebanking.services.implement.LoanServiceImplement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RequestMapping("/api")
@RestController
public class LoanController {

    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private ClientLoanRepository clientLoanRepository;
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    LoanServiceImplement loanServiceImplement;

    @GetMapping("/loans")
    public List<LoanDTO> getLoansDTO(){ return loanServiceImplement.getLoansDTO();}

    @Autowired
    private ClientService clientService;

    @Autowired
    private LoanService loanService;

    @Transactional
    @PostMapping("/loans")
    public ResponseEntity<Object> CreateLoans(
            @RequestBody LoanApplicationDTO loanApplicationDTO,Authentication authentication
    ){

        Account destinationAccount = accountRepository.findByNumber(loanApplicationDTO.getAccountNumber());
        Client client = clientRepository.findByEmail(authentication.getName());
        Loan currentLoan = loanRepository.findById(loanApplicationDTO.getLoanId()).orElse(null);
        Set<Loan> LoanTaken = client.getLoans().stream().map(ClientLoanDTO -> loanRepository.findById(ClientLoanDTO.getId()).orElse(null)).collect(Collectors.toSet());
        if (loanApplicationDTO.getAmount() <= 0 || loanApplicationDTO.getPayments() <= 0) {

            return new ResponseEntity<>("Hay campos sin completar", HttpStatus.FORBIDDEN);

        }
        if (currentLoan == null) {

            return new ResponseEntity<>("El tipo Prestamo no existe", HttpStatus.FORBIDDEN);

        }
        if (currentLoan.getMaxAmount() < loanApplicationDTO.getAmount()) {

            return new ResponseEntity<>("excede el maximo permitido", HttpStatus.FORBIDDEN);

        }
        if (!currentLoan.getPayments().contains(loanApplicationDTO.getPayments())) {

            return new ResponseEntity<>("las cuotas agregadas no estan disponibles", HttpStatus.FORBIDDEN);

        }
        if (accountRepository.findByNumber(loanApplicationDTO.getAccountNumber()) == null) {

            return new ResponseEntity<>("La cuenta no existe", HttpStatus.FORBIDDEN);

        }
        if (!client.getAccounts().stream().map(Account::getNumber).collect(Collectors.toList()).contains(loanApplicationDTO.getAccountNumber())) {

            return new ResponseEntity<>("La cuenta no es tuya", HttpStatus.FORBIDDEN);

        }
        if(LoanTaken.contains(currentLoan)){
            return new ResponseEntity<>("Ya hay un tipo prestamo solicitado", HttpStatus.FORBIDDEN);
        }
        Double amountInterest = loanApplicationDTO.getAmount() * (currentLoan.getInterest() * 0.01 + 1);

        destinationAccount.setBalance(destinationAccount.getBalance() + loanApplicationDTO.getAmount());

        Transaction transaction = new Transaction(TransactionType.CREDITO,loanApplicationDTO.getAmount(),currentLoan.getName() + " Loan approved",LocalDateTime.now(),destinationAccount, destinationAccount.getBalance());

        ClientLoan clientLoan = new ClientLoan(amountInterest,loanApplicationDTO.getPayments(),currentLoan,client);

        accountRepository.save(destinationAccount);
        transactionRepository.save(transaction);
        clientLoanRepository.save(clientLoan);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

//    @PostMapping("/loan")
//    public ResponseEntity<Object>createLoan(Authentication authentication,@RequestBody LoanDTO loanDTO){
//        Client client = clientService.getClientByEmail(authentication);
//        List<Loan> loanExist = loanServiceImplement.getLoans();
//    }


    @PostMapping("/loans/createLoan")
    public ResponseEntity<Object>addNewLoan(Authentication authentication,@RequestBody LoanDTO newLoanDTO){

        Client client = clientService.getClientByEmail(authentication);
        if (!client.getEmail().contains("admin")){
            return new ResponseEntity<>("Only admins can enter here",HttpStatus.FORBIDDEN);
        }
        if (loanService.getLoans().stream().map(loan -> loan.getName()).collect(Collectors.toSet()).contains(newLoanDTO.getName())){
            return new ResponseEntity<>("Loan already exists, enter another name",HttpStatus.FORBIDDEN);
        }
        if(newLoanDTO.getName().isEmpty() || newLoanDTO.getMaxAmount() <= 0 || newLoanDTO.getPayments().isEmpty() || newLoanDTO.getInterest() <= 0){
            return new ResponseEntity<>("Missing Data",HttpStatus.FORBIDDEN);
        }

        Loan loan = new Loan(newLoanDTO.getName(), newLoanDTO.getMaxAmount(), newLoanDTO.getInterest(),newLoanDTO.getPayments());
        loanService.newLoan(loan);

        return new ResponseEntity<>("Created",HttpStatus.CREATED);
    }
}
