package br.com.agendamentoapi.controller;

import br.com.agendamentoapi.dto.ErroResponse;
import br.com.agendamentoapi.dto.TransferenciaDto;
import br.com.agendamentoapi.service.TransferenciaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@Tag(name = "Transferências", description = "Gerenciamento de transferências")
@RestController
@AllArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/transferencias")
public class TransferenciasController {
    private static final Logger log = LoggerFactory.getLogger(TransferenciasController.class);

    private final TransferenciaService service;

    @Operation(
            summary = "Agendar transferência",
            description = "Agenda uma nova transferência."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Transferência agendada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TransferenciaDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos na solicitação",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErroResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno do servidor",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErroResponse.class)
                    )
            )
    })
    @PostMapping
    public ResponseEntity<TransferenciaDto> agendar(@Valid @RequestBody TransferenciaDto dto) {
        log.info("Recebida solicitação para agendar transferência.");

        TransferenciaDto transferencia = service.agendar(dto);

        log.info("Transferência agendada com sucesso. id={}", transferencia.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(transferencia);
    }



    @Operation(
            summary = "Listar transferências",
            description = "Retorna todas as transferências cadastradas."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping
    public ResponseEntity<List<TransferenciaDto>> listarTodos() {
        log.info("Iniciando busca de todas as transferências.");

        List<TransferenciaDto> transferencias = service.listarTodos();

        log.info("Busca de transferências concluída. quantidade={}", transferencias.size());

        return ResponseEntity.ok(transferencias);
    }
}