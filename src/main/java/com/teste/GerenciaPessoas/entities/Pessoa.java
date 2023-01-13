package com.teste.GerenciaPessoas.entities;

import com.teste.GerenciaPessoas.dtos.PessoaDtoRequest;
import com.teste.GerenciaPessoas.utils.DateUtil;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "pessoa")
public class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @NotNull
    private String nome;
    @NotNull
    private Date data;

    @OneToMany(cascade=CascadeType.ALL, mappedBy="pessoa")
    private List<Endereco> enderecos=new ArrayList<>();

    public void setPrincipal( Long idNewEndereco, boolean isPrincipal) {
        if (this.getEnderecos().size()==1){
            this.getEnderecos().get(0).setPrincipal(true);
        }else{
            this.getEnderecos().forEach(e->{
                boolean isNewEndereco=e.getId().equals(idNewEndereco);
                if(!isNewEndereco&&isPrincipal)
                {
                    e.setPrincipal(false);
                }
            });
        }
    }

    public void updatePessoa(PessoaDtoRequest pessoaDto) throws ParseException {
        this.setNome(pessoaDto.getNome());
        this.setData(DateUtil.dateFromString(pessoaDto.getData()));
    }

}