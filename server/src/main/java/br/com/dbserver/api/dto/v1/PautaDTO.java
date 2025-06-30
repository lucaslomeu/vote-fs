package br.com.dbserver.api.dto.v1;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PautaDTO {
    private Long id;
    private String titulo;
    private String descricao;
    private Boolean sessaoAberta;
}