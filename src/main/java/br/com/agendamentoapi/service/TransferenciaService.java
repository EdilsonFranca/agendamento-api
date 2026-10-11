package br.com.agendamentoapi.service;

import br.com.agendamentoapi.dto.TransferenciaDto;
import br.com.agendamentoapi.model.Transferencia;
import br.com.agendamentoapi.repository.TransferenciaRepository;
import br.com.agendamentoapi.util.CalculadoraTaxa;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@AllArgsConstructor
public class TransferenciaService {

    private static final Logger log = LoggerFactory.getLogger(TransferenciaService.class);

    private final TransferenciaRepository repository;
    private final CalculadoraTaxa calculadoraTaxa;
    private final ModelMapper modelMapper;

    public TransferenciaDto agendar(TransferenciaDto dto) {
        log.info("Iniciando agendamento de transferência.");

        Transferencia transferencia = modelMapper.map(dto, Transferencia.class);
        transferencia.setDataAgendamento(LocalDate.now());

        long dias = ChronoUnit.DAYS.between(
                transferencia.getDataAgendamento(),
                transferencia.getDataTransferencia()
        );

        log.debug("Calculando taxa da transferência. diasAntecedencia={}", dias);

        BigDecimal taxa = calculadoraTaxa.calcular(
                transferencia.getValor(),
                dias
        );

        transferencia.setTaxa(taxa);
        transferencia.setValorTotal(transferencia.getValor().add(taxa));

        Transferencia salva = repository.save(transferencia);

        log.info("Transferência agendada com sucesso. id={}", salva.getId());

        return modelMapper.map(salva, TransferenciaDto.class);
    }

    public List<TransferenciaDto> listarTodos() {
        log.info("Iniciando consulta de transferências.");

        List<TransferenciaDto> transferencias = repository
                .findAllByOrderByDataAgendamentoDesc()
                .stream()
                .map(transferencia -> modelMapper.map(
                        transferencia,
                        TransferenciaDto.class
                ))
                .toList();

        log.info("Consulta de transferências concluída. quantidade={}", transferencias.size());

        return transferencias;
    }
}