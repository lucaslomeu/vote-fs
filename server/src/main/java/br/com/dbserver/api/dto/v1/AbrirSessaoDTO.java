package br.com.dbserver.api.dto.v1;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AbrirSessaoDTO {
    private Long pautaId;
    private Integer duracaoEmMinutos;
}