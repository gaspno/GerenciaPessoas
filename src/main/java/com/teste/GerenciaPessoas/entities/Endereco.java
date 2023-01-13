package com.teste.GerenciaPessoas.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.teste.GerenciaPessoas.dtos.EnderecoDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "endereco")

public class Endereco {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @NotNull
    private String logradouro;
    @NotNull
    private String cep;
    @NotNull
    private Integer numero;
    @NotNull
    private String cidade;
    @NotNull
    private boolean isPrincipal;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "endereco_pessoa_id",referencedColumnName = "id")
    private Pessoa pessoa;

    public Endereco(EnderecoDto enderecoDto) {
        this.logradouro=enderecoDto.getLogradouro();
        this.cep=enderecoDto.getCep();
        this.numero=enderecoDto.getNumero();
        this.cidade=enderecoDto.getCidade();
        isPrincipal=enderecoDto.getIsPrincipal();
    }
}