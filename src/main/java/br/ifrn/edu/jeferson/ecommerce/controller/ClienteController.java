package br.ifrn.edu.jeferson.ecommerce.controller;

import br.ifrn.edu.jeferson.ecommerce.domain.dtos.ClienteRequestDTO;
import br.ifrn.edu.jeferson.ecommerce.domain.dtos.ClienteResponseDTO;
import br.ifrn.edu.jeferson.ecommerce.domain.dtos.EnderecoDTO;
import br.ifrn.edu.jeferson.ecommerce.domain.dtos.PedidoResponseDTO;
import br.ifrn.edu.jeferson.ecommerce.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@Tag(name = "Clientes", description = "API de gerenciamento de clientes.")
@Slf4j
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Operation(summary = "Criar cliente")
    @PostMapping
    public ResponseEntity<ClienteResponseDTO> salvar(@RequestBody ClienteRequestDTO clienteRequestDTO){
        log.trace("Criando usuários");
        log.debug("Criando usuários");
        log.info("Criando usuários");
        log.warn("Criando usuários");
        log.error("Criando usuários");
        return ResponseEntity.ok(clienteService.salvar(clienteRequestDTO));
    }

    @Operation(summary = "Obter todos os clientes")
    @GetMapping
    public Page<ClienteResponseDTO> obterClientes(Pageable pageable) {
        return clienteService.obterClientes(pageable);
    }

    @Operation(summary = "Obter um cliente")
    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> obterPorId(@PathVariable Long id) {
        ClienteResponseDTO clienteResponseDTO = clienteService.obterClientePorId(id);
        return ResponseEntity.ok(clienteResponseDTO);
    }

    @Operation(summary = "Atualizar um cliente")
    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> atualizarCliente(
            @PathVariable Long id,
            @RequestBody ClienteRequestDTO dto
        ) {
        ClienteResponseDTO clienteResponseDTO = clienteService.atualizarCliente(id, dto);
        return ResponseEntity.ok(clienteResponseDTO);
    }

    @Operation(summary = "Deletar um cliente")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> atualizarCliente(@PathVariable Long id) {
        clienteService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Cadastrar endereço para cliente")
    @PostMapping("{id}/enderecos")
    public ResponseEntity<EnderecoDTO> criarEndereco(
            @PathVariable Long id,
            @RequestBody EnderecoDTO enderecoDTO
    ){
        return ResponseEntity.ok(clienteService.cadastrarEndereco(id, enderecoDTO));
    }

    @Operation(summary = "Cadastrar endereço para cliente")
    @GetMapping("{id}/enderecos")
    public ResponseEntity<EnderecoDTO> obterEndereco(
            @PathVariable Long id
    ){
        return ResponseEntity.ok(clienteService.obterEndereco(id));
    }

    @Operation(summary = "Atualizar endereço do cliente")
    @PutMapping("/{clienteId}/enderecos")
    public ResponseEntity<EnderecoDTO> atualizarEndereco(
            @PathVariable Long clienteId,
            @RequestBody EnderecoDTO enderecoDTO
    ) {
        EnderecoDTO enderecoAtualizado = clienteService.atualizarEndereco(clienteId, enderecoDTO);
        return ResponseEntity.ok(enderecoAtualizado);
    }

    @Operation(summary = "Remover endereço do cliente")
    @DeleteMapping("/{clienteId}/enderecos")
    public ResponseEntity<Void> removerEndereco(@PathVariable Long clienteId) {
        clienteService.removerEndereco(clienteId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Buscar pedidos do cliente")
    @GetMapping("/{id}/pedidos")
    public ResponseEntity<List<PedidoResponseDTO>> obterPedidosDoCliente(
            @PathVariable Long id
    ){
        return ResponseEntity.ok(clienteService.obterPedidosDoCliente(id));
    }


}
