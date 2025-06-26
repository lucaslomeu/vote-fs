package br.com.dbserver.api.dto.v1;

import br.com.dbserver.api.model.OpcaoVoto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VotoDTO {
    @NotBlank(message = "O ID do associado é obrigatório.")
    private String idAssociado;

    @NotNull(message = "A opção de voto é obrigatória.")
    private OpcaoVoto opcaoVoto;
}