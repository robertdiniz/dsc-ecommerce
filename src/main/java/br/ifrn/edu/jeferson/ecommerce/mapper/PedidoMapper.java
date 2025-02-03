package br.ifrn.edu.jeferson.ecommerce.mapper;

import br.ifrn.edu.jeferson.ecommerce.domain.Pedido;
import br.ifrn.edu.jeferson.ecommerce.domain.dtos.PedidoResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PedidoMapper {

    PedidoMapper INSTANCE = Mappers.getMapper(PedidoMapper.class);

    @Mapping(source = "cliente.id", target = "clienteId")
    @Mapping(source = "statusPedido", target = "statusPedido")
    PedidoResponseDTO toDTO(Pedido pedido);

    List<PedidoResponseDTO> toDTOList(List<Pedido> pedidos);

}
