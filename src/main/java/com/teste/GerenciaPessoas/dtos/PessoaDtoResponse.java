package com.teste.GerenciaPessoas.dtos;

import com.teste.GerenciaPessoas.entities.Pessoa;
import com.teste.GerenciaPessoas.utils.DateUtil;
import lombok.Data;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class PessoaDtoResponse implements Serializable {

    private final Long id;

    private final String nome;

    private final String data;
    private List<EnderecoDto> enderecos;

    public PessoaDtoResponse(Pessoa pessoa) {
        this.id=pessoa.getId();
        this.nome= pessoa.getNome();
        this.data= DateUtil.stringFromDate(pessoa.getData());
        enderecos=pessoa.getEnderecos().stream().map(e->new EnderecoDto(e)).collect(Collectors.toList());
    }
}
