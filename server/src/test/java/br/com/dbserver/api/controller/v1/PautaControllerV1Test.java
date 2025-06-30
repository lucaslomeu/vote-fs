package br.com.dbserver.api.controller.v1;

import br.com.dbserver.api.dto.v1.PautaDTO;
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
        PautaDTO dto = new PautaDTO(null, "Titulo", "Descricao", false);
        Mockito.when(pautaService.criar(any(PautaDTO.class))).thenReturn(null); // Não importa o retorno
        mockMvc.perform(post("/api/v1/pautas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    void testListar() throws Exception {
        PautaDTO pauta1 = new PautaDTO(1L, "Titulo A", "Desc A", true);
        PautaDTO pauta2 = new PautaDTO(2L, "Titulo B", "Desc B", false);
        Mockito.when(pautaService.listar()).thenReturn(Arrays.asList(pauta1, pauta2));
        mockMvc.perform(get("/api/v1/pautas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }
}
