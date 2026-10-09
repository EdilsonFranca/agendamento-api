package br.com.agendamentoapi.service;

import br.com.agendamentoapi.dto.TransferenciaDto;
import br.com.agendamentoapi.model.Transferencia;
import br.com.agendamentoapi.repository.TransferenciaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@AllArgsConstructor
public class TransferenciaService {

    private final TransferenciaRepository repository;

    public TransferenciaDto agendar(TransferenciaDto dto) {

        Transferencia transferencia = new Transferencia(dto);

        transferencia.setDataAgendamento(LocalDate.now());

        long between = ChronoUnit.DAYS.between(
                transferencia.getDataAgendamento(),
                transferencia.getDataTransferencia()
        );

        BigDecimal taxa = calcular(
                transferencia.getValor(),
                between
        );

        transferencia.setTaxa(taxa);
        transferencia.setValorTotal(transferencia.getValor().add(taxa));

        Transferencia salva = repository.save(transferencia);

        return new TransferenciaDto(salva);
    }


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