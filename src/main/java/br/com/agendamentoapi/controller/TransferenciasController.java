package br.com.agendamentoapi.controller;

import br.com.agendamentoapi.dto.TransferenciaDto;
import br.com.agendamentoapi.service.TransferenciaService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/transferencias")
public class TransferenciasController {
    private static final Logger log = LoggerFactory.getLogger(TransferenciasController.class);

    private final TransferenciaService service;

    @PostMapping
    public ResponseEntity<TransferenciaDto> agendar(@Valid @RequestBody TransferenciaDto dto) {
        log.info("Recebida solicitação para agendar transferência.");

        TransferenciaDto transferencia = service.agendar(dto);

        log.info("Transferência agendada com sucesso. id={}", transferencia.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(transferencia);
    }

    @GetMapping
    public ResponseEntity<List<TransferenciaDto>> listarTodos() {
        log.info("Iniciando busca de todas as transferências.");

        List<TransferenciaDto> transferencias = service.listarTodos();

        log.info("Busca de transferências concluída. quantidade={}", transferencias.size());

        return ResponseEntity.ok(transferencias);
    }
}