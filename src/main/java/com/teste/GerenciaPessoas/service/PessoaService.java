package com.teste.GerenciaPessoas.service;

import com.teste.GerenciaPessoas.dtos.EnderecoDto;
import com.teste.GerenciaPessoas.dtos.PessoaDtoRequest;
import com.teste.GerenciaPessoas.entities.Endereco;
import com.teste.GerenciaPessoas.entities.Pessoa;
import com.teste.GerenciaPessoas.exception.business.DateFormatException;
import com.teste.GerenciaPessoas.exception.business.NotFoundPessoaException;
import com.teste.GerenciaPessoas.repositories.EnderecoRepository;
import com.teste.GerenciaPessoas.repositories.PessoaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PessoaService {

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Transactional
    public Pessoa save(PessoaDtoRequest dto)  {
        Pessoa pessoa= null;
        try {
            pessoa = dto.getPessoaFromDTO();
        } catch (ParseException e) {
            throw new DateFormatException(e);
        }
        pessoa= pessoaRepository.save(pessoa);
        if(!dto.getEnderecos().isEmpty()) {
            Long id=pessoa.getId();
            dto.getEnderecos().forEach(e -> {
                if(e.getCidade()!=null&&e.getCep()!=null&&e.getLogradouro()!=null&&e.getNumero()!=null){
                    addEndereco(e, id);
                }
            });
        }
        return pessoa;
    }
    @Transactional
    public Pessoa update(PessoaDtoRequest pessoaDto,Long id) {
            existsPessoa(id,"Não existe pessoa com esse id cadastrado");
            Pessoa p=pessoaRepository.getReferenceById(id);
            if(!pessoaDto.getEnderecos().isEmpty()){
                removeEnderecosAntigos(p);
                List<Endereco> enderecos=new ArrayList<>();
                p.setEnderecos(enderecos);
                pessoaDto.getEnderecos().forEach(e->{
                    if(e.getCidade()!=null&&e.getCep()!=null&&e.getLogradouro()!=null&&e.getNumero()!=null){
                       addEndereco(e,p.getId());
                    }
                });
            }
            try {
                p.updatePessoa(pessoaDto);
            } catch (ParseException e) {
                throw new DateFormatException(e);
            }
            return pessoaRepository.save(p);
    }
    @Transactional
    public Pessoa getPessoaByID(Long id) {
        existsPessoa(id,"Não existe pessoa com esse id cadastrado");
        return pessoaRepository.getReferenceById(id);
    }
    @Transactional
    public List<Pessoa> getAllPessoa() {
        return pessoaRepository.findAll();
    }
    @Transactional
    public Endereco addEndereco(EnderecoDto enderecoDto, Long id) {
        existsPessoa(id,"Não existe pessoa com esse id cadastrado");
            Pessoa pessoa=pessoaRepository.getReferenceById(id);
            Endereco endereco=new Endereco(enderecoDto);
            endereco.setPessoa(pessoa);
            endereco=enderecoRepository.save(endereco);
            pessoa.getEnderecos().add(endereco);
            Long idNewEndereco=endereco.getId();
            //se apenas um endereco para pessoa torna-lo principal
            pessoa.setPrincipal(idNewEndereco, endereco.isPrincipal());
            return endereco;
    }

    @Transactional
    private void removeEnderecosAntigos(Pessoa p) {
        p.getEnderecos().forEach(e -> {
            Endereco endereco=enderecoRepository.getReferenceById(e.getId());
            endereco.setPessoa(null);
            enderecoRepository.deleteById(endereco.getId());
        });
    }
    public void delete(Long id) {
        existsPessoa(id,"Não existe pessoa com esse id no banco de dados");
        pessoaRepository.deleteById(id);
    }

    private void existsPessoa(Long id,String  msg) {
        if(!pessoaRepository.existsById(id))
            throw new NotFoundPessoaException("pessoa não encontrada no banco de dados");
    }
}
