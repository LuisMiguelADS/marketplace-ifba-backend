package com.marketplace.ifba.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<Object> buildResponse(String message, HttpStatus status) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(EmailInvalidoException.class)
    public ResponseEntity<Object> handleEmailInvalidoException(EmailInvalidoException exception) {
        return buildResponse(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SenhaInvalidaException.class)
    public ResponseEntity<Object> handleSenhaInvalidaException(SenhaInvalidaException exception) {
        return buildResponse(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsuarioInvalidoException.class)
    public ResponseEntity<Object> handleUsuarioInvalidoException(UsuarioInvalidoException exception) {
        return buildResponse(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AreaInvalidaException.class)
    public ResponseEntity<Object> handleAreaInvalidaException(AreaInvalidaException exception) {
        return buildResponse(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PropostaInvalidaException.class)
    public ResponseEntity<Object> handlePropostaInvalidaException(PropostaInvalidaException exception) {
        return buildResponse(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DemandaInvalidaException.class)
    public ResponseEntity<Object> handleDemandaInvalidaException(DemandaInvalidaException exception) {
        return buildResponse(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(GrupoPesquisaInvalidoException.class)
    public ResponseEntity<Object> handleGrupoPesquisaInvalidoException(GrupoPesquisaInvalidoException exception) {
        return buildResponse(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InstituicaoInvalidaException.class)
    public ResponseEntity<Object> handleInstituicaoInvalidaException(InstituicaoInvalidaException exception) {
        return buildResponse(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(OfertaSolucaoInvalidaException.class)
    public ResponseEntity<Object> handleOfertaSolucaoInvalidaException(OfertaSolucaoInvalidaException exception) {
        return buildResponse(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(OrganizacaoInvalidaException.class)
    public ResponseEntity<Object> handleOrganizacaoInvalidaException(OrganizacaoInvalidaException exception) {
        return buildResponse(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ProjetoInvalidoException.class)
    public ResponseEntity<Object> handleProjetoInvalidoException(ProjetoInvalidoException exception) {
        return buildResponse(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException exception) {
        return buildResponse(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Object> handleIllegalStateException(IllegalStateException exception) {
        return buildResponse(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception exception) {
        return buildResponse("Erro interno do servidor: " + exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
