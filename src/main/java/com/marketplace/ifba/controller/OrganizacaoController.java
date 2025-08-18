package com.marketplace.ifba.controller;

import com.marketplace.ifba.dto.AprovarOuReprovarOrganizacaoRequest;
import com.marketplace.ifba.dto.OrganizacaoRequest;
import com.marketplace.ifba.dto.OrganizacaoResponse;
import com.marketplace.ifba.dto.UserInfosMinResponse;
import com.marketplace.ifba.mapper.OrganizacaoMapper;
import com.marketplace.ifba.mapper.UserMapper;
import com.marketplace.ifba.service.OrganizacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/organizacoes")
@Tag(name = "Organização", description = "Gerencia as operações relacionadas às organizações no marketplace.")
public class OrganizacaoController {

    private final OrganizacaoService organizacaoService;
    private final OrganizacaoMapper organizacaoMapper;
    private final UserMapper userMapper;

    public OrganizacaoController(OrganizacaoService organizacaoService, OrganizacaoMapper organizacaoMapper, UserMapper userMapper) {
        this.organizacaoService = organizacaoService;
        this.organizacaoMapper = organizacaoMapper;
        this.userMapper = userMapper;
    }

    @Operation(summary = "Retorna organização a partir do ID", description = "Procura uma organização com o ID informado")
    @GetMapping("/{idOrganizacao}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ALUNO') or hasRole('PROFESSOR') or hasRole('EXTERNO')")
    public ResponseEntity<OrganizacaoResponse> buscarOrganizacaoPorId(@PathVariable UUID idOrganizacao) {
        return ResponseEntity.ok(organizacaoMapper.toDTO(organizacaoService.buscarOrganizacaoPorId(idOrganizacao)));
    }

    @Operation(summary = "Retorna organização a partir do NOME", description = "Procura uma organização salva com o NOME informado")
    @GetMapping("/name/{nome}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ALUNO') or hasRole('PROFESSOR') or hasRole('EXTERNO')")
    public ResponseEntity<OrganizacaoResponse> buscarOrganizacaoPorNome(@PathVariable String nome) {
        return ResponseEntity.ok(organizacaoMapper.toDTO(organizacaoService.buscarOrganizacaoPorNome(nome)));
    }

    @Operation(summary = "Retorna organização a partir do CNPJ", description = "Procura uma organização salva com o CNPJ informado")
    @GetMapping(params = "cnpj")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ALUNO') or hasRole('PROFESSOR') or hasRole('EXTERNO')")
    public ResponseEntity<OrganizacaoResponse> buscarOrganizacaoPorCnpj(@RequestParam String cnpj) {
        System.out.println("Passou Controller");
        return ResponseEntity.ok(organizacaoMapper.toDTO(organizacaoService.buscarOrganizacaoPorCnpj(cnpj)));
    }

    @Operation(summary = "Retorna usuários solicitantes associação", description = "No sistema usuários podem solicitar a sua adição como integrante da instituição, nesse método retorna os participantes que realizaram essa solicitação")
    @GetMapping("/solicitantes-associacao/{idOrganizacao}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ALUNO') or hasRole('PROFESSOR') or hasRole('EXTERNO')")
    public ResponseEntity<List<UserInfosMinResponse>> buscarUsuariosSolicitantesAssociacao(@PathVariable UUID idOrganizacao) {
        return ResponseEntity.ok(organizacaoService.buscarUsuariosSolicitantesAssociacao(idOrganizacao).stream().map(userMapper::toDTOInfosMin).toList());
    }

    @Operation(summary = "Retorna todas as organizações", description = "Retorna todas as organizações cadastradas")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('ALUNO') or hasRole('PROFESSOR') or hasRole('EXTERNO')")
    public ResponseEntity<List<OrganizacaoResponse>> buscarTodasOrganizacoes() {
        return ResponseEntity.ok(organizacaoService.buscarTodasOrganizacoes().stream().map(organizacaoMapper::toDTO).toList());
    }

    @Operation(summary = "Registra organização", description = "Realiza registro da organização se passar das regras de negócio")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('EXTERNO')")
    public ResponseEntity<OrganizacaoResponse> registrarOrganizacao(@RequestBody @Valid OrganizacaoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(organizacaoMapper.toDTO(organizacaoService.registrarOrganizacao(organizacaoMapper.toEntity(request), request.idUsuarioRegistrador())));
    }

    @Operation(summary = "Atualiza organização", description = "Realiza registro da organização se passar das regras de negócio")
    @PutMapping("/{idOrganizacao}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('EXTERNO')")
    public ResponseEntity<OrganizacaoResponse> atualizarOrganizacao(@RequestBody @Valid OrganizacaoRequest request, @PathVariable UUID idOrganizacao) {
        return ResponseEntity.ok(organizacaoMapper.toDTO(organizacaoService.atualizarOrganizacao(organizacaoMapper.toEntity(request), idOrganizacao)));
    }

    @Operation(summary = "Aprova ou reprova organização", description = "Realiza registro da organização se passar das regras de negócio")
    @PostMapping("/aprovar-reprovar")
    @PreAuthorize("hasRole('ADMIN') or hasRole('EXTERNO')")
    public ResponseEntity<OrganizacaoResponse> aprovarOrganizacao(@RequestBody @Valid AprovarOuReprovarOrganizacaoRequest request) {
        return ResponseEntity.ok(organizacaoMapper.toDTO(organizacaoService.aprovaOuReprovaOrganizacao(request.idOrganizacao(), request.idAdm(), request.decisao())));
    }

    @Operation(summary = "Remove organização", description = "Remove o cadastro da organização")
    @DeleteMapping("/{idOrganizacao}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('EXTERNO')")
    public ResponseEntity<Void> removerOrganizacao(@PathVariable UUID idOrganizacao) {
        organizacaoService.removerOrganizacao(idOrganizacao);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}