package com.teste.GerenciaPessoas.service;

import com.teste.GerenciaPessoas.dtos.EnderecoDto;
import com.teste.GerenciaPessoas.entities.Endereco;
import com.teste.GerenciaPessoas.entities.Pessoa;
import com.teste.GerenciaPessoas.exception.business.NotFoundEnderecoException;
import com.teste.GerenciaPessoas.exception.business.NotFoundPessoaException;
import com.teste.GerenciaPessoas.repositories.EnderecoRepository;
import com.teste.GerenciaPessoas.repositories.PessoaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EnderecoService {
    @Autowired
    private PessoaRepository pessoaRepository;
    @Autowired
    private EnderecoRepository enderecoRepository;
    public List<Endereco> getEnderecosFromPessoaId(Long id) {
        isPessoaOnDatabase(id,"não existe pessoa cadastrada com esse id");
            Pessoa p=pessoaRepository.getReferenceById(id);
            List<Endereco> enderecos = new ArrayList<>(p.getEnderecos());
            return enderecos;

    }

    public Endereco getEnderecoPrincipalDePessoaId(Long id) {
        isPessoaOnDatabase(id,"não existe pessoa cadastrada com esse id");
        Pessoa p=pessoaRepository.getReferenceById(id);
        Optional<Endereco> endereco= p.getEnderecos().stream().filter(e->e.isPrincipal()).findFirst();
        if (endereco.isPresent()) {
            return endereco.get();
        }else {
            throw new NotFoundEnderecoException("pessoa não possui endereco cadastrado");
        }

    }

    @Transactional
    public Endereco update(Long id, Long idEndereco, EnderecoDto endereco) {
        isPessoaOnDatabase(id,"não existe pessoa cadastrada com esse id");
        isEnderecoOnDatabase(idEndereco,"não existe endereço cadastrada com esse id");
        Pessoa pessoa=pessoaRepository.getReferenceById(id);
        Endereco enderecoAtualizado = enderecoRepository.getReferenceById(idEndereco);
        if(pessoa.getEnderecos().contains(enderecoAtualizado)){
            atualizaEndereco(endereco, enderecoAtualizado);
            pessoa.setPrincipal(idEndereco, enderecoAtualizado.isPrincipal());
            return enderecoRepository.save(enderecoAtualizado);
        }else{
            throw new NotFoundEnderecoException("pessoa não possui endereco com esse id");
        }
    }
    @Transactional
    public void delete(Long idPessoa, Long idEndereco) {
        isPessoaOnDatabase(idPessoa,"não existe pessoa cadastrada com esse id");
        isEnderecoOnDatabase(idEndereco,"endereco invalido encontrado");
        Pessoa p=pessoaRepository.findById(idPessoa).get();
            Endereco endereco= enderecoRepository.getReferenceById(idEndereco);
            if(endereco.getPessoa().equals(p)) {
                enderecoRepository.delete(endereco);
            }
    }
    private void atualizaEndereco(EnderecoDto e,Endereco endereco) {
        endereco.setPrincipal(e.getIsPrincipal());
        endereco.setCep(e.getCep());
        endereco.setCidade(e.getCidade());
        endereco.setNumero(e.getNumero());
        endereco.setLogradouro(e.getLogradouro());
    }
    private void isPessoaOnDatabase(Long id, String errorMessage){
        if(pessoaRepository.existsById(id)){
        }else {
            throw new NotFoundPessoaException(errorMessage);
        }
    }
    private void isEnderecoOnDatabase(Long id, String errorMessage){
        if(enderecoRepository.existsById(id)){
        }else {
            throw new NotFoundEnderecoException(errorMessage);
        }
    }
}
