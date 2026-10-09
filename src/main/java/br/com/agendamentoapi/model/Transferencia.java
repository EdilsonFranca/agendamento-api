package br.com.agendamentoapi.model;

import br.com.agendamentoapi.dto.TransferenciaDto;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
public class Transferencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String contaOrigem;
    private String contaDestino;

    private BigDecimal valor;
    private BigDecimal taxa;
    private BigDecimal valorTotal;

    private LocalDate dataTransferencia;
    private LocalDate dataAgendamento;

    public Transferencia(TransferenciaDto transferenciaDto) {
        this.id = transferenciaDto.getId();
        this.contaOrigem = transferenciaDto.getContaOrigem();
        this.contaDestino = transferenciaDto.getContaDestino();
        this.valor = transferenciaDto.getValor();
        this.taxa = transferenciaDto.getTaxa();
        this.valorTotal = transferenciaDto.getValorTotal();
        this.dataTransferencia = transferenciaDto.getDataTransferencia();
        this.dataAgendamento = transferenciaDto.getDataAgendamento();
    }

    public Transferencia() {}
}