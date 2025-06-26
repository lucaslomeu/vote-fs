package br.com.dbserver.api.dto.v1;

import br.com.dbserver.api.model.OpcaoVoto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VotoResponseDTO {
    private Long id;
    private String idAssociado;
    private OpcaoVoto opcaoVoto;
} 