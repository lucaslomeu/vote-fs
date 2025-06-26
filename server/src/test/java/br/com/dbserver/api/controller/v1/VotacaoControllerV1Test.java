package br.com.dbserver.api.controller.v1;

import br.com.dbserver.api.dto.v1.AbrirSessaoDTO;
import br.com.dbserver.api.dto.v1.ResultadoDTO;
import br.com.dbserver.api.dto.v1.VotoDTO;
import br.com.dbserver.api.model.OpcaoVoto;
import br.com.dbserver.api.model.Pauta;
import br.com.dbserver.api.model.SessaoVotacao;
import br.com.dbserver.api.model.Voto;
import br.com.dbserver.api.service.SessaoVotacaoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VotacaoControllerV1.class)
class VotacaoControllerV1Test {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private SessaoVotacaoService sessaoVotacaoService;

        @Autowired
        private ObjectMapper objectMapper;

        @Test
        void testAbrirSessao() throws Exception {
                AbrirSessaoDTO abrirSessaoDTO = new AbrirSessaoDTO();
                abrirSessaoDTO.setPautaId(1L);
                abrirSessaoDTO.setDuracaoEmMinutos(5);

                Pauta pauta = new Pauta();
                pauta.setId(1L);
                pauta.setTitulo("Pauta Teste");
                pauta.setDescricao("Descrição da pauta");

                SessaoVotacao sessaoVotacao = new SessaoVotacao();
                sessaoVotacao.setId(1L);
                sessaoVotacao.setPauta(pauta);
                sessaoVotacao.setDataAbertura(LocalDateTime.now());
                sessaoVotacao.setDuracaoEmMinutos(5);

                Mockito.when(sessaoVotacaoService.abrirSessao(any(AbrirSessaoDTO.class))).thenReturn(sessaoVotacao);

                mockMvc.perform(post("/api/v1/votacao/sessoes/abrir")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(abrirSessaoDTO)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.id").value(1))
                                .andExpect(jsonPath("$.pauta.id").value(1))
                                .andExpect(jsonPath("$.pauta.titulo").value("Pauta Teste"))
                                .andExpect(jsonPath("$.duracaoEmMinutos").value(5));
        }

        @Test
        void testAbrirSessao_DuracaoPadrao() throws Exception {

                AbrirSessaoDTO abrirSessaoDTO = new AbrirSessaoDTO();
                abrirSessaoDTO.setPautaId(1L);

                Pauta pauta = new Pauta();
                pauta.setId(1L);
                pauta.setTitulo("Pauta Teste");

                SessaoVotacao sessaoVotacao = new SessaoVotacao();
                sessaoVotacao.setId(1L);
                sessaoVotacao.setPauta(pauta);
                sessaoVotacao.setDataAbertura(LocalDateTime.now());
                sessaoVotacao.setDuracaoEmMinutos(1);

                Mockito.when(sessaoVotacaoService.abrirSessao(any(AbrirSessaoDTO.class))).thenReturn(sessaoVotacao);

                mockMvc.perform(post("/api/v1/votacao/sessoes/abrir")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(abrirSessaoDTO)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.duracaoEmMinutos").value(1));
        }

        @Test
        void testVotar() throws Exception {
                VotoDTO votoDTO = new VotoDTO();
                votoDTO.setIdAssociado("123456");
                votoDTO.setOpcaoVoto(OpcaoVoto.SIM);

                Mockito.when(sessaoVotacaoService.votar(eq(1L), any(VotoDTO.class)))
                                .thenReturn(new Voto(1L, null, "123456", OpcaoVoto.SIM));

                mockMvc.perform(post("/api/v1/votacao/pautas/1/votos")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(votoDTO)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.id").value(1))
                                .andExpect(jsonPath("$.idAssociado").value("123456"))
                                .andExpect(jsonPath("$.opcaoVoto").value("SIM"));
        }

        @Test
        void testVotar_VotoNao() throws Exception {
                VotoDTO votoDTO = new VotoDTO();
                votoDTO.setIdAssociado("789012");
                votoDTO.setOpcaoVoto(OpcaoVoto.NAO);

                Mockito.when(sessaoVotacaoService.votar(eq(1L), any(VotoDTO.class)))
                                .thenReturn(new Voto(2L, null, "789012", OpcaoVoto.NAO));

                mockMvc.perform(post("/api/v1/votacao/pautas/1/votos")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(votoDTO)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.id").value(2))
                                .andExpect(jsonPath("$.idAssociado").value("789012"))
                                .andExpect(jsonPath("$.opcaoVoto").value("NAO"));
        }

        @Test
        void testObterResultado() throws Exception {
                Pauta pauta = new Pauta();
                pauta.setId(1L);
                pauta.setTitulo("Pauta Teste");
                pauta.setDescricao("Descrição da pauta");

                ResultadoDTO resultadoDTO = new ResultadoDTO();
                resultadoDTO.setPauta(pauta);
                resultadoDTO.setTotalSim(5L);
                resultadoDTO.setTotalNao(3L);
                resultadoDTO.setTotalVotos(8L);
                resultadoDTO.setStatusSessao("Aberta");

                Mockito.when(sessaoVotacaoService.obterResultado(1L)).thenReturn(resultadoDTO);

                mockMvc.perform(get("/api/v1/votacao/pautas/1/resultado"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.pauta.id").value(1))
                                .andExpect(jsonPath("$.pauta.titulo").value("Pauta Teste"))
                                .andExpect(jsonPath("$.totalSim").value(5))
                                .andExpect(jsonPath("$.totalNao").value(3))
                                .andExpect(jsonPath("$.totalVotos").value(8))
                                .andExpect(jsonPath("$.statusSessao").value("Aberta"));
        }

        @Test
        void testObterResultado_SessaoFechada() throws Exception {
                Pauta pauta = new Pauta();
                pauta.setId(1L);
                pauta.setTitulo("Pauta Teste");

                ResultadoDTO resultadoDTO = new ResultadoDTO();
                resultadoDTO.setPauta(pauta);
                resultadoDTO.setTotalSim(2L);
                resultadoDTO.setTotalNao(1L);
                resultadoDTO.setTotalVotos(3L);
                resultadoDTO.setStatusSessao("Fechada");

                Mockito.when(sessaoVotacaoService.obterResultado(1L)).thenReturn(resultadoDTO);

                mockMvc.perform(get("/api/v1/votacao/pautas/1/resultado"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.statusSessao").value("Fechada"))
                                .andExpect(jsonPath("$.totalVotos").value(3));
        }

        @Test
        void testObterResultado_SemVotos() throws Exception {
                Pauta pauta = new Pauta();
                pauta.setId(1L);
                pauta.setTitulo("Pauta Teste");

                ResultadoDTO resultadoDTO = new ResultadoDTO();
                resultadoDTO.setPauta(pauta);
                resultadoDTO.setTotalSim(0L);
                resultadoDTO.setTotalNao(0L);
                resultadoDTO.setTotalVotos(0L);
                resultadoDTO.setStatusSessao("Aberta");

                Mockito.when(sessaoVotacaoService.obterResultado(1L)).thenReturn(resultadoDTO);

                mockMvc.perform(get("/api/v1/votacao/pautas/1/resultado"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.totalSim").value(0))
                                .andExpect(jsonPath("$.totalNao").value(0))
                                .andExpect(jsonPath("$.totalVotos").value(0));
        }

        @Test
        void testVotar_ValidacaoFalha() throws Exception {
                VotoDTO votoDTO = new VotoDTO();
                votoDTO.setOpcaoVoto(OpcaoVoto.SIM);

                mockMvc.perform(post("/api/v1/votacao/pautas/1/votos")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(votoDTO)))
                                .andExpect(status().isBadRequest());
        }

        @Test
        void testVotar_ValidacaoOpcaoVotoFalha() throws Exception {
                VotoDTO votoDTO = new VotoDTO();
                votoDTO.setIdAssociado("123456");

                mockMvc.perform(post("/api/v1/votacao/pautas/1/votos")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(votoDTO)))
                                .andExpect(status().isBadRequest());
        }
}