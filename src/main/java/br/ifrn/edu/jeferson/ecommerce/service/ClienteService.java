package br.ifrn.edu.jeferson.ecommerce.service;

import br.ifrn.edu.jeferson.ecommerce.domain.Cliente;
import br.ifrn.edu.jeferson.ecommerce.domain.Endereco;
import br.ifrn.edu.jeferson.ecommerce.domain.dtos.ClienteRequestDTO;
import br.ifrn.edu.jeferson.ecommerce.domain.dtos.ClienteResponseDTO;
import br.ifrn.edu.jeferson.ecommerce.domain.dtos.EnderecoDTO;
import br.ifrn.edu.jeferson.ecommerce.exception.BusinessException;
import br.ifrn.edu.jeferson.ecommerce.mapper.ClienteMapper;
import br.ifrn.edu.jeferson.ecommerce.mapper.EnderecoMapper;
import br.ifrn.edu.jeferson.ecommerce.repository.ClienteRepository;
import br.ifrn.edu.jeferson.ecommerce.repository.EnderecoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ClienteMapper clienteMapper;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private EnderecoMapper enderecoMapper;


    public Page<ClienteResponseDTO> obterClientes(Pageable pageable){
        Page<Cliente> clientes = clienteRepository.findAll(pageable);
        return clientes.map(clienteMapper::toDTO);
    };

    public ClienteResponseDTO salvar(ClienteRequestDTO clienteDTO){
        Cliente cliente = clienteMapper.toEntity(clienteDTO);

        if(clienteRepository.existsByCpf(cliente.getCpf())){
            throw new BusinessException("Já existe um cliente com esse CPF");
        }

        if(clienteRepository.existsByEmail(cliente.getEmail())){
            throw new BusinessException("Já existe um cliente com esse Email");
        }

        if (cliente.getEndereco() != null) {
            Endereco endereco = cliente.getEndereco();
            endereco.setCliente(cliente);
            enderecoRepository.save(endereco);
            cliente.setEndereco(endereco);
        }

        clienteRepository.save(cliente);

        return clienteMapper.toDTO(cliente);
    }

    public ClienteResponseDTO obterClientePorId(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Cliente não encontrado com o ID: " + id));

        return clienteMapper.toDTO(cliente);
    }

    public ClienteResponseDTO atualizarCliente(Long id, ClienteRequestDTO clienteRequestDTO){
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Cliente não encontrado com o ID: " + id));

        if(!cliente.getCpf().equals(clienteRequestDTO.getCpf()) &&
            clienteRepository.existsByCpf(clienteRequestDTO.getCpf())
        ) {
            throw new BusinessException("Já existe um cliente com este CPF.");
        }

        if (!cliente.getEmail().equals(clienteRequestDTO.getEmail()) &&
                clienteRepository.existsByEmail(clienteRequestDTO.getEmail())) {
            throw new BusinessException("Já existe um cliente com este Email.");
        }

        clienteMapper.updateEntityFromDTO(clienteRequestDTO, cliente);

        clienteRepository.save(cliente);

        return clienteMapper.toDTO(cliente);
    }

    public void deletar(Long id){
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Cliente não encontrado com o ID: " + id));

        if(!cliente.getPedidos().isEmpty()){
            throw new BusinessException("Não é possível remover o cliente, pois ele possui pedidos.");
        }

        clienteRepository.delete(cliente);
    }

    public EnderecoDTO cadastrarEndereco(Long clienteId, EnderecoDTO endereco) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        Endereco enderecoObj = enderecoMapper.toEntity(endereco);
        enderecoObj.setCliente(cliente);

        enderecoRepository.save(enderecoObj);

        return enderecoMapper.toDTO(enderecoObj);
    }

    public EnderecoDTO obterEndereco(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        return enderecoMapper.toDTO(cliente.getEndereco());
    }

    public EnderecoDTO atualizarEndereco(Long clienteId, EnderecoDTO enderecoDTO) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        Endereco endereco = cliente.getEndereco();
        enderecoMapper.updateEntityFromDTO(enderecoDTO, endereco);
        enderecoRepository.save(endereco);

        return enderecoMapper.toDTO(endereco);
    }

    public void removerEndereco(Long clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        Endereco endereco = cliente.getEndereco();
        if (endereco != null) {
            enderecoRepository.delete(endereco);
        }
    }

}
