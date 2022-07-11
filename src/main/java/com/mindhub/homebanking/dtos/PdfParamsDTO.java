package com.mindhub.homebanking.dtos;

import java.time.LocalDateTime;

public class PdfParamsDTO {
    private String numberAccount;

    private LocalDateTime since;
    private LocalDateTime until;

    public PdfParamsDTO() {
    }

    public PdfParamsDTO(String numberAccount, LocalDateTime since, LocalDateTime until) {
        this.numberAccount = numberAccount;
        this.since = since;
        this.until = until;
    }

    public String getAccountNumber() {
        return numberAccount;
    }

    public LocalDateTime getSince() {
        return since;
    }

    public LocalDateTime getUntil() {
        return until;
    }


    public void setNumberAccount(String numberAccount) {
        this.numberAccount = numberAccount;
    }

    public void setSince(LocalDateTime since) {
        this.since = since;
    }

    public void setUntil(LocalDateTime until) {
        this.until = until;
    }
}
