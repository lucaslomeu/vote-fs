package br.com.dbserver.api.controller.v1;

import br.com.dbserver.api.dto.v1.AbrirSessaoDTO;
import br.com.dbserver.api.dto.v1.ResultadoDTO;
import br.com.dbserver.api.dto.v1.VotoDTO;
import br.com.dbserver.api.model.SessaoVotacao;
import br.com.dbserver.api.model.Voto;
import br.com.dbserver.api.service.SessaoVotacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/votacao")
@RequiredArgsConstructor
public class VotacaoControllerV1 {

    private final SessaoVotacaoService sessaoVotacaoService;

    @PostMapping("/sessoes/abrir")
    public ResponseEntity<SessaoVotacao> abrirSessao(@Valid @RequestBody AbrirSessaoDTO abrirSessaoDTO) {
        SessaoVotacao sessao = sessaoVotacaoService.abrirSessao(abrirSessaoDTO);
        return new ResponseEntity<>(sessao, HttpStatus.CREATED);
    }

    @PostMapping("/pautas/{pautaId}/votos")
    public ResponseEntity<Voto> votar(@PathVariable Long pautaId, @Valid @RequestBody VotoDTO votoDTO) {
        Voto voto = sessaoVotacaoService.votar(pautaId, votoDTO);
        return new ResponseEntity<>(voto, HttpStatus.CREATED);
    }

    @GetMapping("/pautas/{pautaId}/resultado")
    public ResponseEntity<ResultadoDTO> obterResultado(@PathVariable Long pautaId) {
        ResultadoDTO resultado = sessaoVotacaoService.obterResultado(pautaId);
        return ResponseEntity.ok(resultado);
    }
}