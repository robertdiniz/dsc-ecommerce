package br.ifrn.edu.jeferson.ecommerce.controller;

import br.ifrn.edu.jeferson.ecommerce.domain.dtos.ClienteRequestDTO;
import br.ifrn.edu.jeferson.ecommerce.domain.dtos.ClienteResponseDTO;
import br.ifrn.edu.jeferson.ecommerce.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<String> obterTodos() {
        return ResponseEntity.ok("Clientes obtidos com sucesso");
    }

}
