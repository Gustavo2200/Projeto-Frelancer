package br.com.myfrilas.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.myfrilas.enums.TypeTransaction;

public class Transaction {
    
    private Long id;
    private String entryOrExit;
    private Long idPayer;
    private Long idRecipient;
    private BigDecimal value;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime date;

    private TypeTransaction type;
    

    public Transaction() {}

    public Transaction(Long id, Long idPayer, Long idRecipient, BigDecimal value,
             LocalDateTime date, TypeTransaction type, String entryOrExit) {
        this.id = id;
        this.idPayer = idPayer;
        this.idRecipient = idRecipient;
        this.value = value;
        this.date = date;
        this.type = type;
        this.entryOrExit = entryOrExit;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdPayer() {
        return idPayer;
    }

    public void setIdPayer(Long idPayer) {
        this.idPayer = idPayer;
    }

    public Long getIdRecipient() {
        return idRecipient;
    }

    public void setIdRecipient(Long idRecipient) {
        this.idRecipient = idRecipient;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public TypeTransaction getType() {
        return type;
    }

    public void setType(TypeTransaction type) {
        this.type = type;
    }

    public String getEntryOrExit() {
        return entryOrExit;
    }

    public void setEntryOrExit(String entryOrExit) {
        this.entryOrExit = entryOrExit;
    }
    
}
