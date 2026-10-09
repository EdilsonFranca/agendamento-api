package br.com.agendamentoapi.service;

import br.com.agendamentoapi.dto.TransferenciaDto;
import br.com.agendamentoapi.model.Transferencia;
import br.com.agendamentoapi.repository.TransferenciaRepository;
import br.com.agendamentoapi.util.CalculadoraTaxa;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@AllArgsConstructor
public class TransferenciaService {

    private final TransferenciaRepository repository;
    private final CalculadoraTaxa calculadoraTaxa;

    public TransferenciaDto agendar(TransferenciaDto dto) {

        Transferencia transferencia = new Transferencia(dto);

        transferencia.setDataAgendamento(LocalDate.now());

        long between = ChronoUnit.DAYS.between(
                transferencia.getDataAgendamento(),
                transferencia.getDataTransferencia()
        );

        BigDecimal taxa = calculadoraTaxa.calcular(
                transferencia.getValor(),
                between
        );

        transferencia.setTaxa(taxa);
        transferencia.setValorTotal(transferencia.getValor().add(taxa));

        Transferencia salva = repository.save(transferencia);

        return new TransferenciaDto(salva);
    }

}