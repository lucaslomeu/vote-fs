package br.com.dbserver.api.controller.v1;

import br.com.dbserver.api.dto.v1.PautaDTO;
import br.com.dbserver.api.model.Pauta;
import br.com.dbserver.api.service.PautaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pautas")
@RequiredArgsConstructor
public class PautaControllerV1 {

    private final PautaService pautaService;

    @PostMapping
    public ResponseEntity<Pauta> criar(@Valid @RequestBody PautaDTO pautaDTO) {
        Pauta novaPauta = pautaService.criar(pautaDTO);
        return new ResponseEntity<>(novaPauta, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Pauta>> listar() {
        List<Pauta> pautas = pautaService.listar();
        return ResponseEntity.ok(pautas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pauta> buscarPorId(@PathVariable Long id) {
        Pauta pauta = pautaService.buscarPorId(id);
        return ResponseEntity.ok(pauta);
    }
}