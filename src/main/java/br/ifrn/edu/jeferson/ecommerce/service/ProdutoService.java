package br.ifrn.edu.jeferson.ecommerce.service;

import br.ifrn.edu.jeferson.ecommerce.domain.Cliente;
import br.ifrn.edu.jeferson.ecommerce.domain.Produto;
import br.ifrn.edu.jeferson.ecommerce.domain.dtos.AtualizarEstoqueDTO;
import br.ifrn.edu.jeferson.ecommerce.domain.dtos.ProdutoDTO;
import br.ifrn.edu.jeferson.ecommerce.domain.specificacion.ProdutoSpecification;
import br.ifrn.edu.jeferson.ecommerce.exception.BusinessException;
import br.ifrn.edu.jeferson.ecommerce.mapper.ProdutoMapper;
import br.ifrn.edu.jeferson.ecommerce.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ProdutoMapper produtoMapper;

    public ProdutoDTO salvar(ProdutoDTO produtoDTO){
        Produto produto = produtoMapper.toEntity(produtoDTO);

        produtoRepository.save(produto);

        return produtoMapper.toDTO(produto);
    }

    @Cacheable("products")
    public Page<Produto> listar(
            String nome,
            BigDecimal precoMin,
            BigDecimal precoMax,
            Integer estoque,
            Integer pageNumber,
            Integer pageSize
    ){

        if (pageNumber == null) {
            pageNumber = 0;
        }
        if (pageSize == null) {
            pageSize = 10;
        }

        Pageable page = PageRequest.of(pageNumber, pageSize);

        Specification<Produto> spec = Specification.where(ProdutoSpecification.nomeContains(nome))
                .and(ProdutoSpecification.precoMax(precoMax))
                .and(ProdutoSpecification.precoMin(precoMin))
                .and(ProdutoSpecification.estoque(estoque));

        return produtoRepository.findAll(spec, page);
    }

    public ProdutoDTO buscarPorId(Long id){
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Produto não encontrado com o ID: " + id));

        return produtoMapper.toDTO(produto);
    }

    public ProdutoDTO atualizarProduto(Long id, ProdutoDTO produtoDTO){
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Produto não encontrado com o ID: " + id));

        produto.setNome(produtoDTO.getNome());
        produto.setDescricao(produtoDTO.getDescricao());
        produto.setPreco(produtoDTO.getPreco());
        produto.setEstoque(produtoDTO.getEstoque());

        produtoRepository.save(produto);

        return produtoMapper.toDTO(produto);
    }

    public void deletar(Long id){
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Produto não encontrado com o ID: " + id));

        produtoRepository.delete(produto);
    }

    public void atualizarEstoque(Long id, Integer estoque){
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Produto não encontrado com o ID: " + id));
        if (estoque < 0) {
            throw new BusinessException("Estoque não pode ser negativo!");
        }
        produto.setEstoque(estoque);
        produtoRepository.save(produto);
    }

    public List<Produto> listarProdutosPorCategoria(Long categoriaId){
        return produtoRepository.findByCategorias_Id(categoriaId);
    }

}
