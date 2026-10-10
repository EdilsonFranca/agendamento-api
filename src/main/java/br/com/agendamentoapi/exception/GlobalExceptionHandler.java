package br.com.agendamentoapi.exception;

import br.com.agendamentoapi.dto.ErroResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log =
            LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Trata erros de validação dos dados recebidos pela API.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> tratarValidacao(
            MethodArgumentNotValidException ex) {

        String mensagem = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(erro -> erro.getField() + ": " + erro.getDefaultMessage())
                .findFirst()
                .orElse("Dados inválidos na solicitação.");

        log.warn("Erro de validação: {}", mensagem);

        ErroResponse erro = new ErroResponse(
                HttpStatus.BAD_REQUEST.value(),
                mensagem
        );

        return ResponseEntity.badRequest().body(erro);
    }

    /**
     * Trata requisições com JSON inválido ou dados incompatíveis.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResponse> tratarJsonInvalido(
            HttpMessageNotReadableException ex) {

        log.warn("Requisição recebida com formato inválido.");

        ErroResponse erro = new ErroResponse(
                HttpStatus.BAD_REQUEST.value(),
                "JSON inválido ou dados em formato incorreto."
        );

        return ResponseEntity.badRequest().body(erro);
    }

    /**
     * Trata erros inesperados durante o processamento da requisição.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponse> tratarErroInterno(Exception ex) {

        log.error("Erro interno ao processar a requisição.", ex);

        ErroResponse erro = new ErroResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Ocorreu um erro interno no servidor."
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(erro);
    }
}
