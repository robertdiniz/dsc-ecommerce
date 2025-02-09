package br.ifrn.edu.jeferson.ecommerce.mapper;

import br.ifrn.edu.jeferson.ecommerce.domain.ItemPedido;
import br.ifrn.edu.jeferson.ecommerce.domain.Pedido;
import br.ifrn.edu.jeferson.ecommerce.domain.dtos.ItemPedidoResponseDTO;
import br.ifrn.edu.jeferson.ecommerce.domain.dtos.PedidoResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = ItemPedidoMapper.class)
public interface PedidoMapper {

    PedidoMapper INSTANCE = Mappers.getMapper(PedidoMapper.class);

    @Mapping(source = "cliente.id", target = "clienteId")
    @Mapping(source = "statusPedido", target = "statusPedido")
    @Mapping(source = "itens", target = "itens", qualifiedByName = "mapItensParaDTO")
    PedidoResponseDTO toDTO(Pedido pedido);

    @Mapping(source = "produto.id", target = "produtoId")
    @Mapping(source = "quantidade", target = "quantidade")
    @Mapping(source = "valorUnitario", target = "valorUnitario")
    ItemPedidoResponseDTO toItemDTO(ItemPedido itemPedido);

    List<PedidoResponseDTO> toDTOList(List<Pedido> pedidos);

    @Named("mapItensParaDTO")
    default List<ItemPedidoResponseDTO> mapItensParaDTO(List<ItemPedido> itens) {
        return itens.stream()
                .map(this::toItemDTO)
                .toList();
    }

}
