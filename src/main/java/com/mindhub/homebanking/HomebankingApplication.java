package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Bean
	public CommandLineRunner initData(ClientRepository repositoryClient, AccountRepository repositoryAccount, TransactionRepository repositoryTransaction, LoanRepository loanTransaction, ClientLoanRepository clientLoanTransaction, CardRepository cardRepository) {
		return (args) -> {
			String mensaje1 = "DATA LOADED";

			System.out.println(mensaje1);


			// save a couple of customers
			Client client0 = new Client("admin", "admin", "admin@admin.com",passwordEncoder.encode("admin"),UserAutority.ADMIN);
			Client client1 = new Client("Melba", "Morel","melba@mindhub.com",passwordEncoder.encode("melba"),UserAutority.CLIENT);
			Client client2= new Client("Valentino", "Arena","xdxdene@gmail.com",passwordEncoder.encode("hola777"),UserAutority.CLIENT);
			Account accountone= new Account("VIN-001", LocalDateTime.now(),5000, client1, true,AccountType.SAVINGS);
			Account accounttwo= new Account("VIN-002", LocalDateTime.now().plusDays(1),7500, client1, true,AccountType.CURRENT);
			Account account3= new Account("VIN-003", LocalDateTime.now(),950500, client0, true,AccountType.CURRENT);

			Transaction transactionOne= new Transaction(TransactionType.DEBITO,550,"Apple Inc.",LocalDateTime.now(), account3,account3.getBalance());
			Transaction transactiontwo= new Transaction(TransactionType.CREDITO,120,"Pinterest. ",LocalDateTime.now(), account3,account3.getBalance());
			Transaction transactionthree= new Transaction(TransactionType.DEBITO,70,"Warner Bros.",LocalDateTime.now(), account3,account3.getBalance());
			Transaction transactionFour= new Transaction(TransactionType.CREDITO,430,"Riot Games RP",LocalDateTime.now(), account3,account3.getBalance());

			repositoryClient.save(client0);
			repositoryClient.save(client1);
			repositoryClient.save(client2);

			repositoryAccount.save(accountone);
			repositoryAccount.save(accounttwo);
			repositoryAccount.save(account3);
			repositoryTransaction.save(transactionOne);
			repositoryTransaction.save(transactiontwo);
			repositoryTransaction.save(transactionthree);
			repositoryTransaction.save(transactionFour);

			Loan loan1 = new Loan("Mortgage", 500000,15d,List.of(12,24,36,48,60));
			Loan loan2 = new Loan("Personal", 100000,12d,List.of(6,12,24));
			Loan loan3 = new Loan("Car", 300000,17d,List.of(6,12,24,36));
			loanTransaction.save(loan1);
			loanTransaction.save(loan2);
			loanTransaction.save(loan3);

			ClientLoan clientloan1 = new ClientLoan(400000.0,60,loan1,client1);
			ClientLoan clientloan2 = new ClientLoan(50000.0,12,loan2,client2);

			ClientLoan clientloan3 = new ClientLoan(100000.0,24,loan2,client2);
			ClientLoan clientloan4 = new ClientLoan(200000.0,36,loan3,client2);


			ClientLoan clientloan5 = new ClientLoan(400000.0,60,loan1,client0);
			ClientLoan clientloan6 = new ClientLoan(50000.0,12,loan2,client0);

			clientLoanTransaction.save(clientloan1);
			clientLoanTransaction.save(clientloan2);
			clientLoanTransaction.save(clientloan3);
			clientLoanTransaction.save(clientloan4);


			clientLoanTransaction.save(clientloan5);
			clientLoanTransaction.save(clientloan6);

			Card card1= new Card(cardType.DEBIT, cardColorType.GOLD, "52194723748329172", 543, LocalDateTime.now(), LocalDateTime.now().plusYears(5), client1,true);
			Card card2= new Card(cardType.CREDIT, cardColorType.TITANIUM, "42154233748365894", 898, LocalDateTime.now(), LocalDateTime.now().plusYears(5), client1,true);


			Card card3= new Card(cardType.CREDIT, cardColorType.SILVER, "52123454748365894", 898, LocalDateTime.now(), LocalDateTime.now().plusYears(5), client2,true);

			Card card4= new Card(cardType.DEBIT, cardColorType.TITANIUM, "4256473859403843", 665,LocalDateTime.now(),LocalDateTime.now().minusYears(1),client0,true);
			Card card5= new Card(cardType.DEBIT, cardColorType.TITANIUM, "5256473859403653", 665,LocalDateTime.now(),LocalDateTime.now().plusDays(15),client0,true);
			cardRepository.save(card1);
			cardRepository.save(card2);
			cardRepository.save(card3);
			cardRepository.save(card4);
			cardRepository.save(card5);
		};
	}
}
