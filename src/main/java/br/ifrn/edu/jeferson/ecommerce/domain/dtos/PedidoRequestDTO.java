package br.ifrn.edu.jeferson.ecommerce.domain.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO para requisição de pedidos")
public class PedidoRequestDTO {
    private String statusPedido;
    private Long clienteId;
    @Min(value = 1, message = "Pedido deve ter no mínimo um item.")
    private List<ItemPedidoRequestDTO> itens;
}

