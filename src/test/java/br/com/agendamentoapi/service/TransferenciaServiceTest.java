
package br.com.agendamentoapi.service;

import br.com.agendamentoapi.dto.TransferenciaDto;
import br.com.agendamentoapi.model.Transferencia;
import br.com.agendamentoapi.repository.TransferenciaRepository;
import br.com.agendamentoapi.util.CalculadoraTaxa;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.*;

import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransferenciaServiceTest {

    @Mock
    private TransferenciaRepository repository;

    @Mock
    private CalculadoraTaxa calculadoraTaxa;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private TransferenciaService service;

    @Test
    void deveAgendarTransferenciaComTaxaEValorTotal() {
        TransferenciaDto dto = new TransferenciaDto();
        dto.setContaOrigem("1234567890");
        dto.setContaDestino("0987654321");
        dto.setValor(new BigDecimal("1000.00"));
        dto.setDataTransferencia(LocalDate.now().plusDays(10));

        Transferencia transferencia = new Transferencia();
        transferencia.setValor(new BigDecimal("1000.00"));
        transferencia.setDataTransferencia(dto.getDataTransferencia());

        BigDecimal taxa = new BigDecimal("12.00");
        TransferenciaDto resposta = new TransferenciaDto();

        when(modelMapper.map(dto, Transferencia.class))
                .thenReturn(transferencia);

        when(calculadoraTaxa.calcular(
                new BigDecimal("1000.00"), 10L))
                .thenReturn(taxa);

        when(repository.save(transferencia))
                .thenReturn(transferencia);

        when(modelMapper.map(transferencia, TransferenciaDto.class))
                .thenReturn(resposta);

        TransferenciaDto resultado = service.agendar(dto);

        assertNotNull(resultado);
        assertEquals(LocalDate.now(), transferencia.getDataAgendamento());
        assertEquals(0, taxa.compareTo(transferencia.getTaxa()));
        assertEquals(
                0,
                new BigDecimal("1012.00")
                        .compareTo(transferencia.getValorTotal())
        );

        verify(calculadoraTaxa).calcular(new BigDecimal("1000.00"), 10L);
        verify(repository).save(transferencia);
        verify(modelMapper).map(transferencia, TransferenciaDto.class);
    }


    @Test
    void deveListarTodasAsTransferencias() {
        Transferencia transferencia1 = new Transferencia();
        transferencia1.setContaOrigem("1234567890");

        Transferencia transferencia2 = new Transferencia();
        transferencia2.setContaOrigem("0987654321");

        TransferenciaDto dto1 = new TransferenciaDto();
        dto1.setContaOrigem("1234567890");

        TransferenciaDto dto2 = new TransferenciaDto();
        dto2.setContaOrigem("0987654321");

        when(repository.findAllByOrderByDataAgendamentoDesc())
                .thenReturn(List.of(transferencia1, transferencia2));

        when(modelMapper.map(same(transferencia1), eq(TransferenciaDto.class)))
                .thenReturn(dto1);

        when(modelMapper.map(same(transferencia2), eq(TransferenciaDto.class)))
                .thenReturn(dto2);

        List<TransferenciaDto> resultado = service.listarTodos();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("1234567890", resultado.get(0).getContaOrigem());
        assertEquals("0987654321", resultado.get(1).getContaOrigem());

        verify(repository, times(1)).findAllByOrderByDataAgendamentoDesc();
        verify(modelMapper, times(1)).map(same(transferencia1), eq(TransferenciaDto.class));
        verify(modelMapper, times(1)).map(same(transferencia2), eq(TransferenciaDto.class));
    }



    @Test
    void naoDeveSalvarTransferenciaQuandoCalculoDaTaxaFalhar() {
        TransferenciaDto dto = new TransferenciaDto();
        dto.setValor(new BigDecimal("1000.00"));
        dto.setDataTransferencia(LocalDate.now().minusDays(1));

        Transferencia transferencia = new Transferencia();
        transferencia.setValor(new BigDecimal("1000.00"));
        transferencia.setDataTransferencia(dto.getDataTransferencia());

        when(modelMapper.map(dto, Transferencia.class)).thenReturn(transferencia);

        when(calculadoraTaxa.calcular(
                any(BigDecimal.class), anyLong()))
                .thenThrow(new IllegalArgumentException(
                        "A data da transferência não pode ser anterior à data do agendamento."
                ));

        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> service.agendar(dto)
        );

        verify(repository, never()).save(any(Transferencia.class));
    }
}
