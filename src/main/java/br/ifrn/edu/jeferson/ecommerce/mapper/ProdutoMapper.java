package br.ifrn.edu.jeferson.ecommerce.mapper;

import br.ifrn.edu.jeferson.ecommerce.domain.Categoria;
import br.ifrn.edu.jeferson.ecommerce.domain.Produto;
import br.ifrn.edu.jeferson.ecommerce.domain.dtos.ProdutoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ProdutoMapper {

    @Mapping(source = "categorias", target = "categorias", qualifiedByName = "mapCategoriasParaIds")
    ProdutoDTO toDTO(Produto produto);

    @Mapping(source = "categorias", target = "categorias", qualifiedByName = "mapIdsParaCategorias")
    Produto toEntity(ProdutoDTO produtoDTO);

    List<ProdutoDTO> toDTOList(List<Produto> produtos);


    @Named("mapCategoriasParaIds")
    default List<Long> mapCategoriasParaIds(List<Categoria> categorias) {
        return categorias.stream()
                .map(Categoria::getId)
                .collect(Collectors.toList());
    }

    @Named("mapIdsParaCategorias")
    default List<Categoria> mapIdsParaCategorias(List<Long> ids) {
        if (ids == null) {
            return null;
        }
        return ids.stream()
                .map(id -> {
                    Categoria categoria = new Categoria();
                    categoria.setId(id);
                    return categoria;
                })
                .collect(Collectors.toList());
    }
}
