package br.com.myfrilas.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.myfrilas.enums.TypeTransaction;

public class Transaction {
    
    private Long id;
    private Long idCustomer;
    private Long idFreelancer;
    private BigDecimal value;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime date;

    private TypeTransaction type;

    public Transaction() {}

    public Transaction(Long id, Long idCustomer, Long idFreelancer, BigDecimal value,
             LocalDateTime date, TypeTransaction type) {
        this.id = id;
        this.idCustomer = idCustomer;
        this.idFreelancer = idFreelancer;
        this.value = value;
        this.date = date;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(Long idCustomer) {
        this.idCustomer = idCustomer;
    }

    public Long getIdFreelancer() {
        return idFreelancer;
    }

    public void setIdFreelancer(Long idFreelancer) {
        this.idFreelancer = idFreelancer;
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
    
}
