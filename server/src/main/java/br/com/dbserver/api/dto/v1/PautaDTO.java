package br.com.dbserver.api.dto.v1;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PautaDTO {
    private String titulo;
    private String descricao;
}