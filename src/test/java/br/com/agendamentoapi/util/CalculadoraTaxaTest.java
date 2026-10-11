package br.com.agendamentoapi.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CalculadoraTaxaTest {

    private CalculadoraTaxa calculadoraTaxa;

    @BeforeEach
    void setUp() {
        calculadoraTaxa = new CalculadoraTaxa();
    }

    @Test
    void deveCalcularTaxaQuandoTransferenciaForNoMesmoDia() {
        BigDecimal valorTransferencia = new BigDecimal("1000.00");
        long diasAteTransferencia = 0;
        BigDecimal taxaEsperada = new BigDecimal("28.00");

        BigDecimal taxaCalculada = calculadoraTaxa.calcular(
                valorTransferencia,
                diasAteTransferencia
        );

        assertEquals(taxaEsperada, taxaCalculada);
    }


    @Test
    void deveCalcularTaxaAte10Dias() {
        BigDecimal valorTransferencia = new BigDecimal("1000.00");
        long diasAteTransferencia = 10;
        BigDecimal taxaEsperada = new BigDecimal("12.00");

        BigDecimal taxaCalculada = calculadoraTaxa.calcular(
                valorTransferencia,
                diasAteTransferencia
        );

        assertEquals(taxaEsperada, taxaCalculada);
    }

    @Test
    void deveCalcularTaxaDe11A20Dias() {
        BigDecimal valorTransferencia = new BigDecimal("1000.00");
        long diasAteTransferencia = 15;
        BigDecimal taxaEsperada = new BigDecimal("82.00");

        BigDecimal taxaCalculada = calculadoraTaxa.calcular(
                valorTransferencia,
                diasAteTransferencia
        );

        assertEquals(taxaEsperada, taxaCalculada);
    }

    @Test
    void deveCalcularTaxaDe21A30Dias() {
        BigDecimal valorTransferencia = new BigDecimal("1000.00");
        long diasAteTransferencia = 25;
        BigDecimal taxaEsperada = new BigDecimal("69.00");

        BigDecimal taxaCalculada = calculadoraTaxa.calcular(
                valorTransferencia,
                diasAteTransferencia
        );

        assertEquals(taxaEsperada, taxaCalculada);
    }

    @Test
    void deveCalcularTaxaDe31A40Dias() {
        BigDecimal valorTransferencia = new BigDecimal("1000.00");
        long diasAteTransferencia = 35;
        BigDecimal taxaEsperada = new BigDecimal("47.00");

        BigDecimal taxaCalculada = calculadoraTaxa.calcular(
                valorTransferencia,
                diasAteTransferencia
        );

        assertEquals(taxaEsperada, taxaCalculada);
    }

    @Test
    void deveCalcularTaxaDe41A50Dias() {
        BigDecimal valorTransferencia = new BigDecimal("1000.00");
        long diasAteTransferencia = 45;
        BigDecimal taxaEsperada = new BigDecimal("17.00");

        BigDecimal taxaCalculada = calculadoraTaxa.calcular(
                valorTransferencia,
                diasAteTransferencia
        );

        assertEquals(taxaEsperada, taxaCalculada);
    }

    @Test
    void deveArredondarTaxaParaDuasCasasDecimais() {
        BigDecimal valorTransferencia = new BigDecimal("100.01");
        long diasAteTransferencia = 15;
        BigDecimal taxaEsperada = new BigDecimal("8.20");

        BigDecimal taxaCalculada = calculadoraTaxa.calcular(
                valorTransferencia,
                diasAteTransferencia
        );

        assertEquals(taxaEsperada, taxaCalculada);
    }

    @Test
    void deveLancarExcecaoQuandoDiasForemNegativos() {
        BigDecimal valorTransferencia = new BigDecimal("1000.00");
        long diasAteTransferencia = -1;
        String mensagemEsperada =
                "A data da transferência não pode ser anterior à data do agendamento.";

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> calculadoraTaxa.calcular(
                        valorTransferencia,
                        diasAteTransferencia
                )
        );

        assertEquals(mensagemEsperada, excecao.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoDiasForemMaioresQue50() {
        BigDecimal valorTransferencia = new BigDecimal("1000.00");
        long diasAteTransferencia = 51;
        String mensagemEsperada =
                "Não existe taxa aplicável para essa data de transferência.";

        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> calculadoraTaxa.calcular(
                        valorTransferencia,
                        diasAteTransferencia
                )
        );

        assertEquals(mensagemEsperada, excecao.getMessage());
    }
}
