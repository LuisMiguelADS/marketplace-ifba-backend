package com.marketplace.ifba.controller;

import com.marketplace.ifba.dto.AtualizarStatusGrupoPesquisaRequest;
import com.marketplace.ifba.dto.DemandaResponse;
import com.marketplace.ifba.dto.GrupoPesquisaRequest;
import com.marketplace.ifba.dto.GrupoPesquisaResponse;
import com.marketplace.ifba.dto.RemoverDemandaDoGrupoRequest;
import com.marketplace.ifba.dto.UserInfosMinResponse;
import com.marketplace.ifba.mapper.DemandaMapper;
import com.marketplace.ifba.mapper.GrupoPesquisaMapper;
import com.marketplace.ifba.mapper.UserMapper;
import com.marketplace.ifba.model.Demanda;
import com.marketplace.ifba.service.GrupoPesquisaService;
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
@RequestMapping("/grupos-pesquisa")
@Tag(name = "Grupo de Pesquisa", description = "Gerencia as operações relacionadas aos grupos de pesquisa no marketplace.")
public class GrupoPesquisaController {

    private final DemandaMapper demandaMapper;

    private final GrupoPesquisaService grupoPesquisaService;
    private final GrupoPesquisaMapper grupoPesquisaMapper;
    private final UserMapper userMapper;

    public GrupoPesquisaController(GrupoPesquisaService grupoPesquisaService, GrupoPesquisaMapper grupoPesquisaMapper, UserMapper userMapper, DemandaMapper demandaMapper) {
        this.grupoPesquisaService = grupoPesquisaService;
        this.grupoPesquisaMapper = grupoPesquisaMapper;
        this.userMapper = userMapper;
        this.demandaMapper = demandaMapper;
    }

    @Operation(summary = "Retorna grupo de pesquisa a partir do ID", description = "Procura um grupo de pesquisa com o ID informado")
    @GetMapping("/{idGrupoPesquisa}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ALUNO') or hasRole('PROFESSOR') or hasRole('EXTERNO')")
    public ResponseEntity<GrupoPesquisaResponse> buscarGrupoPesquisaPorId(@PathVariable UUID idGrupoPesquisa) {
        return ResponseEntity.ok(grupoPesquisaMapper.toDTO(grupoPesquisaService.buscarGrupoPesquisaPorId(idGrupoPesquisa)));
    }

    @Operation(summary = "Retorna grupo de pesquisa a partir do NOME", description = "Procura um grupo de pesquisa salva com o NOME informado")
    @GetMapping("/nome/{nome}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ALUNO') or hasRole('PROFESSOR') or hasRole('EXTERNO')")
    public ResponseEntity<GrupoPesquisaResponse> buscarGrupoPesquisaPorNome(@PathVariable String nome) {
        return ResponseEntity.ok(grupoPesquisaMapper.toDTO(grupoPesquisaService.buscarGrupoPesquisaPorNome(nome)));
    }

    @Operation(summary = "Retorna grupo de pesquisa a partir da INSTITUIÇÃO associada", description = "Procura um demanda salva com a INSTITUIÇÃO associada ao grupo de pesquisa")
    @GetMapping("/instituicao/{idInstituicao}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ALUNO') or hasRole('PROFESSOR') or hasRole('EXTERNO')")
    public ResponseEntity<List<GrupoPesquisaResponse>> buscarGruposPorInstituicao(@PathVariable UUID idInstituicao) {
        return ResponseEntity.ok(grupoPesquisaService.buscarGruposPorInstituicao(idInstituicao).stream().map(grupoPesquisaMapper::toDTO).toList());
    }

    @Operation(summary = "Retorna usuários solicitantes associação", description = "No sistema usuários podem solicitar a sua adição como integrante do grupo de pesquisa, nesse método retorna os participantes que realizaram essa solicitação")
    @GetMapping("/solicitantes-associacao/{idGrupoPesquisa}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ALUNO') or hasRole('PROFESSOR') or hasRole('EXTERNO')")
    public ResponseEntity<List<UserInfosMinResponse>> buscarUsuariosSolicitantesAssociacao(@PathVariable UUID idGrupoPesquisa) {
        return ResponseEntity.ok(grupoPesquisaService.buscarUsuariosSolicitantesAssociacao(idGrupoPesquisa).stream().map(userMapper::toDTOInfosMin).toList());
    }

