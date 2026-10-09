package br.com.agendamentoapi.dto;

import br.com.agendamentoapi.model.Transferencia;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import javax.validation.constraints.*;

@Data
public class TransferenciaDto {

    private Long id;

    @NotBlank(message = "A conta de origem é obrigatória.")
    @Pattern(
            regexp = "\\d{10}",
            message = "A conta de origem deve conter exatamente 10 dígitos."
    )
    private String contaOrigem;

    @NotBlank(message = "A conta de destino é obrigatória.")
    @Pattern(
            regexp = "\\d{10}",
            message = "A conta de destino deve conter exatamente 10 dígitos."
    )
    private String contaDestino;

    @NotNull(message = "O valor da transferência é obrigatório.")
    @DecimalMin(
            value = "0.01",
            message = "O valor deve ser maior que zero."
    )
    private BigDecimal valor;

    private BigDecimal taxa;
    private BigDecimal valorTotal;

    @NotNull(message = "A data da transferência é obrigatória.")
    @FutureOrPresent(
            message = "A data da transferência não pode ser anterior a hoje."
    )
    private LocalDate dataTransferencia;

    private LocalDate dataAgendamento;

    public TransferenciaDto(Transferencia transferencia) {
        this.id = transferencia.getId();
        this.contaOrigem = transferencia.getContaOrigem();
        this.contaDestino = transferencia.getContaDestino();
        this.valor = transferencia.getValor();
        this.taxa = transferencia.getTaxa();
        this.valorTotal = transferencia.getValorTotal();
        this.dataTransferencia = transferencia.getDataTransferencia();
        this.dataAgendamento = transferencia.getDataAgendamento();
    }
}
