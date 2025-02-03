package br.ifrn.edu.jeferson.ecommerce.domain.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO para requisição de produtos")
public class ProdutoDTO {

    @Schema(description = "ID do produto", example = "1")
    private Long id;

    @Schema(description = "Nome do produto", example = "Mouse")
    @NotBlank(message = "Nome do produto é obrigatório.")
    private String nome;

    @Schema(description = "Descrição do produto", example = "Mouse com FIO, possui RGB...")
    @NotBlank(message = "Descrição do produto é obrigatório.")
    private String descricao;

    @Schema(description = "Descrição do produto", example = "Mouse com FIO, possui RGB...")
    @Positive
    private BigDecimal preco;

    @Schema(description = "Estoque do produto", example = "5")
    @Min(value = 0, message = "O estoque não pode ser negativo.")
    private Integer estoque;

    @Schema(description = "IDs das categorias associadas ao produto")
    private List<Long> categorias;
}
