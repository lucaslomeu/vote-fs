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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SessaoVotacaoService {

    private final SessaoVotacaoRepository sessaoVotacaoRepository;
    private final VotoRepository votoRepository;
    private final PautaService pautaService;

    private static final int DURACAO_PADRAO_MINUTOS = 1;

    public SessaoVotacao abrirSessao(AbrirSessaoDTO abrirSessaoDTO) {
        Pauta pauta = pautaService.buscarPorId(abrirSessaoDTO.getPautaId());

        sessaoVotacaoRepository.findByPautaId(pauta.getId()).ifPresent(s -> {
            throw new SessaoVotacaoException("Já existe uma sessão de votação para esta pauta.");
        });

        SessaoVotacao sessaoVotacao = new SessaoVotacao();
        sessaoVotacao.setPauta(pauta);
        sessaoVotacao.setDataAbertura(LocalDateTime.now());
        sessaoVotacao.setDuracaoEmMinutos(
                Optional.ofNullable(abrirSessaoDTO.getDuracaoEmMinutos()).orElse(DURACAO_PADRAO_MINUTOS));

        return sessaoVotacaoRepository.save(sessaoVotacao);
    }

    public Voto votar(Long pautaId, VotoDTO votoDTO) {
        SessaoVotacao sessao = sessaoVotacaoRepository.findByPautaId(pautaId)
                .orElseThrow(
                        () -> new SessaoVotacaoException("Sessão de votação não encontrada para a pauta informada."));

        if (!sessao.isAberta()) {
            throw new SessaoVotacaoException("A sessão de votação está fechada.");
        }

        if (votoRepository.existsBySessaoVotacaoIdAndIdAssociado(sessao.getId(), votoDTO.getIdAssociado())) {
            throw new SessaoVotacaoException("Associado já votou nesta pauta.");
        }

        Voto voto = new Voto();
        voto.setSessaoVotacao(sessao);
        voto.setIdAssociado(votoDTO.getIdAssociado());
        voto.setOpcaoVoto(votoDTO.getOpcaoVoto());

        return votoRepository.save(voto);
    }

    public ResultadoDTO obterResultado(Long pautaId) {
        Pauta pauta = pautaService.buscarPorId(pautaId);
        SessaoVotacao sessao = sessaoVotacaoRepository.findByPautaId(pautaId)
                .orElseThrow(() -> new SessaoVotacaoException("Nenhuma sessão de votação encontrada para esta pauta."));

        long totalSim = votoRepository.countBySessaoVotacaoIdAndOpcaoVoto(sessao.getId(), OpcaoVoto.SIM);
        long totalNao = votoRepository.countBySessaoVotacaoIdAndOpcaoVoto(sessao.getId(), OpcaoVoto.NAO);

        String sessaoAberta = sessao.isAberta() ? "Aberta" : "Fechada";

        return new ResultadoDTO(
                pauta,
                totalSim,
                totalNao,
                totalSim + totalNao,
                sessaoAberta);
    }
}