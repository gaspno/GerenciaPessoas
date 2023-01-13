package com.teste.GerenciaPessoas.controllers;

import com.teste.GerenciaPessoas.dtos.EnderecoDto;
import com.teste.GerenciaPessoas.entities.Endereco;
import com.teste.GerenciaPessoas.service.EnderecoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/pessoa")
public class EnderecoController {
    @Autowired
    private EnderecoService enderecoService;

    @GetMapping(path = "{id}/endereco")
    public ResponseEntity<List<EnderecoDto>> getEnderecosPessoa(@PathVariable("id") String id){
        List<Endereco> enderecos=enderecoService.getEnderecosFromPessoaId(Long.valueOf(id));
        List<EnderecoDto> dtos= enderecos.stream().map(EnderecoDto::new).toList();
        return ResponseEntity.ok(dtos);
    }
    @GetMapping(path = "{id}/enderecoprincipal")
    public ResponseEntity<EnderecoDto> getEnderecoPrincipalPessoa(@PathVariable("id") String id){
        Endereco endereco=enderecoService.getEnderecoPrincipalDePessoaId(Long.valueOf(id));
        return ResponseEntity.ok(new EnderecoDto(endereco));
    }
    @PutMapping(path = "{id}/endereco/{idEndereco}")
    public ResponseEntity<EnderecoDto> updateEnderecosPessoa(@PathVariable("id") String id ,@PathVariable("idEndereco") String idEndereco,@Valid @RequestBody EnderecoDto enderecoDto){

        Endereco endereco=enderecoService.update(Long.valueOf(id),Long.valueOf(idEndereco),enderecoDto);
        return ResponseEntity.ok(new EnderecoDto(endereco));
    }
    @DeleteMapping(path = "{id}/endereco/{idEndereco}")
    public ResponseEntity removeEnderecosPessoa(@PathVariable("id") String id ,@PathVariable("idEndereco") String idEndereco){
        enderecoService.delete(Long.valueOf(id),Long.valueOf(idEndereco));
        return ResponseEntity.noContent().build();
    }
}
