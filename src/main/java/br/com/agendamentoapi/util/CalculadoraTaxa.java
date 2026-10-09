package br.com.agendamentoapi.util;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class CalculadoraTaxa {

    public BigDecimal calcular(BigDecimal valor, long dias) {

        if (dias < 0) {
            throw new IllegalArgumentException(
                    "A data da transferência não pode ser anterior à data do agendamento."
            );
        }

        if (dias <= 10) {
            return valor.multiply(new BigDecimal("0.025"))
                    .add(new BigDecimal("12.00"))
                    .setScale(2, RoundingMode.HALF_UP);
        }

        if (dias <= 20) {
            return BigDecimal.ZERO.setScale(2);
        }

        if (dias <= 30) {
            return valor.multiply(new BigDecimal("0.082"))
                    .setScale(2, RoundingMode.HALF_UP);
        }

        if (dias <= 40) {
            return valor.multiply(new BigDecimal("0.069"))
                    .setScale(2, RoundingMode.HALF_UP);
        }

        if (dias <= 50) {
            return valor.multiply(new BigDecimal("0.047"))
                    .setScale(2, RoundingMode.HALF_UP);
        }

        throw new IllegalArgumentException(
                "Não existe taxa aplicável para essa data de transferência."
        );
    }
}
