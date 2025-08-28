package com.marketplace.ifba.service;

import com.marketplace.ifba.exception.AreaInvalidaException;
import com.marketplace.ifba.model.Area;
import com.marketplace.ifba.repository.AreaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AreaServiceTest {

    private AreaRepository areaRepository;
    private AreaService areaService;

    @BeforeEach
    void setUp() {
        areaRepository = mock(AreaRepository.class);
        areaService = new AreaService(areaRepository);
    }

    // Buscar Area Por ID
    @Test
    void deveBuscarAreaPorIdQuandoExistir() {
        UUID id = UUID.randomUUID();
        Area area = new Area();
        area.setIdArea(id);
        area.setNomeArea("TI");

        when(areaRepository.findById(id)).thenReturn(Optional.of(area));

        Area result = areaService.buscarAreaPorID(id);

        assertNotNull(result);
        assertEquals("TI", result.getNomeArea());
        assertEquals(id, result.getIdArea());
        verify(areaRepository).findById(id);
    }

    @Test
    void deveLancarExcecaoAoBuscarAreaInexistente() {
        UUID id = UUID.randomUUID();
        when(areaRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(AreaInvalidaException.class, () -> areaService.buscarAreaPorID(id));
        verify(areaRepository).findById(id);
    }

    // Buscar todas areas
    @Test
    void deveRetornarTodasAsAreas() {
        List<Area> areas = Arrays.asList(new Area(), new Area());
        when(areaRepository.findAll()).thenReturn(areas);

        List<Area> result = areaService.buscarTodasAreas();

        assertEquals(2, result.size());
        verify(areaRepository).findAll();
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoExistirAreas() {
        when(areaRepository.findAll()).thenReturn(Collections.emptyList());

        List<Area> result = areaService.buscarTodasAreas();

        assertTrue(result.isEmpty());
        verify(areaRepository).findAll();
    }

    // Registrar area
    @Test
    void deveRegistrarAreaQuandoNomeForUnico() {
        Area area = new Area();
        area.setNomeArea("TI");

        when(areaRepository.findAll()).thenReturn(Collections.emptyList());
        when(areaRepository.save(area)).thenReturn(area);

        Area result = areaService.registrarArea(area);

        assertNotNull(result);
        assertEquals("TI", result.getNomeArea());
        verify(areaRepository).save(area);
    }

    @Test
    void deveLancarExcecaoAoRegistrarAreaComNomeDuplicado() {
        Area area = new Area();
        area.setNomeArea("TI");

        Area areaExistente = new Area();
        areaExistente.setNomeArea("TI");

        when(areaRepository.findAll()).thenReturn(Collections.singletonList(areaExistente));

        assertThrows(AreaInvalidaException.class, () -> areaService.registrarArea(area));
        verify(areaRepository, never()).save(any());
    }

    // Atualizar Area
    @Test
    void deveAtualizarAreaQuandoExistir() {
        UUID id = UUID.randomUUID();
        Area areaAntiga = new Area();
        areaAntiga.setIdArea(id);
        areaAntiga.setNomeArea("TI");

        Area novaArea = new Area();
        novaArea.setNomeArea("Saúde");

        when(areaRepository.findById(id)).thenReturn(Optional.of(areaAntiga));
        when(areaRepository.save(any(Area.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Area result = areaService.atualizarArea(id, novaArea);

        assertEquals("Saúde", result.getNomeArea());

        ArgumentCaptor<Area> captor = ArgumentCaptor.forClass(Area.class);
        verify(areaRepository).save(captor.capture());
        assertEquals("Saúde", captor.getValue().getNomeArea());
        assertEquals(id, captor.getValue().getIdArea()); // Verifica se o ID permanece o mesmo
    }

    @Test
    void deveLancarExcecaoAoAtualizarAreaInexistente() {
        UUID id = UUID.randomUUID();
        Area novaArea = new Area();
        novaArea.setNomeArea("Saúde");

        when(areaRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(AreaInvalidaException.class, () -> areaService.atualizarArea(id, novaArea));
        verify(areaRepository, never()).save(any());
    }

    // Remover Area
    @Test
    void deveRemoverAreaQuandoExistir() {
        UUID id = UUID.randomUUID();
        when(areaRepository.existsById(id)).thenReturn(true);

        areaService.removerArea(id);

        verify(areaRepository).deleteById(id);
    }

    @Test
    void deveLancarExcecaoAoRemoverAreaInexistente() {
        UUID id = UUID.randomUUID();
        when(areaRepository.existsById(id)).thenReturn(false);

        assertThrows(AreaInvalidaException.class, () -> areaService.removerArea(id));
        verify(areaRepository, never()).deleteById(id);
    }

}
