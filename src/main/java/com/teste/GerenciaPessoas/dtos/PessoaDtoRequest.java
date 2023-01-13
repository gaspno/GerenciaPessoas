package com.teste.GerenciaPessoas.dtos;

import com.teste.GerenciaPessoas.entities.Pessoa;
import com.teste.GerenciaPessoas.utils.DateUtil;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PessoaDtoRequest implements Serializable {
    @NotEmpty
    private String nome;
    @NotEmpty
    private String data;

    private List<EnderecoDto> enderecos=new ArrayList<>();

    public Pessoa getPessoaFromDTO() throws ParseException {
        Pessoa pessoa=new Pessoa();
        pessoa.setNome(this.getNome());
        pessoa.setData(DateUtil.dateFromString(this.data));
        return pessoa;

    }
}