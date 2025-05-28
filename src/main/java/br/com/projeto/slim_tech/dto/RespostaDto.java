package br.com.projeto.slim_tech.dto;

import br.com.projeto.slim_tech.enums.Genero;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RespostaDto(@NotNull Integer idade,
                          @NotNull Double peso,
                          @NotNull Double altura ,
                          @NotNull String metaDoUsuario,
                          @NotNull Genero genero,
                          @NotBlank String problemaDeSaude) {

}
