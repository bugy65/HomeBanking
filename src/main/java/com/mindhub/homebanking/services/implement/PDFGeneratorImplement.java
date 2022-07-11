package com.mindhub.homebanking.services.implement;


import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.mindhub.homebanking.dtos.PdfParamsDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.PdfGeneratorService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;

@Service
public class PDFGeneratorImplement implements PdfGeneratorService {
    @Autowired
    ClientService clientService;

    @Autowired
    AccountService accountService;

    @Autowired
    TransactionService transactionService;


    @Override
    public void PDFGenerator(HttpServletResponse response, Authentication authentication, PdfParamsDTO pdfParamsDTO) throws IOException, DocumentException {

        Client client = clientService.getClientByEmail(authentication);
//        Set<Transaction> transactions = account.getTransactions();

//        Set<Transaction> transactionsDate = transactions.stream().filter(transaction -> transaction.getTransactionDate().isAfter(until) && transaction.getTransactionDate().isBefore(since)).collect(Collectors.toSet());

        Set<Transaction> transactions = transactionService.getTransactionsByAccount(pdfParamsDTO.getAccountNumber());

        Set<Transaction> setTransactionFilter = transactions.stream().filter(transaction -> transaction.getDate().isAfter(pdfParamsDTO.getSince()) && transaction.getDate().isBefore(pdfParamsDTO.getUntil())).collect(Collectors.toSet());
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document,response.getOutputStream());

        //// inicia el documento//
        document.open();
        // Titulo
//        Image imageLogo = Image.getInstance("https://i.ibb.co/XWYGVZX/LogoPDF2.png");
////        imageLogo.setWidthPercentage(15f);
//        imageLogo.setAlignment(Element.ALIGN_CENTER);
////        imageLogo.setTransparency(new int[] { 0x50, 0x15 });
//        imageLogo.setAbsolutePosition(0,0);


//        // Creating a Document object
//        ImageData imageData = ImageDataFactory.create(
//                currDir + "/image.jpg");
//
//        // Creating imagedata from image on disk(from given
//        // path) using ImageData object
//        Image img = new Image(imageData);
//
//        // Creating Image object from the imagedata
//        doc.add(img);


        Font fontTitle = FontFactory.getFont(FontFactory.TIMES_BOLD);
        fontTitle.setSize(33);
        fontTitle.setColor(new Color(5, 189, 103));
        Paragraph title = new Paragraph("SKYBANK",fontTitle);
        title.setAlignment(Paragraph.ALIGN_RIGHT);

        Font fontClient = FontFactory.getFont(FontFactory.TIMES_BOLD);
        fontClient.setSize(20);
        Paragraph clientParagraph = new Paragraph("Client:" + client.getFullName(),fontClient);
        clientParagraph.setAlignment(Paragraph.ALIGN_LEFT);
        clientParagraph.setSpacingAfter(3);

        Font fontSubtitle = FontFactory.getFont(FontFactory.TIMES_BOLD);
        fontSubtitle.setSize(20);
        Paragraph subtitle = new Paragraph("List of transactions in Account ",fontSubtitle);
        subtitle.setAlignment(Paragraph.ALIGN_CENTER);
        subtitle.setSpacingAfter(8);

        // Creating a table
        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);

        java.util.List<String> lista = List.of("Date","Account","Description","Type","Amount","Balance");

        lista.forEach(element -> {
            PdfPCell c1 = new PdfPCell(new Phrase(element));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
        });
        Comparator<Transaction> idComparator = Comparator.comparing(Transaction::getId);

        setTransactionFilter.stream().sorted(idComparator.reversed()).forEach(transaction -> {
            table.addCell(transaction.getDate().format(ISO_LOCAL_DATE));
            table.addCell(transaction.getAccount().getNumber() +"");
            table.addCell(transaction.getDescription()+"");
            table.addCell(transaction.getType()+"");
            table.addCell(transaction.getAmount()+"");
            table.addCell(transaction.getNewBalance()+"");
        });
//        document.add(imageLogo);
        document.add(title);
        document.add(clientParagraph);
        document.add(subtitle);
        document.add(table);

        //// finaliza el documento//
        document.close();
    }
}
