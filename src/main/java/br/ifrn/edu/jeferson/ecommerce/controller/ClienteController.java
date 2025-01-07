package br.ifrn.edu.jeferson.ecommerce.controller;

import br.ifrn.edu.jeferson.ecommerce.domain.dtos.ClienteRequestDTO;
import br.ifrn.edu.jeferson.ecommerce.domain.dtos.ClienteResponseDTO;
import br.ifrn.edu.jeferson.ecommerce.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@Tag(name = "Clientes", description = "API de gerenciamento de clientes.")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Operation(summary = "Criar cliente")
    @PostMapping
    public ResponseEntity<ClienteResponseDTO> salvar(@RequestBody ClienteRequestDTO clienteRequestDTO){
        return ResponseEntity.ok(clienteService.salvar(clienteRequestDTO));
    }

    @Operation(summary = "Obter todos os clientes")
    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> obterTodos() {
        return ResponseEntity.ok(clienteService.obterClientes());
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

    // Implementar o de pedidos...

}
