package br.ifrn.edu.jeferson.ecommerce.domain.specificacion;


import br.ifrn.edu.jeferson.ecommerce.domain.Produto;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;

public class ProdutoSpecification {

    public static Specification<Produto> nomeContains(String nome){
        return(root, query, builder) -> {
            if(ObjectUtils.isEmpty(nome)){
                return null;
            }
            return builder.like(root.get("nome"), "%" + nome + "%");
        };
    }

    public static Specification<Produto> precoMin(BigDecimal precoMin){
        return(root, query, builder) -> {
            if(ObjectUtils.isEmpty(precoMin)){
                return null;
            }
            return builder.greaterThanOrEqualTo(root.get("preco"), precoMin);
        };
    }

    public static Specification<Produto> precoMax(BigDecimal precoMax){
        return(root, query, builder) -> {
            if(ObjectUtils.isEmpty(precoMax)){
                return null;
            }
            return builder.lessThanOrEqualTo(root.get("preco"), precoMax);
        };
    }

    public static Specification<Produto> estoque(Integer estoque){
        return(root, query, builder) -> {
            if(ObjectUtils.isEmpty(estoque)){
                return null;
            }
            return builder.lessThanOrEqualTo(root.get("estoque"), estoque);
        };
    }

}
