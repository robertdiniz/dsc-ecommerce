package br.ifrn.edu.jeferson.ecommerce.domain.dtos;

import br.ifrn.edu.jeferson.ecommerce.domain.Cliente;
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
@Schema(description = "DTO para requisição de clientes")
public class ClienteRequestDTO {

    @Schema(description = "Nome da categoria", example = "Eletrônicos")
    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @Schema(description = "Email do cliente", example = "user@example.com")
    @NotBlank(message = "Email é obrigatório")
    private String email;

    @Schema(description = "CPF do cliente", example = "XXX.XXX.XXX-XX")
    @NotBlank(message = "CPF é obrigatório")
    private String cpf;

    @Schema(description = "Telefone do cliente", example = "XXXXXXX-XXXX")
    @NotBlank(message = "Telefone é obrigatório")
    private String telefone;

    @Schema(description = "Endereço do cliente", example = "CIDADE - ESTADO")
    private EnderecoDTO endereco;

    public Cliente toEntity(){
        Cliente cliente = new Cliente();
        cliente.setNome(this.nome);
        cliente.setEmail(this.email);
        cliente.setCpf(this.cpf);
        cliente.setTelefone(this.telefone);
        if (this.endereco != null) {
            Endereco enderecoEntity = this.endereco.toEntity();
            enderecoEntity.setCliente(cliente);
            cliente.setEndereco(enderecoEntity);
        }
        return cliente;
    }

}
