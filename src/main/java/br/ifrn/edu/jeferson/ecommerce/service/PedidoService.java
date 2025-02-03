package br.ifrn.edu.jeferson.ecommerce.service;

import br.ifrn.edu.jeferson.ecommerce.domain.Pedido;
import br.ifrn.edu.jeferson.ecommerce.domain.dtos.ItemPedidoResponseDTO;
import br.ifrn.edu.jeferson.ecommerce.domain.dtos.PedidoRequestDTO;
import br.ifrn.edu.jeferson.ecommerce.domain.dtos.PedidoResponseDTO;
import br.ifrn.edu.jeferson.ecommerce.domain.enums.StatusPedido;
import br.ifrn.edu.jeferson.ecommerce.mapper.PedidoMapper;
import br.ifrn.edu.jeferson.ecommerce.repository.ItemPedidoRepository;
import br.ifrn.edu.jeferson.ecommerce.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    @Autowired
    private PedidoMapper pedidoMapper;

    public PedidoResponseDTO criarPedido(Pedido pedido) {
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setValorTotal(calcularValorTotal(pedido));

        Pedido pedidoSalvo = pedidoRepository.save(pedido);
        return pedidoMapper.toDTO(pedidoSalvo);
    }

    public Page<PedidoResponseDTO> listarPedidos(Pageable pageable) {
        Page<Pedido> pedidos = pedidoRepository.findAll(pageable);
        return pedidos.map(pedidoMapper::toDTO);
    }

    public PedidoResponseDTO buscarPedidoPorId(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
        return pedidoMapper.toDTO(pedido);
    }

    public void atualizarStatusPedido(Long id, String novoStatus) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        try {
            pedido.setStatusPedido(StatusPedido.valueOf(novoStatus.toUpperCase().trim()));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Status inválido: " + novoStatus);
        }

        pedidoRepository.save(pedido);
    }

    public List<PedidoResponseDTO> listarPedidosPorCliente(Long clienteId) {
        List<Pedido> pedidos = pedidoRepository.findByClienteId(clienteId);
        return pedidoMapper.toDTOList(pedidos);
    }

    private BigDecimal calcularValorTotal(Pedido pedido) {
        return pedido.getItens().stream()
                .map(item -> item.getValorUnitario().multiply(new BigDecimal(item.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
