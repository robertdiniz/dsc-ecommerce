package br.ifrn.edu.jeferson.ecommerce.domain.dtos;

import br.ifrn.edu.jeferson.ecommerce.domain.Endereco;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO para informações de endereço")
public class EnderecoDTO {

    @Schema(description = "Rua do endereço", example = "Rua das Flores")
    @NotBlank(message = "Rua é obrigatória")
    private String rua;

    @Schema(description = "Número do endereço", example = "123")
    @NotBlank(message = "Número é obrigatório")
    private String numero;

    @Schema(description = "Bairro do endereço", example = "Centro")
    @NotBlank(message = "Bairro é obrigatório")
    private String bairro;

    @Schema(description = "Cidade do endereço", example = "Natal")
    @NotBlank(message = "Cidade é obrigatória")
    private String cidade;

    @Schema(description = "Estado do endereço", example = "RN")
    @NotBlank(message = "Estado é obrigatório")
    private String estado;

    @Schema(description = "CEP do endereço", example = "59000-000")
    @NotBlank(message = "CEP é obrigatório")
    private String cep;

    public Endereco toEntity() {
        Endereco endereco = new Endereco();
        endereco.setRua(this.rua);
        endereco.setNumero(this.numero);
        endereco.setBairro(this.bairro);
        endereco.setCidade(this.cidade);
        endereco.setEstado(this.estado);
        endereco.setCep(this.cep);
        return endereco;
    }

}
