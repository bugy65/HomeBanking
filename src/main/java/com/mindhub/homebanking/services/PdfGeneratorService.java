package com.mindhub.homebanking.services;

import com.lowagie.text.DocumentException;
import com.mindhub.homebanking.dtos.PdfParamsDTO;
import com.mindhub.homebanking.models.Client;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

public interface PdfGeneratorService {

    void PDFGenerator(HttpServletResponse response, Authentication authentication, PdfParamsDTO pdfParamsDTO) throws IOException, DocumentException;
}
