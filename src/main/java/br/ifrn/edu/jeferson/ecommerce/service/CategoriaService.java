package br.ifrn.edu.jeferson.ecommerce.service;

import br.ifrn.edu.jeferson.ecommerce.domain.Categoria;
import br.ifrn.edu.jeferson.ecommerce.domain.Produto;
import br.ifrn.edu.jeferson.ecommerce.domain.dtos.CategoriaRequestDTO;
import br.ifrn.edu.jeferson.ecommerce.domain.dtos.CategoriaResponseDTO;
import br.ifrn.edu.jeferson.ecommerce.exception.BusinessException;
import br.ifrn.edu.jeferson.ecommerce.exception.ResourceNotFoundException;
import br.ifrn.edu.jeferson.ecommerce.mapper.CategoriaMapper;
import br.ifrn.edu.jeferson.ecommerce.repository.CategoriaRepository;
import br.ifrn.edu.jeferson.ecommerce.repository.ProdutoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CategoriaService {
    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private CategoriaMapper mapper;
    @Autowired
    private CategoriaMapper categoriaMapper;

    @Autowired
    private ProdutoRepository produtoRepository;

    public CategoriaResponseDTO salvar(CategoriaRequestDTO categoriaDto) {
        log.info("Criando categoria: {}", categoriaDto.getNome());
        var categoria =  mapper.toEntity(categoriaDto);

        if (categoriaRepository.existsByNome(categoria.getNome())) {
            log.error("Erro: Já existe uma categoria com esse nome {}", categoria.getNome());
            throw new BusinessException("Já existe uma categoria com esse nome");
        }

        categoriaRepository.save(categoria);
        log.info("Categoria {} criada com sucesso.", categoria.getNome());
        return mapper.toResponseDTO(categoria);
    }

    public List<CategoriaResponseDTO> lista(){
        log.info("Listando todas as categorias");
        List<Categoria> categorias = categoriaRepository.findAll();
        log.info("Total de categorias encontradas: {}", categorias.size());
        return mapper.toDTOList (categorias);
    }

    public void deletar(Long id) {
        log.info("Tentando deletar a categoria com ID: {}", id);
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Erro: Categoria com ID {} não encontrada", id);
                    return new ResourceNotFoundException("Categoria não encontrada");
                });

        if (!categoria.getProdutos().isEmpty()) {
            log.error("Erro: Não é possível remover a categoria com ID {} porque há produtos associados a ela.", id);
            throw new BusinessException("Não é possível remover a categoria, pois há produtos associados a ela.");
        }

        categoriaRepository.delete(categoria);
        log.info("Categoria com ID {} deletada com sucesso.", id);
    }

    public CategoriaResponseDTO atualizar(Long id, CategoriaRequestDTO categoriaDto) {
        log.info("Atualizando categoria com ID: {}", id);
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Erro: Categoria com ID {} não encontrada", id);
                    return new ResourceNotFoundException("Categoria não encontrada");
                });

        if (!categoria.getNome().equals(categoriaDto.getNome()) && categoriaRepository.existsByNome(categoriaDto.getNome())) {
            log.error("Erro: Já existe uma categoria com o nome {}", categoriaDto.getNome());
            throw  new BusinessException("Já existe uma categoria com esse nome");
        }

        categoriaMapper.updateEntityFromDTO(categoriaDto, categoria);
        var categoriaAlterada = categoriaRepository.save(categoria);
        log.info("Categoria com ID {} atualizada com sucesso.", id);
        return categoriaMapper.toResponseDTO(categoriaAlterada);
    }

    public CategoriaResponseDTO buscarPorId(Long id) {
        log.info("Buscando categoria com ID: {}", id);
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Erro: Categoria com ID {} não encontrada", id);
                    return new ResourceNotFoundException("Categoria não encontrada");
                });

        return categoriaMapper.toResponseDTO(categoria);
    }

    public void associarCategorias(Long categoriaId, Long produtoId){
        log.info("Associando produto com ID {} à categoria com ID {}", produtoId, categoriaId);
        Categoria categoria = categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));

        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

        if (!categoria.getProdutos().contains(produto)) {
            categoria.getProdutos().add(produto);
            produto.getCategorias().add(categoria);
            categoriaRepository.save(categoria);
            produtoRepository.save(produto);
            log.info("Produto com ID {} associado à categoria com ID {}", produtoId, categoriaId);
        } else {
            log.warn("O produto com ID {} já está associado à categoria com ID {}", produtoId, categoriaId);
        }
    }

    public void desassociarCategorias(Long categoriaId, Long produtoId){
        log.info("Desassociando produto com ID {} à categoria com ID {}", produtoId, categoriaId);
        Categoria categoria = categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));

        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

        if (categoria.getProdutos().contains(produto)) {
            categoria.getProdutos().remove(produto);
            produto.getCategorias().remove(categoria);

            categoriaRepository.save(categoria);
            produtoRepository.save(produto);
            log.info("Produto com ID {} desassociado da categoria com ID {}", produtoId, categoriaId);
        } else {
            log.warn("O produto com ID {} não está associado a categoria com ID {}", produtoId, categoriaId);
            throw new BusinessException("O produto não está associado a esta categoria");
        }

    }

}
