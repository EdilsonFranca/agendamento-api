package br.com.agendamentoapi.controller;

import br.com.agendamentoapi.dto.TransferenciaDto;
import br.com.agendamentoapi.service.TransferenciaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.boot.test.mock.mockito.MockBean;

@WebMvcTest(TransferenciasController.class)
class TransferenciasControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TransferenciaService service;

    @Test
    void deveAgendarTransferenciaERetornarStatusCreated() throws Exception {
        TransferenciaDto transferenciaSolicitada = criarTransferenciaDto();

        TransferenciaDto transferenciaSalva = criarTransferenciaDto();
        transferenciaSalva.setId(1L);
        transferenciaSalva.setTaxa(new BigDecimal("37.00"));
        transferenciaSalva.setValorTotal(new BigDecimal("1037.00"));

        when(service.agendar(any(TransferenciaDto.class)))
                .thenReturn(transferenciaSalva);

        mockMvc.perform(post("/api/v1/transferencias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                transferenciaSolicitada
                        )))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.contaOrigem").value("1234567890"))
                .andExpect(jsonPath("$.contaDestino").value("0987654321"))
                .andExpect(jsonPath("$.valor").value(1000.00))
                .andExpect(jsonPath("$.taxa").value(37.00))
                .andExpect(jsonPath("$.valorTotal").value(1037.00));

        verify(service).agendar(any(TransferenciaDto.class));
    }

    @Test
    void deveListarTodasAsTransferenciasERetornarStatusOk() throws Exception {
        TransferenciaDto primeiraTransferencia = criarTransferenciaDto();
        primeiraTransferencia.setId(1L);

        TransferenciaDto segundaTransferencia = criarTransferenciaDto();
        segundaTransferencia.setId(2L);
        segundaTransferencia.setContaOrigem("1111111111");

        when(service.listarTodos())
                .thenReturn(List.of(
                        primeiraTransferencia,
                        segundaTransferencia
                ));

        mockMvc.perform(get("/api/v1/transferencias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].contaOrigem").value("1234567890"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].contaOrigem").value("1111111111"));

        verify(service).listarTodos();
    }

    private TransferenciaDto criarTransferenciaDto() {
        TransferenciaDto dto = new TransferenciaDto();
        dto.setContaOrigem("1234567890");
        dto.setContaDestino("0987654321");
        dto.setValor(new BigDecimal("1000.00"));
        dto.setDataTransferencia(LocalDate.now().plusDays(10));

        return dto;
    }
}
