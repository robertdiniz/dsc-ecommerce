package br.ifrn.edu.jeferson.ecommerce.service;

import br.ifrn.edu.jeferson.ecommerce.domain.Cliente;
import br.ifrn.edu.jeferson.ecommerce.domain.Endereco;
import br.ifrn.edu.jeferson.ecommerce.domain.Pedido;
import br.ifrn.edu.jeferson.ecommerce.domain.dtos.ClienteRequestDTO;
import br.ifrn.edu.jeferson.ecommerce.domain.dtos.ClienteResponseDTO;
import br.ifrn.edu.jeferson.ecommerce.domain.dtos.EnderecoDTO;
import br.ifrn.edu.jeferson.ecommerce.domain.dtos.PedidoResponseDTO;
import br.ifrn.edu.jeferson.ecommerce.exception.BusinessException;
import br.ifrn.edu.jeferson.ecommerce.mapper.ClienteMapper;
import br.ifrn.edu.jeferson.ecommerce.mapper.EnderecoMapper;
import br.ifrn.edu.jeferson.ecommerce.mapper.PedidoMapper;
import br.ifrn.edu.jeferson.ecommerce.repository.ClienteRepository;
import br.ifrn.edu.jeferson.ecommerce.repository.EnderecoRepository;
import br.ifrn.edu.jeferson.ecommerce.repository.PedidoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ClienteMapper clienteMapper;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private EnderecoMapper enderecoMapper;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private PedidoMapper pedidoMapper;

    public Page<ClienteResponseDTO> obterClientes(Pageable pageable){
        log.info("Obtendo lista paginada de clientes");
        Page<Cliente> clientes = clienteRepository.findAll(pageable);
        log.info("Foram encontrados {} clientes", clientes.getTotalElements());
        return clientes.map(clienteMapper::toDTO);
    };

    public ClienteResponseDTO salvar(ClienteRequestDTO clienteDTO){
        log.info("Salvando novo cliente: CPF={}, Email={}", clienteDTO.getCpf(), clienteDTO.getEmail());
        Cliente cliente = clienteMapper.toEntity(clienteDTO);

        if(clienteRepository.existsByCpf(cliente.getCpf())){
            log.error("Erro ao salvar cliente: já existe um cliente com o CPF {}", cliente.getCpf());
            throw new BusinessException("Já existe um cliente com esse CPF");
        }

        if(clienteRepository.existsByEmail(cliente.getEmail())){
            log.error("Erro ao salvar cliente: já existe um cliente com o Email {}", cliente.getEmail());
            throw new BusinessException("Já existe um cliente com esse Email");
        }

        if (cliente.getEndereco() != null) {
            Endereco endereco = cliente.getEndereco();
            endereco.setCliente(cliente);
            enderecoRepository.save(endereco);
            cliente.setEndereco(endereco);
            log.info("Endereço cadastrado para o cliente: {}", endereco);
        }

        clienteRepository.save(cliente);
        log.info("Cliente salvo com sucesso: ID={}", cliente.getId());
        return clienteMapper.toDTO(cliente);
    }

    public ClienteResponseDTO obterClientePorId(Long id) {
        log.info("Buscando cliente pelo ID {}", id);
        Cliente cliente = clienteRepository.findById(id)

                .orElseThrow(() -> {
                    log.error("Cliente com ID {} não encontrado", id);
                    return new BusinessException("Cliente não encontrado com o ID: " + id);
                });

        return clienteMapper.toDTO(cliente);
    }

    public ClienteResponseDTO atualizarCliente(Long id, ClienteRequestDTO clienteRequestDTO){
        log.info("Atualizando cliente com ID {}", id);
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Cliente com ID {} não encontrado", id);
                    return new BusinessException("Cliente não encontrado com o ID: " + id);
                });

        if(!cliente.getCpf().equals(clienteRequestDTO.getCpf()) &&
            clienteRepository.existsByCpf(clienteRequestDTO.getCpf())
        ) {
            log.error("Erro ao atualizar cliente: já existe um cliente com o CPF {}", clienteRequestDTO.getCpf());
            throw new BusinessException("Já existe um cliente com este CPF.");
        }

        if (!cliente.getEmail().equals(clienteRequestDTO.getEmail()) &&
                clienteRepository.existsByEmail(clienteRequestDTO.getEmail()))
        {
            log.error("Erro ao atualizar cliente: já existe um cliente com o Email {}", clienteRequestDTO.getEmail());
            throw new BusinessException("Já existe um cliente com este Email.");
        }

        clienteMapper.updateEntityFromDTO(clienteRequestDTO, cliente);

        clienteRepository.save(cliente);
        log.info("Cliente com ID {} atualizado com sucesso.", id);
        return clienteMapper.toDTO(cliente);
    }

    public void deletar(Long id){
        log.info("Tentando deletar cliente com ID {}", id);
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Cliente com ID {} não encontrado", id);
                    return new BusinessException("Cliente não encontrado com o ID: " + id);
                });

        if(!cliente.getPedidos().isEmpty()){
            log.warn("Tentativa de deletar cliente com ID {} falhou: possui pedidos associados", id);
            throw new BusinessException("Não é possível remover o cliente, pois ele possui pedidos.");
        }

        if (cliente.getEndereco() != null) {
            enderecoRepository.delete(cliente.getEndereco());
        }

        clienteRepository.deleteById(id);
        log.info("Cliente com ID {} deletado com sucesso.", id);
    }

    public EnderecoDTO cadastrarEndereco(Long clienteId, EnderecoDTO endereco) {
        log.info("Cadastrando endereço para cliente com ID {}", clienteId);
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> {
                    log.error("Cliente com ID {} não encontrado", clienteId);
                    return new RuntimeException("Cliente não encontrado");
                });

        Endereco enderecoObj = enderecoMapper.toEntity(endereco);
        enderecoObj.setCliente(cliente);

        enderecoRepository.save(enderecoObj);
        log.info("Endereço cadastrado com sucesso para cliente ID {}", clienteId);
        return enderecoMapper.toDTO(enderecoObj);
    }

    public EnderecoDTO obterEndereco(Long id) {
        log.info("Obtendo endereço do cliente com ID {}", id);
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Cliente com ID {} não encontrado", id);
                    return new RuntimeException("Cliente não encontrado");
                });
        return enderecoMapper.toDTO(cliente.getEndereco());
    }

    public EnderecoDTO atualizarEndereco(Long clienteId, EnderecoDTO enderecoDTO) {
        log.info("Atualizando endereço do cliente com ID {}", clienteId);
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> {
                    log.error("Cliente com ID {} não encontrado", clienteId);
                    return new RuntimeException("Cliente não encontrado");
                });

        Endereco endereco = cliente.getEndereco();
        enderecoMapper.updateEntityFromDTO(enderecoDTO, endereco);
        enderecoRepository.save(endereco);
        log.info("Endereço atualizado com sucesso para cliente ID {}", clienteId);
        return enderecoMapper.toDTO(endereco);
    }

    public void removerEndereco(Long clienteId) {
        log.info("Removendo endereço do cliente com ID {}", clienteId);
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> {
                    log.error("Cliente com ID {} não encontrado", clienteId);
                    return new RuntimeException("Cliente não encontrado");
                });

        Endereco endereco = cliente.getEndereco();
        if (endereco != null) {
            enderecoRepository.delete(endereco);
            log.info("Endereço do cliente ID {} removido com sucesso.", clienteId);
        }
    }

    public List<PedidoResponseDTO> obterPedidosDoCliente(Long id) {
        log.info("Obtendo pedidos do cliente com ID {}", id);
        List<Pedido> pedidos = pedidoRepository.findByClienteId(id);
        log.info("Cliente com ID {} possui {} pedidos", id, pedidos.size());
        return pedidoMapper.toDTOList(pedidos);
    }
}
