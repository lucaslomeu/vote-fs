package br.com.dbserver.api.service;

import br.com.dbserver.api.dto.v1.PautaDTO;
import br.com.dbserver.api.exception.PautaNotFoundException;
import br.com.dbserver.api.model.Pauta;
import br.com.dbserver.api.model.SessaoVotacao;
import br.com.dbserver.api.repository.PautaRepository;
import br.com.dbserver.api.repository.SessaoVotacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PautaService {

    private final PautaRepository pautaRepository;
    private final SessaoVotacaoRepository sessaoVotacaoRepository;

    public Pauta criar(PautaDTO pautaDTO) {
        Pauta pauta = new Pauta();
        pauta.setTitulo(pautaDTO.getTitulo());
        pauta.setDescricao(pautaDTO.getDescricao());
        return pautaRepository.save(pauta);
    }

    public List<PautaDTO> listar() {
        return pautaRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public Pauta buscarPorId(Long id) {
        return pautaRepository.findById(id)
                .orElseThrow(() -> new PautaNotFoundException("Pauta não encontrada com o id: " + id));
    }

    public PautaDTO toDTO(Pauta pauta) {
        boolean sessaoAberta = sessaoVotacaoRepository.findByPautaId(pauta.getId())
                .map(SessaoVotacao::isAberta)
                .orElse(false);
        return new PautaDTO(pauta.getId(), pauta.getTitulo(), pauta.getDescricao(), sessaoAberta);
    }
}