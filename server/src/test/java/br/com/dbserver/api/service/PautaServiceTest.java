package br.com.dbserver.api.service;

import br.com.dbserver.api.dto.v1.PautaDTO;
import br.com.dbserver.api.exception.PautaNotFoundException;
import br.com.dbserver.api.model.Pauta;
import br.com.dbserver.api.repository.PautaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PautaServiceTest {
    @Mock
    private PautaRepository pautaRepository;

    @InjectMocks
    private PautaService pautaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCriar() {
        PautaDTO dto = new PautaDTO();
        dto.setTitulo("Titulo");
        dto.setDescricao("Descricao");
        Pauta pauta = new Pauta();
        pauta.setTitulo(dto.getTitulo());
        pauta.setDescricao(dto.getDescricao());
        when(pautaRepository.save(any(Pauta.class))).thenReturn(pauta);
        Pauta result = pautaService.criar(dto);
        assertEquals(dto.getTitulo(), result.getTitulo());
        assertEquals(dto.getDescricao(), result.getDescricao());
    }

    @Test
    void testListar() {
        Pauta pauta1 = new Pauta();
        Pauta pauta2 = new Pauta();
        when(pautaRepository.findAll()).thenReturn(Arrays.asList(pauta1, pauta2));
        List<Pauta> pautas = pautaService.listar();
        assertEquals(2, pautas.size());
    }

    @Test
    void testBuscarPorIdFound() {
        Pauta pauta = new Pauta();
        pauta.setTitulo("Titulo");
        when(pautaRepository.findById(1L)).thenReturn(Optional.of(pauta));
        Pauta result = pautaService.buscarPorId(1L);
        assertEquals("Titulo", result.getTitulo());
    }

    @Test
    void testBuscarPorIdNotFound() {
        when(pautaRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(PautaNotFoundException.class, () -> pautaService.buscarPorId(1L));
    }
}
