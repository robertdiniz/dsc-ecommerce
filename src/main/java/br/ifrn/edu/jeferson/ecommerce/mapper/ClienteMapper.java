package br.ifrn.edu.jeferson.ecommerce.mapper;

import br.ifrn.edu.jeferson.ecommerce.domain.Cliente;
import br.ifrn.edu.jeferson.ecommerce.domain.Endereco;
import br.ifrn.edu.jeferson.ecommerce.domain.dtos.ClienteRequestDTO;
import br.ifrn.edu.jeferson.ecommerce.domain.dtos.ClienteResponseDTO;
import br.ifrn.edu.jeferson.ecommerce.domain.dtos.EnderecoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClienteMapper {

    ClienteResponseDTO toDTO(Cliente cliente);

    @Mapping(source = "endereco", target = "endereco")
    Cliente toEntity(ClienteRequestDTO clienteResponseDTO);

    //Endereco toEnderecoEntity(EnderecoDTO enderecoRequestDTO);
}
