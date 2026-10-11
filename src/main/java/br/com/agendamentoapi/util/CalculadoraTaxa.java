package br.com.agendamentoapi.util;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class CalculadoraTaxa {

    private static final BigDecimal TAXA_FIXA_3 = new BigDecimal("3.00");
    private static final BigDecimal TAXA_FIXA_12 = new BigDecimal("12.00");

    private static final BigDecimal PERCENTUAL_MESMO_DIA    = new BigDecimal("0.025");
    private static final BigDecimal PERCENTUAL_11_A_20_DIAS = new BigDecimal("0.082");
    private static final BigDecimal PERCENTUAL_21_A_30_DIAS = new BigDecimal("0.069");
    private static final BigDecimal PERCENTUAL_31_A_40_DIAS = new BigDecimal("0.047");
    private static final BigDecimal PERCENTUAL_41_A_50_DIAS = new BigDecimal("0.017");

    private static final int ESCALA_DECIMAL = 2;
    private static final RoundingMode MODO_ARREDONDAMENTO = RoundingMode.HALF_UP;

    /**
     * Calcula a taxa de transferência conforme a quantidade de dias
     * entre o agendamento e a realização da transferência.
     *
     * @param valor valor da transferência
     * @param dias quantidade de dias até a transferência
     * @return valor da taxa calculada, arredondado para duas casas decimais
     * @throws IllegalArgumentException se os dias forem negativos
     *                                  ou superiores a 50
     */
    public BigDecimal calcular(BigDecimal valor, long dias) {

        validarDias(dias);

        if (dias == 0) {
            return calcularPercentual(valor, PERCENTUAL_MESMO_DIA)
                    .add(TAXA_FIXA_3)
                    .setScale(ESCALA_DECIMAL, MODO_ARREDONDAMENTO);
        }

        if (dias <= 10) {
            return TAXA_FIXA_12;
        }

        if (dias <= 20) {
            return calcularPercentual(valor, PERCENTUAL_11_A_20_DIAS);
        }

        if (dias <= 30) {
            return calcularPercentual(valor, PERCENTUAL_21_A_30_DIAS);
        }

        if (dias <= 40) {
            return calcularPercentual(valor, PERCENTUAL_31_A_40_DIAS);
        }

        if (dias <= 50) {
            return calcularPercentual(valor, PERCENTUAL_41_A_50_DIAS);
        }

        throw new IllegalArgumentException(
                "Não existe taxa aplicável para essa data de transferência."
        );
    }

    private BigDecimal calcularPercentual(
            BigDecimal valor,
            BigDecimal percentual
    ) {
        return valor.multiply(percentual)
                .setScale(ESCALA_DECIMAL, MODO_ARREDONDAMENTO);
    }

    private void validarDias(long dias) {
        if (dias < 0) {
            throw new IllegalArgumentException(
                    "A data da transferência não pode ser anterior à data do agendamento."
            );
        }
    }
}
