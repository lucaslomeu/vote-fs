package br.com.dbserver.api.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = { "sessao_votacao_id", "id_associado" })
})
public class Voto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sessao_votacao_id", nullable = false)
    private SessaoVotacao sessaoVotacao;

    @Column(name = "id_associado", nullable = false)
    private String idAssociado;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OpcaoVoto opcaoVoto;
}