package com.mindhub.homebanking.controllers;


import com.lowagie.text.DocumentException;
import com.mindhub.homebanking.dtos.CardPostnetDTO;
import com.mindhub.homebanking.dtos.PdfParamsDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.services.*;
import com.mindhub.homebanking.services.implement.AccountServiceImplement;
import com.mindhub.homebanking.services.implement.CardServiceImplement;
import com.mindhub.homebanking.services.implement.TransactionServiceImplement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController

@RequestMapping("/api")
public class TransactionController {


    @Autowired
    private AccountRepository accountRepo;

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private CardService cardService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private PdfGeneratorService pdfGeneratorService;

    @Autowired
    private ClientService clientService;
    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    @PostMapping("/transactions")
    public ResponseEntity<Object> transaction(Authentication authentication,
            @RequestParam double amount,@RequestParam String description,@RequestParam String rootAccount,@RequestParam String destinationAccount,Double newBalance
    ){
        Account account = accountRepo.findByNumber(authentication.getName());

        if (rootAccount.isEmpty() || destinationAccount.isEmpty()|| amount <=0 || description.isEmpty()) {

            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);

        }
        if (rootAccount.equals(destinationAccount)) {

            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);

        }
        if (accountRepo.findByNumber(rootAccount) == null) {

            return new ResponseEntity<>("rootAccount doesnt exist", HttpStatus.FORBIDDEN);

        }
        if (!clientRepository.findByEmail(authentication.getName()).getAccounts().stream().map(account1 -> account1.getNumber()).collect(Collectors.toSet()).contains(rootAccount)) {

            return new ResponseEntity<>("account is not aunthenticated", HttpStatus.FORBIDDEN);

        }
        if (accountRepo.findByNumber(destinationAccount) == null) {

            return new ResponseEntity<>("destinationAccount doesnt exist", HttpStatus.FORBIDDEN);

        }
        if (accountRepo.findByNumber(rootAccount).getBalance() < amount){
            return new ResponseEntity<>("la cuenta no tiene saldo suficiente", HttpStatus.FORBIDDEN);
        }

        Account accountDebit = accountRepo.findByNumber(rootAccount);
        Account accountCredit = accountRepo.findByNumber(destinationAccount);

        accountDebit.setBalance(accountDebit.getBalance() - amount);
        accountCredit.setBalance(accountCredit.getBalance() + amount);

        Transaction transactionDebit = new Transaction(TransactionType.DEBITO,0-amount,description + "From" + accountCredit.getNumber(), LocalDateTime.now(), accountDebit,accountDebit.getBalance());
        Transaction transactionCredit = new Transaction(TransactionType.CREDITO,amount,description + "From" + accountDebit.getNumber(), LocalDateTime.now(),accountCredit,accountCredit.getBalance());



        accountRepo.save(accountDebit);
        accountRepo.save(accountCredit);

        transactionRepository.save(transactionDebit);
        transactionRepository.save(transactionCredit);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Transactional
    @PostMapping("/transactions/cardPayment")
    public ResponseEntity<String> cardPostnet(@RequestBody CardPostnetDTO cardPostnetDTO){
        Card card = cardService.FindCard(cardPostnetDTO.getCardNumber());
        Client client = card.getClient();
        String cardHolder = card.getCardholder();
        int cvv = card.getCvv();
        LocalDateTime localDate = card.getThruDate();
        List<Account> account = accountService.getAccountByClient(client).stream().filter(account1 -> account1.getActiveAccount()).collect(Collectors.toList());
        Comparator<Account> balanceComparator = Comparator.comparing(Account::getBalance);
        account.sort(balanceComparator.reversed());


        if (account.get(0).getBalance() < cardPostnetDTO.getAmount()){
            return new ResponseEntity<>("this account does not have enough fund", HttpStatus.FORBIDDEN);
        }
        if (cardPostnetDTO.getCardNumber().isEmpty() || cardPostnetDTO.getAmount() <= 0|| cardPostnetDTO.getCvv() <=0 || cardPostnetDTO.getDescription().isEmpty()) {

            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);

        }
    if (localDate.getYear() < LocalDateTime.now().getYear()|| localDate.getYear() == LocalDateTime.now().getYear() && localDate.getMonthValue() < LocalDateTime.now().getMonthValue() ){
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }


    account.get(0).setBalance(account.get(0).getBalance() - cardPostnetDTO.getAmount());
    accountService.saveAccounts(account.get(0));
    Transaction transaction = new Transaction(TransactionType.DEBITO,cardPostnetDTO.getAmount(),cardPostnetDTO.getDescription(),LocalDateTime.now(),account.get(0),account.get(0).getBalance());
        transactionRepository.save(transaction);
        return new ResponseEntity<>(HttpStatus.CREATED);

    }


    @PostMapping("/transactions/generate")
    public void generatePDF(HttpServletResponse response, Authentication authentication,@RequestBody PdfParamsDTO pdfParamsDTO) throws IOException, DocumentException {
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd:hh::mm");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-disposition";
        String headerValue = "attachment; filename=Vossen_bank_" + currentDateTime + ".pdf";
        response.setHeader(headerKey,headerValue);

        System.out.println(pdfParamsDTO.getAccountNumber());
        pdfGeneratorService.PDFGenerator(response, authentication, pdfParamsDTO);

    }
}
