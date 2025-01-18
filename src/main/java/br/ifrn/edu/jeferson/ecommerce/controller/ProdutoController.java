package br.ifrn.edu.jeferson.ecommerce.controller;

import br.ifrn.edu.jeferson.ecommerce.domain.Produto;
import br.ifrn.edu.jeferson.ecommerce.domain.dtos.ProdutoDTO;
import br.ifrn.edu.jeferson.ecommerce.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/produtos")
@Tag(name = "Produtos", description = "API de gerenciamento de produtos.")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @Operation(summary = "Criar produto")
    @PostMapping
    public ResponseEntity<ProdutoDTO> salvar(@RequestBody ProdutoDTO produtoDTO){

        ProdutoDTO produtoResponse = produtoService.salvar(produtoDTO);

        return ResponseEntity.ok(produtoResponse);

    }

    @Operation(summary = "Listar produtos.")
    @GetMapping
    public Page<Produto> listar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) BigDecimal precoMin,
            @RequestParam(required = false) BigDecimal precoMax,
            @RequestParam(required = false) Integer estoque,
            @RequestParam(required = false) Integer pageNumber,
            @RequestParam(required = false) Integer pageSize
            ){

        return produtoService.listar(nome, precoMin, precoMax, estoque, pageNumber, pageSize);
    }

    @Operation(summary = "Buscar produto.")
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoDTO> buscarPorId(@PathVariable Long id){
        return ResponseEntity.ok(produtoService.buscarPorId(id));
    }

    @Operation(summary = "Atualizar produto.")
    @PutMapping("/{id}")
    public ResponseEntity<ProdutoDTO> atualizarProduto(
            @PathVariable Long id,
            @RequestBody ProdutoDTO produtoDTO
    ){
        return ResponseEntity.ok(produtoService.atualizarProduto(id, produtoDTO));
    }

    @Operation(summary = "Deletar produto.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id){
        produtoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Atualizar estoque.")
    @PatchMapping("/{id}")
    public ResponseEntity<ProdutoDTO> atualizarEstoque(
            @PathVariable Long id,
            @RequestBody Integer estoque
    ){
        return ResponseEntity.ok(produtoService.atualizarEstoque(id, estoque));
    }
}
