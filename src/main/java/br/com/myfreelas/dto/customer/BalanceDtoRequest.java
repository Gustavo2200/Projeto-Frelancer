package br.com.myfreelas.dto.customer;

import java.math.BigDecimal;

public class BalanceDtoRequest {
    
    private BigDecimal value;

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    
}
