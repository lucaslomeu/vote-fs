package br.com.dbserver.api.service;

import br.com.dbserver.api.dto.v1.AbrirSessaoDTO;
import br.com.dbserver.api.dto.v1.ResultadoDTO;
import br.com.dbserver.api.dto.v1.VotoDTO;
import br.com.dbserver.api.exception.SessaoVotacaoException;
import br.com.dbserver.api.model.OpcaoVoto;
import br.com.dbserver.api.model.Pauta;
import br.com.dbserver.api.model.SessaoVotacao;
import br.com.dbserver.api.model.Voto;
import br.com.dbserver.api.repository.SessaoVotacaoRepository;
import br.com.dbserver.api.repository.VotoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SessaoVotacaoServiceTest {

    @Mock
    private SessaoVotacaoRepository sessaoVotacaoRepository;

    @Mock
    private VotoRepository votoRepository;

    @Mock
    private PautaService pautaService;

    @InjectMocks
    private SessaoVotacaoService sessaoVotacaoService;

    private Pauta pauta;
    private SessaoVotacao sessaoVotacao;
    private Voto voto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        pauta = new Pauta();
        pauta.setId(1L);
        pauta.setTitulo("Teste Pauta");
        pauta.setDescricao("Descrição da pauta de teste");

        sessaoVotacao = new SessaoVotacao();
        sessaoVotacao.setId(1L);
        sessaoVotacao.setPauta(pauta);
        sessaoVotacao.setDataAbertura(LocalDateTime.now());
        sessaoVotacao.setDuracaoEmMinutos(1);

        voto = new Voto();
        voto.setId(1L);
        voto.setSessaoVotacao(sessaoVotacao);
        voto.setIdAssociado("123456");
        voto.setOpcaoVoto(OpcaoVoto.SIM);
    }

    @Test
    void testAbrirSessao_Sucesso() {
        AbrirSessaoDTO abrirSessaoDTO = new AbrirSessaoDTO();
        abrirSessaoDTO.setPautaId(1L);
        abrirSessaoDTO.setDuracaoEmMinutos(5);

        when(pautaService.buscarPorId(1L)).thenReturn(pauta);
        when(sessaoVotacaoRepository.findByPautaId(1L)).thenReturn(Optional.empty());
        when(sessaoVotacaoRepository.save(any(SessaoVotacao.class))).thenReturn(sessaoVotacao);

        SessaoVotacao resultado = sessaoVotacaoService.abrirSessao(abrirSessaoDTO);

        assertNotNull(resultado);
        assertEquals(pauta, resultado.getPauta());
        verify(pautaService).buscarPorId(1L);
        verify(sessaoVotacaoRepository).findByPautaId(1L);
        verify(sessaoVotacaoRepository).save(any(SessaoVotacao.class));
    }

    @Test
    void testAbrirSessao_DuracaoPadrao() {
        AbrirSessaoDTO abrirSessaoDTO = new AbrirSessaoDTO();
        abrirSessaoDTO.setPautaId(1L);

        when(pautaService.buscarPorId(1L)).thenReturn(pauta);
        when(sessaoVotacaoRepository.findByPautaId(1L)).thenReturn(Optional.empty());
        when(sessaoVotacaoRepository.save(any(SessaoVotacao.class))).thenReturn(sessaoVotacao);

        SessaoVotacao resultado = sessaoVotacaoService.abrirSessao(abrirSessaoDTO);

        assertNotNull(resultado);
        verify(sessaoVotacaoRepository).save(any(SessaoVotacao.class));
    }

    @Test
    void testAbrirSessao_SessaoJaExiste() {
        AbrirSessaoDTO abrirSessaoDTO = new AbrirSessaoDTO();
        abrirSessaoDTO.setPautaId(1L);

        when(pautaService.buscarPorId(1L)).thenReturn(pauta);
        when(sessaoVotacaoRepository.findByPautaId(1L)).thenReturn(Optional.of(sessaoVotacao));

        SessaoVotacaoException exception = assertThrows(SessaoVotacaoException.class,
                () -> sessaoVotacaoService.abrirSessao(abrirSessaoDTO));

        assertEquals("Já existe uma sessão de votação para esta pauta.", exception.getMessage());
        verify(sessaoVotacaoRepository, never()).save(any(SessaoVotacao.class));
    }

    @Test
    void testVotar_Sucesso() {
        VotoDTO votoDTO = new VotoDTO();
        votoDTO.setIdAssociado("123456");
        votoDTO.setOpcaoVoto(OpcaoVoto.SIM);

        when(sessaoVotacaoRepository.findByPautaId(1L)).thenReturn(Optional.of(sessaoVotacao));
        when(votoRepository.existsBySessaoVotacaoIdAndIdAssociado(1L, "123456")).thenReturn(false);
        when(votoRepository.save(any(Voto.class))).thenReturn(voto);

        Voto resultado = sessaoVotacaoService.votar(1L, votoDTO);

        assertNotNull(resultado);
        assertEquals("123456", resultado.getIdAssociado());
        assertEquals(OpcaoVoto.SIM, resultado.getOpcaoVoto());
        verify(votoRepository).save(any(Voto.class));
    }

    @Test
    void testVotar_SessaoNaoEncontrada() {
        VotoDTO votoDTO = new VotoDTO();
        votoDTO.setIdAssociado("123456");
        votoDTO.setOpcaoVoto(OpcaoVoto.SIM);

        when(sessaoVotacaoRepository.findByPautaId(1L)).thenReturn(Optional.empty());

        SessaoVotacaoException exception = assertThrows(SessaoVotacaoException.class,
                () -> sessaoVotacaoService.votar(1L, votoDTO));

        assertEquals("Sessão de votação não encontrada para a pauta informada.", exception.getMessage());
        verify(votoRepository, never()).save(any(Voto.class));
    }

    @Test
    void testVotar_SessaoFechada() {
        VotoDTO votoDTO = new VotoDTO();
        votoDTO.setIdAssociado("123456");
        votoDTO.setOpcaoVoto(OpcaoVoto.SIM);

        SessaoVotacao sessaoFechada = new SessaoVotacao();
        sessaoFechada.setId(1L);
        sessaoFechada.setPauta(pauta);
        sessaoFechada.setDataAbertura(LocalDateTime.now().minusMinutes(2));
        sessaoFechada.setDuracaoEmMinutos(1);

        when(sessaoVotacaoRepository.findByPautaId(1L)).thenReturn(Optional.of(sessaoFechada));

        SessaoVotacaoException exception = assertThrows(SessaoVotacaoException.class,
                () -> sessaoVotacaoService.votar(1L, votoDTO));

        assertEquals("A sessão de votação está fechada.", exception.getMessage());
        verify(votoRepository, never()).save(any(Voto.class));
    }

    @Test
    void testVotar_AssociadoJaVotou() {
        VotoDTO votoDTO = new VotoDTO();
        votoDTO.setIdAssociado("123456");
        votoDTO.setOpcaoVoto(OpcaoVoto.SIM);

        when(sessaoVotacaoRepository.findByPautaId(1L)).thenReturn(Optional.of(sessaoVotacao));
        when(votoRepository.existsBySessaoVotacaoIdAndIdAssociado(1L, "123456")).thenReturn(true);

        SessaoVotacaoException exception = assertThrows(SessaoVotacaoException.class,
                () -> sessaoVotacaoService.votar(1L, votoDTO));

        assertEquals("Associado já votou nesta pauta.", exception.getMessage());
        verify(votoRepository, never()).save(any(Voto.class));
    }

    @Test
    void testObterResultado_Sucesso() {
        when(pautaService.buscarPorId(1L)).thenReturn(pauta);
        when(sessaoVotacaoRepository.findByPautaId(1L)).thenReturn(Optional.of(sessaoVotacao));
        when(votoRepository.countBySessaoVotacaoIdAndOpcaoVoto(1L, OpcaoVoto.SIM)).thenReturn(5L);
        when(votoRepository.countBySessaoVotacaoIdAndOpcaoVoto(1L, OpcaoVoto.NAO)).thenReturn(3L);

        ResultadoDTO resultado = sessaoVotacaoService.obterResultado(1L);

        assertNotNull(resultado);
        assertEquals(pauta, resultado.getPauta());
        assertEquals(5L, resultado.getTotalSim());
        assertEquals(3L, resultado.getTotalNao());
        assertEquals(8L, resultado.getTotalVotos());
        assertEquals("Aberta", resultado.getStatusSessao());
    }

    @Test
    void testObterResultado_SessaoNaoEncontrada() {
        when(pautaService.buscarPorId(1L)).thenReturn(pauta);
        when(sessaoVotacaoRepository.findByPautaId(1L)).thenReturn(Optional.empty());

        SessaoVotacaoException exception = assertThrows(SessaoVotacaoException.class,
                () -> sessaoVotacaoService.obterResultado(1L));

        assertEquals("Nenhuma sessão de votação encontrada para esta pauta.", exception.getMessage());
    }

    @Test
    void testObterResultado_SessaoFechada() {
        SessaoVotacao sessaoFechada = new SessaoVotacao();
        sessaoFechada.setId(1L);
        sessaoFechada.setPauta(pauta);
        sessaoFechada.setDataAbertura(LocalDateTime.now().minusMinutes(2));
        sessaoFechada.setDuracaoEmMinutos(1);

        when(pautaService.buscarPorId(1L)).thenReturn(pauta);
        when(sessaoVotacaoRepository.findByPautaId(1L)).thenReturn(Optional.of(sessaoFechada));
        when(votoRepository.countBySessaoVotacaoIdAndOpcaoVoto(1L, OpcaoVoto.SIM)).thenReturn(2L);
        when(votoRepository.countBySessaoVotacaoIdAndOpcaoVoto(1L, OpcaoVoto.NAO)).thenReturn(1L);

        ResultadoDTO resultado = sessaoVotacaoService.obterResultado(1L);

        assertNotNull(resultado);
        assertEquals("Fechada", resultado.getStatusSessao());
        assertEquals(3L, resultado.getTotalVotos());
    }

    @Test
    void testObterResultado_SemVotos() {
        when(pautaService.buscarPorId(1L)).thenReturn(pauta);
        when(sessaoVotacaoRepository.findByPautaId(1L)).thenReturn(Optional.of(sessaoVotacao));
        when(votoRepository.countBySessaoVotacaoIdAndOpcaoVoto(1L, OpcaoVoto.SIM)).thenReturn(0L);
        when(votoRepository.countBySessaoVotacaoIdAndOpcaoVoto(1L, OpcaoVoto.NAO)).thenReturn(0L);

        ResultadoDTO resultado = sessaoVotacaoService.obterResultado(1L);

        assertNotNull(resultado);
        assertEquals(0L, resultado.getTotalSim());
        assertEquals(0L, resultado.getTotalNao());
        assertEquals(0L, resultado.getTotalVotos());
    }
}