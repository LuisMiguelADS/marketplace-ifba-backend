package com.marketplace.ifba.service;

import com.marketplace.ifba.exception.*;
import com.marketplace.ifba.model.*;
import com.marketplace.ifba.model.enums.StatusEntrega;
import com.marketplace.ifba.model.enums.StatusProjeto;
import com.marketplace.ifba.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProjetoService {

    private final ProjetoRepository projetoRepository;
    private final OrganizacaoRepository organizacaoRepository;
    private final InstituicaoRepository instituicaoRepository;
    private final DemandaRepository demandaRepository;
    private final OfertaSolucaoRepository ofertaSolucaoRepository;
    private final GrupoPesquisaRepository grupoPesquisaRepository;
    private final EntregaRepository entregaRepository;

    public ProjetoService(ProjetoRepository projetoRepository, OrganizacaoRepository organizacaoRepository,
                          InstituicaoRepository instituicaoRepository, DemandaRepository demandaRepository,
                          OfertaSolucaoRepository ofertaSolucaoRepository, GrupoPesquisaRepository grupoPesquisaRepository,
                          EntregaRepository entregaRepository) {
        this.projetoRepository = projetoRepository;
        this.organizacaoRepository = organizacaoRepository;
        this.instituicaoRepository = instituicaoRepository;
        this.demandaRepository = demandaRepository;
        this.ofertaSolucaoRepository = ofertaSolucaoRepository;
        this.grupoPesquisaRepository = grupoPesquisaRepository;
        this.entregaRepository = entregaRepository;
    }

    // ---------- LEITURA

    // BUSCA PROJETO PELO SEU ID
    @Transactional(readOnly = true)
    public Projeto buscarProjetoPorId(UUID idProjeto) {
        return projetoRepository.findById(idProjeto)
                .orElseThrow(() -> new ProjetoInvalidoException("Projeto não encontrado com o ID!"));
    }

    // BUSCA O PROJETO PELO SEU NOME
    @Transactional(readOnly = true)
    public Projeto buscarProjetoPorNome(String nome) {
        return projetoRepository.findByNome(nome)
                .orElseThrow(() -> new ProjetoInvalidoException("Projeto não encontrado com o nome!"));
    }

    // BUSCA PROJETO PELA SUA DEMANDA
    @Transactional(readOnly = true)
    public Projeto buscarProjetoPorDemanda(UUID idDemanda) {
        return projetoRepository.findAll().stream()
                .filter(projeto -> projeto.getDemanda().getIdDemanda().equals(idDemanda))
                .findFirst().orElseThrow(() -> new ProjetoInvalidoException("Projeto não encontrado para a Demanda!"));
    }

    // BUSCA PROJETO PELO GRUPO DE PESQUISA
    @Transactional(readOnly = true)
    public List<Projeto> buscarProjetosPorGrupoPesquisa(UUID idGrupoPesquisa) {
        GrupoPesquisa grupo = grupoPesquisaRepository.findById(idGrupoPesquisa)
                .orElseThrow(() -> new GrupoPesquisaInvalidoException("Grupo Pesquisa não encontrado!"));

        return projetoRepository.findByGrupoPesquisa(grupo);
    }

    // BUSCA PROJETO PELA INSTITUIÇÃO
    @Transactional(readOnly = true)
    public List<Projeto> buscarProjetosPorInstituicao(UUID idInstituicao) {
        Instituicao instituicao = instituicaoRepository.findById(idInstituicao)
                .orElseThrow(() -> new InstituicaoInvalidaException("Instituição não encontrada!"));

        return projetoRepository.findByInstituicao(instituicao);
    }

    // BUSCA PROJETO PELA ORGANIZAÇÃO
    @Transactional(readOnly = true)
    public List<Projeto> buscarProjetosPorOrganizacao(UUID idOrganizacao) {
        Organizacao organizacao = organizacaoRepository.findById(idOrganizacao)
                .orElseThrow(() -> new OrganizacaoInvalidaException("Organização não encontrada!"));

        return projetoRepository.findByOrganizacao(organizacao);
    }

    // LISTA TODOS OS PROJETOS DO SISTEMA
    @Transactional(readOnly = true)
    public List<Projeto> buscarTodosProjetos() {
        return projetoRepository.findAll();
    }

    // ---------- ESCRITA

    // REGISTRA PROJETO
    @Transactional
    public Projeto registrarProjeto(Projeto projeto, UUID idOrganizacao, UUID idInstituicao, UUID idDemanda, UUID idOfertaSolucao, UUID idGrupoPesquisa) {
        if (projetoRepository.findAll().stream().anyMatch(proj -> proj.getOfertaSolucao().getIdSolucao().equals(idOfertaSolucao))) {
            throw new DemandaInvalidaException("A Demanda já está associada com um projeto!");
        }

        Organizacao organizacao = organizacaoRepository.findById(idOrganizacao)
                .orElseThrow(() -> new OrganizacaoInvalidaException("Organização não encontrada com o ID"));
        projeto.setOrganizacao(organizacao);

        Instituicao instituicao = instituicaoRepository.findById(idInstituicao)
                .orElseThrow(() -> new InstituicaoInvalidaException("Instituição não encontrada com o ID"));
        projeto.setInstituicao(instituicao);

        Demanda demanda = demandaRepository.findById(idDemanda)
                .orElseThrow(() -> new DemandaInvalidaException("Demanda não encontrada com o ID"));
        projeto.setDemanda(demanda);

        GrupoPesquisa grupoPesquisa = grupoPesquisaRepository.findById(idGrupoPesquisa)
                .orElseThrow(() -> new GrupoPesquisaInvalidoException("Grupo de Pesquisa não encontrado com o ID!"));
        projeto.setGrupoPesquisa(grupoPesquisa);

        if (idOfertaSolucao != null) {
            OfertaSolucao ofertaSolucao = ofertaSolucaoRepository.findById(idOfertaSolucao)
                    .orElseThrow(() -> new OfertaSolucaoInvalidaException("Oferta Solução não encontrada!"));
            if (projetoRepository.findAll().stream()
                    .anyMatch(proj -> proj.getOfertaSolucao().getIdSolucao().equals(idOfertaSolucao))) {
                throw new OfertaSolucaoInvalidaException("A Oferta Solução já está associada a outro projeto.");
            }
            projeto.setOfertaSolucao(ofertaSolucao);
        }

        Chat chat = new Chat();
        chat.setDataCriacao(LocalDateTime.now());

        List<ChatUsuario> chatUsuarios = new ArrayList<>();
        for (User userChat : grupoPesquisa.getUsuarios()) {
            ChatUsuario chatUsuario = new ChatUsuario();
            chatUsuario.setUsuario(userChat);
            chatUsuario.setChat(chat);
            chatUsuarios.add(chatUsuario);
        }
        chat.setChatUsuarios(chatUsuarios);
        projeto.setChat(chat);

        projeto.setStatus(StatusProjeto.DESENVOLVENDO);
        return projetoRepository.save(projeto);
    }

    // ATUALIZA NOME DO PROJETO
    @Transactional
    public Projeto atualizarNomeProjeto(UUID idProjeto, String novoNome) {
        Projeto projeto = projetoRepository.findById(idProjeto)
                .orElseThrow(() -> new ProjetoInvalidoException("Projeto não encontrado para atualização!"));

        if (!projeto.getNome().equals(novoNome)) {
            projetoRepository.findByNome(novoNome).ifPresent(p -> {
                if (!p.getIdProjeto().equals(idProjeto)) {
                    throw new ProjetoInvalidoException("Já existe outro projeto com o nome!");
                }
            });
        }

        projeto.setNome(novoNome);

        return projetoRepository.save(projeto);
    }

    // ATUALIZA STATUS DO PROJETO
    @Transactional
    public Projeto atualizarStatusProjeto(UUID idProjeto, StatusProjeto novoStatus) {
        Projeto projeto = projetoRepository.findById(idProjeto)
                .orElseThrow(() -> new ProjetoInvalidoException("Projeto não encontrado com o ID!"));

        if (StatusProjeto.CANCELADO.equals(projeto.getStatus())) {
            throw new ProjetoInvalidoException("O projeto está cancelado!");
        }

        if (StatusProjeto.FINALIZADO.equals(projeto.getStatus())) {
            throw new ProjetoInvalidoException("O projeto está finalizado!");
        }

        if (StatusProjeto.DESENVOLVENDO.equals(projeto.getStatus()) && StatusProjeto.DESENVOLVENDO.equals(novoStatus)) {
            throw new ProjetoInvalidoException("O projeto já está com Status desenvolvendo!");
        }

        projeto.setStatus(novoStatus);

        return projetoRepository.save(projeto);
    }

    // REMOVE PROJETO PELO SEU ID
    @Transactional
    public void removerProjeto(UUID idProjeto) {
        if (!projetoRepository.existsById(idProjeto)) {
            throw new ProjetoInvalidoException("Projeto não encontrado para exclusão com o ID: " + idProjeto);
        }
        projetoRepository.deleteById(idProjeto);
    }

    @Transactional
    public Entrega adicionarEntrega(UUID idProjeto, Entrega entrega, UUID idOrganizacaoSolicitante, 
                                   UUID idGrupoPesquisaSolicitante, UUID idOrganizacaoSolicitada, 
                                   UUID idGrupoPesquisaSolicitado) {
        Projeto projeto = buscarProjetoPorId(idProjeto);

        if ((idOrganizacaoSolicitante != null && idGrupoPesquisaSolicitante != null) ||
            (idOrganizacaoSolicitante == null && idGrupoPesquisaSolicitante == null)) {
            throw new ProjetoInvalidoException("Deve ser informado apenas um tipo de solicitante (Organização OU Grupo de Pesquisa)");
        }
        
        if ((idOrganizacaoSolicitada != null && idGrupoPesquisaSolicitado != null) ||
            (idOrganizacaoSolicitada == null && idGrupoPesquisaSolicitado == null)) {
            throw new ProjetoInvalidoException("Deve ser informado apenas um tipo de solicitado (Organização OU Grupo de Pesquisa)");
        }
        
        if (idOrganizacaoSolicitante != null) {
            Organizacao organizacao = organizacaoRepository.findById(idOrganizacaoSolicitante)
                    .orElseThrow(() -> new OrganizacaoInvalidaException("Organização solicitante não encontrada"));
            entrega.setOrganizacaoSolicitante(organizacao);
        } else {
            GrupoPesquisa grupoPesquisa = grupoPesquisaRepository.findById(idGrupoPesquisaSolicitante)
                    .orElseThrow(() -> new GrupoPesquisaInvalidoException("Grupo de pesquisa solicitante não encontrado"));
            entrega.setGrupoPesquisaSolicitante(grupoPesquisa);
        }
        
        if (idOrganizacaoSolicitada != null) {
            Organizacao organizacao = organizacaoRepository.findById(idOrganizacaoSolicitada)
                    .orElseThrow(() -> new OrganizacaoInvalidaException("Organização solicitada não encontrada"));
            entrega.setOrganizacaoSolicitada(organizacao);
        } else {
            GrupoPesquisa grupoPesquisa = grupoPesquisaRepository.findById(idGrupoPesquisaSolicitado)
                    .orElseThrow(() -> new GrupoPesquisaInvalidoException("Grupo de pesquisa solicitado não encontrado"));
            entrega.setGrupoPesquisaSolicitado(grupoPesquisa);
        }
        
        entrega.setStatus(StatusEntrega.SOLICITADA);
        entrega.setProjeto(projeto);
        
        if (projeto.getEntregas() == null) {
            projeto.setEntregas(new ArrayList<>());
        }
        projeto.getEntregas().add(entrega);
        
        projetoRepository.save(projeto);
        return entrega;
    }

    // BUSCA ENTREGAS DE UM PROJETO
    @Transactional(readOnly = true)
    public List<Entrega> buscarEntregasPorProjeto(UUID idProjeto) {
        Projeto projeto = buscarProjetoPorId(idProjeto);
        return projeto.getEntregas() != null ? projeto.getEntregas() : new ArrayList<>();
    }

    // ATUALIZA ENTREGA
    @Transactional
    public Entrega atualizarEntrega(UUID idEntrega, String titulo, String descricao, LocalDateTime prazoDesejado) {
        Entrega entrega = entregaRepository.findById(idEntrega)
                .orElseThrow(() -> new ProjetoInvalidoException("Entrega não encontrada com o ID: " + idEntrega));
        
        if (StatusEntrega.CANCELADA.equals(entrega.getStatus())) {
            throw new ProjetoInvalidoException("Não é possível atualizar uma entrega cancelada");
        }
        
        if (titulo != null && !titulo.trim().isEmpty()) {
            entrega.setTitulo(titulo);
        }
        
        if (descricao != null) {
            entrega.setDescricao(descricao);
        }
        
        if (prazoDesejado != null) {
            entrega.setPrazoDesejado(prazoDesejado);
        }
        
        return entregaRepository.save(entrega);
    }

    // CANCELA ENTREGA
    @Transactional
    public Entrega cancelarEntrega(UUID idEntrega) {
        Entrega entrega = entregaRepository.findById(idEntrega)
                .orElseThrow(() -> new ProjetoInvalidoException("Entrega não encontrada com o ID: " + idEntrega));
        
        if (StatusEntrega.CANCELADA.equals(entrega.getStatus())) {
            throw new ProjetoInvalidoException("A entrega já está cancelada");
        }
        
        if (StatusEntrega.ENTREGUE.equals(entrega.getStatus())) {
            throw new ProjetoInvalidoException("Não é possível cancelar uma entrega já entregue");
        }
        
        entrega.setStatus(StatusEntrega.CANCELADA);
        
        return entregaRepository.save(entrega);
    }
}
