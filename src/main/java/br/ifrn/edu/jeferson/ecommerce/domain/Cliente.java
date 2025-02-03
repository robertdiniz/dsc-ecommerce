package br.ifrn.edu.jeferson.ecommerce.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private String cpf;
    private String telefone;

    @OneToOne(mappedBy = "cliente")
    @JsonBackReference
    private Endereco endereco;

    @OneToMany(mappedBy = "cliente")
    private List<Pedido> pedidos = new ArrayList<>();

    @PreRemove
    private void verificarPedidosAntesDeRemover() {
        if (!pedidos.isEmpty()) {
            throw new IllegalStateException("Não é possível remover o cliente, pois ele possui pedidos.");
        }
    }

}
