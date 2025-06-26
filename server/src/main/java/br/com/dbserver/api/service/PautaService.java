package br.com.dbserver.api.service;

import br.com.dbserver.api.dto.v1.PautaDTO;
import br.com.dbserver.api.exception.PautaNotFoundException;
import br.com.dbserver.api.model.Pauta;
import br.com.dbserver.api.repository.PautaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PautaService {

    private final PautaRepository pautaRepository;

    public Pauta criar(PautaDTO pautaDTO) {
        Pauta pauta = new Pauta();
        pauta.setTitulo(pautaDTO.getTitulo());
        pauta.setDescricao(pautaDTO.getDescricao());
        return pautaRepository.save(pauta);
    }

    public List<Pauta> listar() {
        return pautaRepository.findAll();
    }

    public Pauta buscarPorId(Long id) {
        return pautaRepository.findById(id)
                .orElseThrow(() -> new PautaNotFoundException("Pauta não encontrada com o id: " + id));
    }
}