
package br.com.agendamentoapi.util;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class CalculadoraTaxa {

    private static final BigDecimal TAXA_ATE_10_DIAS  = new BigDecimal("0.025");
    private static final BigDecimal TAXA_21_A_30_DIAS = new BigDecimal("0.082");
    private static final BigDecimal TAXA_31_A_40_DIAS = new BigDecimal("0.069");
    private static final BigDecimal TAXA_41_A_50_DIAS = new BigDecimal("0.047");
    private static final BigDecimal TAXA_FIXA         = new BigDecimal("12.00");
    private static final BigDecimal ZERO              = BigDecimal.ZERO;

    public BigDecimal calcular(BigDecimal valor, long dias) {

        validarDias(dias);

        BigDecimal taxa = obterTaxa(dias);

        if (dias <= 10) {
            return valor.multiply(taxa)
                    .add(TAXA_FIXA)
                    .setScale(2, RoundingMode.HALF_UP);
        }

        return valor.multiply(taxa)
                .setScale(2, RoundingMode.HALF_UP);
    }

    private void validarDias(long dias) {
        if (dias < 0) {
            throw new IllegalArgumentException(
                    "A data da transferência não pode ser anterior à data do agendamento."
            );
        }
    }

    private BigDecimal obterTaxa(long dias) {
        if (dias <= 10) {
            return TAXA_ATE_10_DIAS;
        }

        if (dias <= 20) {
            return ZERO;
        }

        if (dias <= 30) {
            return TAXA_21_A_30_DIAS;
        }

        if (dias <= 40) {
            return TAXA_31_A_40_DIAS;
        }

        if (dias <= 50) {
            return TAXA_41_A_50_DIAS;
        }

        throw new IllegalArgumentException(
                "Não existe taxa aplicável para essa data de transferência."
        );
    }
}
