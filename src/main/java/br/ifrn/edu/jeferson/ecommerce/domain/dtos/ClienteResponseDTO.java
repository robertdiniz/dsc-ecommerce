package br.ifrn.edu.jeferson.ecommerce.domain.dtos;

import br.ifrn.edu.jeferson.ecommerce.domain.Endereco;
import br.ifrn.edu.jeferson.ecommerce.domain.Pedido;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO para resposta de cliente")
public class ClienteResponseDTO {

    @Schema(description = "ID do cliente", example = "1")
    private Long id;

    @Schema(description = "Nome do cliente", example = "Eletrônicos")
    private String nome;

    private String email;

    private String cpf;

    private String telefone;


    private Endereco endereco;

    private List<Pedido> pedidos = new ArrayList<>();



}
