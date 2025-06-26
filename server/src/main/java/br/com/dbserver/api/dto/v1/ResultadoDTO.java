package br.com.dbserver.api.dto.v1;

import br.com.dbserver.api.model.Pauta;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultadoDTO {
    private Pauta pauta;
    private long totalSim;
    private long totalNao;
    private long totalVotos;
    private String statusSessao;
}