    @Operation(summary = "Retorna todos os grupos de pesquisa", description = "Retorna todas os grupos de pesquisa cadastradas")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('ALUNO') or hasRole('PROFESSOR') or hasRole('EXTERNO')")
    public ResponseEntity<List<GrupoPesquisaResponse>> buscarTodosGruposPesquisa() {
        return ResponseEntity.ok(grupoPesquisaService.buscarTodosGruposPesquisa().stream().map(grupoPesquisaMapper::toDTO).toList());
    }

    @Operation(summary = "Registra grupo de pesquisa", description = "Realiza registro do grupo de pesquisa se passar das regras de negócio")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROFESSOR')")
    public ResponseEntity<GrupoPesquisaResponse> registrarGrupoPesquisa(@RequestBody @Valid GrupoPesquisaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(grupoPesquisaMapper.toDTO(grupoPesquisaService.registrarGrupoPesquisa(grupoPesquisaMapper.toEntity(request), request.idInstituicao(), request.usuarioRegistrador(), request.idsAreas())));
    }

    @Operation(summary = "Atualiza grupo de pesquisa", description = "Atualiza grupo de pesquisa se passar das regras de negócio")
    @PutMapping()
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROFESSOR')")
    public ResponseEntity<GrupoPesquisaResponse> atualizarGrupoPesquisa(@RequestBody @Valid GrupoPesquisaRequest request) {
        return ResponseEntity.ok(grupoPesquisaMapper.toDTO(grupoPesquisaService.atualizarGrupoPesquisa(request.idGrupoPesquisa(), grupoPesquisaMapper.toEntity(request))));
    }

    @Operation(summary = "Atualiza status grupo de pesquisa", description = "Atualiza status do grupo de pesquisa se passar das regras de negócio")
    @PatchMapping("/status/{idGrupoPesquisa}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROFESSOR')")
    public ResponseEntity<GrupoPesquisaResponse> atualizarStatusGrupoPesquisa(@PathVariable UUID idGrupoPesquisa, @RequestBody @Valid AtualizarStatusGrupoPesquisaRequest request) {
        return ResponseEntity.ok(grupoPesquisaMapper.toDTO(grupoPesquisaService.atualizarStatusGrupoPesquisa(idGrupoPesquisa, request.novoStatus())));
    }

    @Operation(summary = "Remove grupo de pesquisa", description = "Remove o cadastro do grupo de pesquisa")
    @DeleteMapping("/{idGrupoPesquisa}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROFESSOR')")
    public ResponseEntity<Void> removerGrupoPesquisa(@PathVariable UUID idGrupoPesquisa) {
        grupoPesquisaService.removerGrupoPesquisa(idGrupoPesquisa);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Busca demandas recebidas por um grupo de pesquisa", description = "Retorna a lista de demandas que foram enviadas para um grupo de pesquisa específico")
    @GetMapping("/demandas/{idGrupoPesquisa}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ALUNO') or hasRole('PROFESSOR') or hasRole('EXTERNO')")
    public ResponseEntity<List<DemandaResponse>> buscarDemandasRecebidas(@PathVariable UUID idGrupoPesquisa) {
        return ResponseEntity.ok(grupoPesquisaService.buscarDemandasRecebidas(idGrupoPesquisa).stream().map(demandaMapper::toDTO).toList());
    }

    @Operation(summary = "Remove demanda de um grupo de pesquisa", description = "Remove uma demanda específica da lista de demandas recebidas por um grupo de pesquisa")
    @DeleteMapping("/remover-demanda")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROFESSOR')")
    public ResponseEntity<Void> removerDemandaDoGrupo(@RequestBody @Valid RemoverDemandaDoGrupoRequest request) {
        grupoPesquisaService.removerDemandaDoGrupo(request.idGrupoPesquisa(), request.idDemanda());
        return ResponseEntity.noContent().build();
    }
}
