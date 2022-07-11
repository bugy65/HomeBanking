package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.implement.CardServiceImplement;
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
public class CardController {


    @Autowired
    private CardRepository cardRepo;
    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private CardService cardService;

    @Autowired
    private ClientService clientService;

    //@RequestMapping("/cards")
    //public List<CardDTO> getCard(){
    //return cardRepo.findAll().stream().map(CardDTO::new).collect(toList());
    //}


    @PostMapping("/clients/current/cards")

    public ResponseEntity<Object> cardCreation(

            @RequestParam cardType type, @RequestParam cardColorType color, Authentication authentication) {

        Client client = clientRepository.findByEmail(authentication.getName());


        if (client.getCards().stream().filter(card -> card.getEnabled() == true).filter(card -> card.getType().toString().equals(type.CREDIT)).collect(Collectors.toList()).size() >= 3) {
            return new ResponseEntity<>("Cannot obtain more than 3 cards for type", HttpStatus.FORBIDDEN);
        }
        if (client.getCards().stream().filter(card -> card.getEnabled() == true).filter(card -> card.getType().toString().equals(type.DEBIT)).collect(Collectors.toList()).size() >= 3) {
            return new ResponseEntity<>("Cannot obtain more than 3 cards for type", HttpStatus.FORBIDDEN);
        }
        cardRepo.save(new Card(type, color, RandomNumberGenerate(0, 9999) + "-" + RandomNumberGenerate(0, 9999) + "-" + RandomNumberGenerate(0, 9999) + "-" + RandomNumberGenerate(0, 9999), RandomNumberGenerate(0, 999), LocalDateTime.now(), LocalDateTime.now().plusYears(2), client,true));
        return new ResponseEntity<>(HttpStatus.CREATED);

    }




    @GetMapping("/clients/current/cards")
    public Set<CardDTO> getClient(Authentication authentication){
        Client client = clientRepository.findByEmail(authentication.getName());
        return client.getCards().stream().filter(card -> card.getEnabled() == true).map(CardDTO::new).collect(Collectors.toSet());
        //return client.getCards().stream().map(CardDTO::new).collect(toList());


    }

    @GetMapping("/clients/current/cards/{id}")
    public Set<Card> deleteCards(Authentication authentication, @PathVariable Long id){
        Client client = clientRepository.findByEmail(authentication.getName());
        return client.getCards().stream().filter(card -> card.getId() != id).collect(Collectors.toSet());

    }

    @PostMapping("/cards/delete")
    public ResponseEntity<String> logicCardDeletion(Authentication authentication,@RequestParam String cardNumber){
        Client client = clientService.getClientByEmail(authentication);
        Card card = cardService.FindCard(cardNumber);

        if(card == null){
            return new ResponseEntity<>("Card does not exist", HttpStatus.FORBIDDEN);
        }
        if (!client.getCards().contains(card)) {
            return new ResponseEntity<>("Account doesn't belong to authenticated client", HttpStatus.FORBIDDEN);
        }

        if (cardService.deleteCard(card)) {
            return new ResponseEntity<String>("Deletion succeed, please wait confirmation from our agents", HttpStatus.ACCEPTED);
        }

        return new ResponseEntity<String>("something wrong happened, please contact our Service Office", HttpStatus.BAD_REQUEST);
    }


}
