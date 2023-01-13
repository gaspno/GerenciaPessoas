package com.teste.GerenciaPessoas.dtos;

import com.teste.GerenciaPessoas.entities.Endereco;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnderecoDto implements Serializable {
    @NotEmpty
    private  String logradouro;
    @NotEmpty
    private  String cep;
    @NotNull
    private  Integer numero;
    @NotEmpty
    private  String cidade;
    @NotNull
    private  Boolean isPrincipal;

    public EnderecoDto(Endereco endereco) {
        logradouro=endereco.getLogradouro();
        cep=endereco.getCep();
        numero=endereco.getNumero();
        cidade=endereco.getCidade();
        isPrincipal=endereco.isPrincipal();
    }
}