package br.com.dbserver.api.repository;

import br.com.dbserver.api.model.OpcaoVoto;
import br.com.dbserver.api.model.Voto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VotoRepository extends JpaRepository<Voto, Long> {

    long countBySessaoVotacaoIdAndOpcaoVoto(Long sessaoVotacaoId, OpcaoVoto opcaoVoto);

    boolean existsBySessaoVotacaoIdAndIdAssociado(Long sessaoVotacaoId, String idAssociado);
}