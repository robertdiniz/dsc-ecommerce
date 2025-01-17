package br.ifrn.edu.jeferson.ecommerce.service;

import br.ifrn.edu.jeferson.ecommerce.domain.Cliente;
import br.ifrn.edu.jeferson.ecommerce.domain.Endereco;
import br.ifrn.edu.jeferson.ecommerce.domain.dtos.ClienteRequestDTO;
import br.ifrn.edu.jeferson.ecommerce.domain.dtos.ClienteResponseDTO;
import br.ifrn.edu.jeferson.ecommerce.exception.BusinessException;
import br.ifrn.edu.jeferson.ecommerce.mapper.ClienteMapper;
import br.ifrn.edu.jeferson.ecommerce.repository.ClienteRepository;
import br.ifrn.edu.jeferson.ecommerce.repository.EnderecoRepository;
import org.springframework.beans.factory.annotation.Autowired;
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


    public List<ClienteResponseDTO> obterClientes(){
        List<Cliente> clientes = clienteRepository.findAll();
        return clienteMapper.toDTOList(clientes);
    };

    public ClienteResponseDTO salvar(ClienteRequestDTO clienteDTO){
        Cliente cliente = clienteMapper.toEntity(clienteDTO);

        if(clienteRepository.existsByCpf(cliente.getNome())){
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

        clienteRepository.delete(cliente);
    }
}
