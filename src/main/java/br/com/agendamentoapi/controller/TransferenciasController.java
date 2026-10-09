package br.com.agendamentoapi.controller;

import br.com.agendamentoapi.dto.TransferenciaDto;
import br.com.agendamentoapi.service.TransferenciaService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/transferencias")
public class TransferenciasController {
    private final TransferenciaService service;

    @PostMapping
    public ResponseEntity<TransferenciaDto> create(@Valid @RequestBody TransferenciaDto dto) {
        TransferenciaDto transferencia = service.agendar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(transferencia);
    }

}