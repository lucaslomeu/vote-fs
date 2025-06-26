package br.com.dbserver.api.controller.v1;

import br.com.dbserver.api.dto.v1.PautaDTO;
import br.com.dbserver.api.model.Pauta;
import br.com.dbserver.api.service.PautaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PautaControllerV1.class)
class PautaControllerV1Test {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PautaService pautaService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCriar() throws Exception {
        PautaDTO dto = new PautaDTO();
        dto.setTitulo("Titulo");
        dto.setDescricao("Descricao");
        Pauta pauta = new Pauta();
        pauta.setTitulo("Titulo");
        pauta.setDescricao("Descricao");
        Mockito.when(pautaService.criar(any(PautaDTO.class))).thenReturn(pauta);
        mockMvc.perform(post("/api/v1/pautas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.titulo").value("Titulo"));
    }

    @Test
    void testListar() throws Exception {
        Pauta pauta1 = new Pauta();
        pauta1.setTitulo("Titulo A");
        Pauta pauta2 = new Pauta();
        pauta2.setTitulo("Titulo B");
        Mockito.when(pautaService.listar()).thenReturn(Arrays.asList(pauta1, pauta2));
        mockMvc.perform(get("/api/v1/pautas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testBuscarPorId() throws Exception {
        Pauta pauta = new Pauta();
        pauta.setTitulo("Titulo A");
        Mockito.when(pautaService.buscarPorId(1L)).thenReturn(pauta);
        mockMvc.perform(get("/api/v1/pautas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Titulo A"));
    }
}
