package com.marketplace.ifba.controller;

import com.marketplace.ifba.dto.ChatResponse;
import com.marketplace.ifba.dto.EditarMensagemRequest;
import com.marketplace.ifba.dto.MensagemRequest;
import com.marketplace.ifba.dto.MensagemResponse;
import com.marketplace.ifba.mapper.ChatMapper;
import com.marketplace.ifba.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/chats")
@Tag(name = "Chat", description = "Gerencia chats e mensagens")
public class ChatController {

    private final ChatService chatService;
    private final ChatMapper chatMapper;

    public ChatController(ChatService chatService, ChatMapper chatMapper) {
        this.chatService = chatService;
        this.chatMapper = chatMapper;
    }

    @Operation(summary = "Buscar chat por ID", description = "Busca um chat específico pelo seu ID")
    @GetMapping("/{idChat}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ALUNO') or hasRole('PROFESSOR') or hasRole('EXTERNO')")
    public ResponseEntity<ChatResponse> buscarChatPorId(@PathVariable UUID idChat) {
        return ResponseEntity.ok(chatMapper.toDTO(chatService.buscarChatPorId(idChat)));
    }

    @Operation(summary = "Buscar chat por projeto", description = "Busca o chat associado a um projeto específico")
    @GetMapping("/projeto/{idProjeto}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ALUNO') or hasRole('PROFESSOR') or hasRole('EXTERNO')")
    public ResponseEntity<ChatResponse> buscarChatPorProjeto(@PathVariable UUID idProjeto) {
        return ResponseEntity.ok(chatMapper.toDTO(chatService.buscarChatPorProjeto(idProjeto)));
    }

    @Operation(summary = "Buscar todos os chats", description = "Lista todos os chats do sistema")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ChatResponse>> buscarTodosChats() {
        return ResponseEntity.ok(chatService.buscarTodosChats().stream().map(chatMapper::toDTO).collect(Collectors.toList()));
    }

    @Operation(summary = "Enviar mensagem", description = "Envia uma nova mensagem para um chat")
    @PostMapping("/mensagem")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ALUNO') or hasRole('PROFESSOR') or hasRole('EXTERNO')")
    public ResponseEntity<MensagemResponse> enviarMensagem(@Valid @RequestBody MensagemRequest mensagemRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(chatMapper.toMensagemDTO(chatService.enviarMensagem(mensagemRequest.idChat(), mensagemRequest.mensagem(), mensagemRequest.idUsuario())));
    }

    @Operation(summary = "Editar mensagem", description = "Edita o conteúdo de uma mensagem existente")
    @PutMapping("/mensagem")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ALUNO') or hasRole('PROFESSOR') or hasRole('EXTERNO')")
    public ResponseEntity<MensagemResponse> editarMensagem(@Valid @RequestBody EditarMensagemRequest editarMensagemRequest) {
        return ResponseEntity.ok(chatMapper.toMensagemDTO(chatService.editarMensagem(editarMensagemRequest.idMensagem(), editarMensagemRequest.novaMensagem())));
    }
}